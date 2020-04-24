package id.sisi.postoko.view.ui.pricegroup

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter_price_group.*

class BSFilterMemberPGandCG: BottomSheetDialogFragment() {

    private var expanded: Boolean = false
    var listener: (Map<String, Any>) -> Unit= {}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = R.layout.fragment_bottom_sheet_filter_price_group
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!expanded){
            expandable_layout.collapse(true)
        }else{
            expandable_layout.expand(true)
        }

        layout_filter_region.setOnClickListener {
            expanded = !expanded

            if (!expanded){
                iv_arrow_up.gone()
                iv_arrow_down.visible()
                expandable_layout.collapse(true)
            }else{
                iv_arrow_up.visible()
                iv_arrow_down.gone()
                expandable_layout.expand(true)
            }
        }

        arguments?.getString("strFilter").let {
            et_filter_customer_name.setText(it)
        }

        btn_close.setOnClickListener {
            this.dismiss()
        }
        tv_reset.setOnClickListener {
            et_filter_customer_name.text = null
            listener(
                mapOf("filter" to (et_filter_customer_name.text.toString()))
            )
            this.dismiss()
        }

        btn_action_apply.setOnClickListener {
            listener(
                mapOf("filter" to (et_filter_customer_name.text.toString()))
            )
            this.dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
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
        var listener: (Map<String, Any>) -> Unit= {}
        fun show(
            fragmentManager: FragmentManager,
            strFilter: String?
        ) {

            val bottomSheetFragment = BSFilterMemberPGandCG()
            bottomSheetFragment.listener = {
                listener(it)
            }
//            bottomSheetFragment.profileType = profileType
            val bundle = Bundle()
            bundle.putString("strFilter", strFilter)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}