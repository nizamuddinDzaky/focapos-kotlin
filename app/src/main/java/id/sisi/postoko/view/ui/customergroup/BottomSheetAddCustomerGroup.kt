package id.sisi.postoko.view.ui.customergroup

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_form_customer_group.*

class BottomSheetAddCustomerGroup: BottomSheetDialogFragment() {
    private lateinit var vmCustomerGroup: CustomerGroupViewModel
    private val progressBar = CustomProgressBar()
    private val myDialog = MyDialog()
    var listener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_form_customer_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_subtitle_bottom_sheet.gone()
        tv_title_bottom_sheet.text = getString(R.string.txt_add_customer_group)
        et_customer_group_percentage.setText("0")
        vmCustomerGroup = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)

        vmCustomerGroup.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
        vmCustomerGroup.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {

                context?.let { ctx -> progressBar.show(ctx, getString(R.string.txt_please_wait)) }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        et_customer_group_kredit_limit.addTextChangedListener(NumberSeparator(et_customer_group_kredit_limit))
        btn_action_submit.setOnClickListener {
            actionAddCustomerGroup()
        }

        btn_close.setOnClickListener {
            this.dismiss()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }

    private fun actionAddCustomerGroup() {
        val numbersMap = validationFormAddCustomerGroup()
        if (numbersMap["type"] as Boolean){
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_customer_group_name?.text.toString()),
                "percentage" to (et_customer_group_percentage?.text.toString()),
                "credit_limit" to (et_customer_group_kredit_limit?.tag.toString())
            )

            vmCustomerGroup.postAddCustomerGroup(body){
                listener()
                this.dismiss()
            }
        }else{
            myDialog.alert(numbersMap["message"] as String, context)
        }
    }

    private fun validationFormAddCustomerGroup(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (TextUtils.isEmpty(et_customer_group_name.text)){
            message += "- Nama Customer Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (TextUtils.isEmpty(et_customer_group_kredit_limit.text)){
            message += "- Kredit Limit Tidak Boleh Kosong\n"
            cek = false
        }
        return mapOf("message" to message, "type" to cek)
    }
}