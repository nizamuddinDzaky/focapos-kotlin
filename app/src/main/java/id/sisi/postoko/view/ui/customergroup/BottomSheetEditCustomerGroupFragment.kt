package id.sisi.postoko.view.ui.customergroup

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_CUSTOMER_GROUP
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setIfExist
import id.sisi.postoko.view.ui.pricegroup.BottomSheetEditPriceGroupFragment
import id.sisi.postoko.view.ui.pricegroup.PriceGroupViewModel
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.content_edit_sale.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_customer_group.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_customer_group.view.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_price_group.*
import java.math.BigDecimal
import java.text.DecimalFormat

class BottomSheetEditCustomerGroupFragment : BottomSheetDialogFragment() {
    private lateinit var mViewModel: CustomerGroupViewModel
    private val numberSparator = NumberSeparator()
    private var customerGroup: CustomerGroup? = null
    var listener: () -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)
        return inflater.inflate(R.layout.fragment_bottom_sheet_edit_customer_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }

        et_customer_group_kredit_limit.addTextChangedListener(numberSparator.onTextChangedListener(et_customer_group_kredit_limit))
        customerGroup = arguments?.getParcelable<CustomerGroup>(KEY_CUSTOMER_GROUP)?.also { customerGroup ->
            et_customer_group_name?.setText(customerGroup.name)
            et_customer_group_percentage?.setText(customerGroup.percent)
            et_customer_group_kredit_limit?.setText(String.format("%.0f",customerGroup.kredit_limit))
        }

        view.btn_action_submit.setOnClickListener {
            actionEditCustomerGroup()
        }
    }

    private fun actionEditCustomerGroup() {
        val numbersMap = validationFormEditCustomerGroup()
        if (numbersMap["type"] as Boolean){
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_customer_group_name?.text.toString()),
                "percentage" to (et_customer_group_percentage?.text.toString()),
                "credit_limit" to (et_customer_group_kredit_limit?.tag.toString())
            )

            mViewModel.putEditCustomerGroup(body,customerGroup?.id.toString()){
                Toast.makeText(context, "" + it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    this.dismiss()
                    listener()
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

    companion object {
        var listener: () -> Unit = {}
        fun show(
            fragmentManager: FragmentManager,
            customerGroup: CustomerGroup
        ) {
            val bottomSheetFragment = BottomSheetEditCustomerGroupFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_CUSTOMER_GROUP, customerGroup)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
            bottomSheetFragment.listener={
                listener()
            }
        }
    }
}