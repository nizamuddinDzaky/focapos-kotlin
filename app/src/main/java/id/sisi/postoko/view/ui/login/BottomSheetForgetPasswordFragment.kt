package id.sisi.postoko.view.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.profile.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_forget_password.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BottomSheetForgetPasswordFragment : BottomSheetDialogFragment() {
    private lateinit var vmProfile: ProfileViewModel
    private val typeface = TypeFace()
    private val progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vmProfile = ViewModelProvider(this).get(ProfileViewModel::class.java)

        return inflater.inflate(R.layout.fragment_bottom_sheet_forget_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.assets?.let {
            typeface.typeFace("robot_font/Roboto-Bold.ttf",tv_title_bottom_sheet,it)
            typeface.typeFace("robot_font/Roboto-Regular.ttf",tv_desc_forgot_pass,it)
            typeface.typeFace("robot_font/Roboto-Bold.ttf",tv_email,it)
            typeface.typeFace("robot_font/Roboto-Italic.ttf",tv_helper_email,it)
        }

        vmProfile.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        vmProfile.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {
                context?.let { c ->
                    progressBar.show(c, getString(R.string.txt_please_wait))
                }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        btn_action_submit.setOnClickListener {
            actionForgetPassword()
        }

        view.findViewById<ImageView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
    }

    private fun actionForgetPassword() {
        val email = et_email?.text

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && email?.length!! > 0 ){
            val body: MutableMap<String, Any> = mutableMapOf(
                "email" to (et_email?.text?.toString() ?: "")
            )

            vmProfile.postResetPassword(body){
                dismiss()
            }
        }
    }



    companion object {
        fun show(
            fragmentManager: FragmentManager
        ) {
            val bottomSheetFragment = BottomSheetForgetPasswordFragment()
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}