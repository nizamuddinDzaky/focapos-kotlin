package id.sisi.postoko.view.ui.profile

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.User
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.extensions.MyToast
import id.sisi.postoko.utils.extensions.showErrorL
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_password.*

class BottomSheetChangePassword: BottomSheetDialogFragment() {
    private lateinit var mViewModel: ProfileViewModel
    private val progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val layoutId = R.layout.fragment_bottom_sheet_edit_profile_password
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {
                context?.let { c ->
                    progressBar.show(c, getString(R.string.txt_please_wait))
                }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        view.findViewById<ImageView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
        view.findViewById<TextView>(R.id.btn_reset)?.setOnClickListener {
            submitForm()
        }
        view.findViewById<TextView>(R.id.btn_action_submit)?.setOnClickListener {
            view.findViewById<TextView>(R.id.btn_reset)?.performClick()
        }
    }

    private fun submitForm() {
        if (!et_new_password?.text?.toString().equals(et_confirm_new_password?.text?.toString())) {
            MyToast.make(context).showErrorL(getString(R.string.txt_error_try_again_later))
            return
        }
        val body = mutableMapOf(
            "old_password" to (et_current_password?.text?.toString() ?: ""),
            "new_password" to (et_new_password?.text?.toString() ?: ""),
            "new_password_confirm" to (et_confirm_new_password?.text?.toString() ?: "")
        )
        mViewModel.putChangePassword(body){
            (activity as? ProfileActivity)?.refreshData(true)
            this.dismiss()
        }
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int { // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay
            .getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager
        ) {
            val bottomSheetFragment = BottomSheetChangePassword()
            val bundle = Bundle()
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}