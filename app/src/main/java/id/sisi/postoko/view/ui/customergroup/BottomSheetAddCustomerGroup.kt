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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_form_customer_group.*

class BottomSheetAddCustomerGroup: BottomSheetDialogFragment() {
    private lateinit var vmCustomerGroup: CustomerGroupViewModel
    private val progressBar = CustomProgressBar()
    var listener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_form_customer_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tv_title_bottom_sheet.text = getString(R.string.txt_add_customer_group)
        et_customer_group_percentage.setText("0")
        vmCustomerGroup = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)
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
            context?.let { progressBar.show(it, "Silakan tunggu...") }
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_customer_group_name?.text.toString()),
                "percentage" to (et_customer_group_percentage?.text.toString()),
                "credit_limit" to (et_customer_group_kredit_limit?.tag.toString())
            )

            vmCustomerGroup.postAddCustomerGroup(body){
                progressBar.dialog.dismiss()
                Toast.makeText(context, "" + it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    listener()
                    this.dismiss()
                }
            }
        }else{
            AlertDialog.Builder(context)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
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