package id.sisi.postoko.view.ui.customergroup

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_CUSTOMER_GROUP
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_form_customer_group.*

class BottomSheetEditCustomerGroup: BottomSheetDialogFragment() {
    private var customerGroup: CustomerGroup = CustomerGroup(id = "0", name = "ForcaPoS")
    private val progressBar = CustomProgressBar()
    private lateinit var mViewModel: CustomerGroupViewModel
    var listener: () -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_form_customer_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)
        et_customer_group_kredit_limit.addTextChangedListener(NumberSeparator(et_customer_group_kredit_limit))

        tv_subtitle_bottom_sheet.visible()
        tv_title_bottom_sheet.text = getString(R.string.title_activity_edit_customer_group)
        arguments?.getParcelable<CustomerGroup>(KEY_CUSTOMER_GROUP)?.let {
            customerGroup = it
            tv_subtitle_bottom_sheet.text = customerGroup.name
            et_customer_group_name?.setText(customerGroup.name)
            et_customer_group_percentage?.setText(customerGroup.percent)
            et_customer_group_kredit_limit?.setText(String.format("%.0f",customerGroup.kredit_limit))
        }

        btn_action_submit.setOnClickListener {
            actionEditCustomerGroup()
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

    private fun actionEditCustomerGroup() {
        val numbersMap = validationFormEditCustomerGroup()
        if (numbersMap["type"] as Boolean){
            context?.let { progressBar.show(it, "Silakan tunggu...") }
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_customer_group_name?.text.toString()),
                "percentage" to (et_customer_group_percentage?.text.toString()),
                "credit_limit" to (et_customer_group_kredit_limit?.tag.toString())
            )

            mViewModel.putEditCustomerGroup(body, customerGroup.id){
                Toast.makeText(context, "" + it["message"], Toast.LENGTH_SHORT).show()
                progressBar.dialog.dismiss()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    listener()
                }
            }
        }else{
            context?.let {
                AlertDialog.Builder(it)
                    .setTitle("Konfirmasi")
                    .setMessage(numbersMap["message"] as String)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                    }
                    .show()
            }
        }
    }

    private fun validationFormEditCustomerGroup(): Map<String, Any?> {
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