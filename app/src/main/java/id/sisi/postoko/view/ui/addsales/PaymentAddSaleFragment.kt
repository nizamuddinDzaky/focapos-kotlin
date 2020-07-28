package id.sisi.postoko.view.ui.addsales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.ui.delivery.BottomSheetAddDeliveryFragment
import id.sisi.postoko.view.ui.sales.SaleBookingFactory
import id.sisi.postoko.view.ui.sales.SaleBookingViewModel
import id.sisi.postoko.view.ui.sales.SaleStatus
import kotlinx.android.synthetic.main.payment_add_sale_fragment.*
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList


class PaymentAddSaleFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = null
        (activity as AddSaleActivity?)?.currentFragmentTag = TAG
        return inflater.inflate(R.layout.payment_add_sale_fragment, container, false)
    }

    private var saleNote: String? = null
    private var employeeNote: String? = null
    private lateinit var vmDetailSale: SaleBookingViewModel
    private var total = 0.0
    private var totalDisc = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saleNote = (activity as AddSaleActivity).saleNote
        employeeNote = (activity as AddSaleActivity).employeeNote
        total = (activity as AddSaleActivity).getTotal()
        totalDisc = (activity as AddSaleActivity).getDiscount()
        setUpUI()

        setUpEventUi()

        (activity as AddSaleActivity).vmAddSale.getTermOfPayment()

        rg_status_add_sale?.check(rg_status_add_sale?.get(0)?.id ?: 0)
    }

    private fun setUpEventUi() {

        rg_status_add_sale?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_add_sale.tag = radioButton.tag.toString()
        }

        et_discount_add_sale.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val str = et_discount_add_sale.text.toString().replace(",".toRegex(), "")
                if (!TextUtils.isEmpty(str)) {
                    tv_order_discount.text = str.toInt().toCurrencyID()
                }
                setUpResult()
            }
        })

        et_shipping_add_sale.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val str = et_shipping_add_sale.text.toString().replace(",".toRegex(), "")
                if (!TextUtils.isEmpty(str)){
                    tv_delivery_fee.text = str.toInt().toCurrencyID()
                }
                setUpResult()
            }
        })


        tv_add_sale_note.setOnClickListener {
            showPopUpNote(getString(R.string.txt_sale_note), saleNote ?: "", KEY_SALE)
        }

        tv_edit_sale_note.setOnClickListener {
            showPopUpNote(getString(R.string.txt_sale_note), saleNote ?: "", KEY_SALE)
        }

        tv_add_employee_note.setOnClickListener {
            showPopUpNote(getString(R.string.txt_employee_records), employeeNote ?: "", KEY_EMPLOYEE)
        }

        tv_edit_employee_note.setOnClickListener {
            showPopUpNote(getString(R.string.txt_employee_records), employeeNote ?: "", KEY_EMPLOYEE)
        }

        btn_action_submit.setOnClickListener {
            val listSelected = ArrayList<Product>()
            (activity as AddSaleActivity).listProduct.forEach {
                if (it.isSelected)
                    listSelected.add(it)
            }

            val saleItems =listSelected.map {
                return@map mutableMapOf(
                    "product_id" to it.id,
                    "price" to it.price.toString(),
                    "quantity" to it.sale_qty,
                    "discount" to it.discount
                )
            }

            val body: MutableMap<String, Any> = mutableMapOf(
                "date" to ((activity as AddSaleActivity).date ?: ""),
                "warehouse" to ((activity as AddSaleActivity).idWarehouse ?: ""),
                "customer" to ((activity as AddSaleActivity).idCustomer ?: ""),
                "order_discount" to (et_discount_add_sale?.tag?.toString() ?: "0"),
                "shipping" to (et_shipping_add_sale?.tag?.toString() ?: "0"),
                "sale_status" to (rg_status_add_sale?.tag?.toString() ?: ""),
                "payment_term" to (et_payment_term_add_sale?.text?.toString() ?: ""),
                "staff_note" to ((activity as AddSaleActivity).employeeNote ?: ""),
                "note" to ((activity as AddSaleActivity).saleNote ?: ""),
                "products" to saleItems
            )

            if(cb_create_delivery.isChecked && rg_status_add_sale?.tag?.toString() == "pending"){
                val dialog = MyDialog()
                dialog.alert(getString(R.string.txt_must_reserved), context)
            }else{
                sentDataAddSale(body)
            }
        }
    }

    private fun sentDataAddSale(body: MutableMap<String, Any>) {
        (activity as AddSaleActivity).vmAddSale.postAddSales(body){ idSale ->
            if(!cb_create_delivery.isChecked){
                finishAddSale()
            }else{
                loadSale(idSale)
            }
        }
    }

    private fun finishAddSale() {
        val returnIntent = Intent()
        returnIntent.putExtra("sale_status", rg_status_add_sale.tag.toString())
        (activity as AddSaleActivity).setResult(Activity.RESULT_OK, returnIntent)
        (activity as AddSaleActivity).finish()
    }

    private fun loadSale(idSale: String?) {

        val newCustomer = Customer(
            address = (activity as AddSaleActivity?)?.customerAddress,
            name = (activity as AddSaleActivity?)?.customerName
        )
        vmDetailSale = ViewModelProvider(
            this,
            SaleBookingFactory(idSale?.toInt() ?: 0)
        ).get(SaleBookingViewModel::class.java)
        vmDetailSale.getDetailSale().observe(viewLifecycleOwner, Observer {
            val bottomSheetFragment = BottomSheetAddDeliveryFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_ID_SALES_BOOKING, it?.id ?: 0)
            bundle.putParcelable("sale_booking", it)
            bundle.putParcelable(KEY_DATA_DELIVERY, newCustomer)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
            bottomSheetFragment.listener = {
                finishAddSale()
            }
        })
        vmDetailSale.requestDetailSale()
    }




    private fun setUpUI() {

        tv_total_item.text = (activity as AddSaleActivity).countItemSelected().toString()
        tv_total.text = total.toCurrencyID()
        tv_total_disc_item.text = totalDisc.toCurrencyID()
        tv_order_discount.text = 0.toCurrencyID()
        tv_delivery_fee.text = 0.toCurrencyID()

        setUpResult()

        et_discount_add_sale.addTextChangedListener(NumberSeparator(et_discount_add_sale))
        if (TextUtils.isEmpty((activity as AddSaleActivity).discount)){
            et_discount_add_sale.setText((activity as AddSaleActivity).discount)
        }

        et_shipping_add_sale.addTextChangedListener(NumberSeparator(et_shipping_add_sale))
        if (TextUtils.isEmpty((activity as AddSaleActivity).shipmentPrice)){
            et_shipping_add_sale.setText((activity as AddSaleActivity).shipmentPrice)
        }

        et_payment_term_add_sale.addTextChangedListener(NumberSeparator(et_payment_term_add_sale))
        if (TextUtils.isEmpty((activity as AddSaleActivity).paymentTerm)){
            et_payment_term_add_sale.setText((activity as AddSaleActivity).paymentTerm)
        }

        for (i in 0 until (rg_status_add_sale?.childCount ?: 0)) {
            (rg_status_add_sale?.get(i) as? RadioButton)?.tag =
                SaleStatus.values()[i].name.toLowerCase(
                    Locale.getDefault()
                )
        }
        setSaleNote()
        setEmployeeNote()
    }

    private fun setUpResult(){
        val strDisc = et_discount_add_sale.text.toString().replace(",".toRegex(), "")
        val strDeliveryFee = et_shipping_add_sale.text.toString().replace(",".toRegex(), "")
        var disc = 0
        var deliveryFee = 0
        if (!TextUtils.isEmpty(strDisc)){
            disc = strDisc.toInt()
        }

        if (!TextUtils.isEmpty(strDeliveryFee)){
            deliveryFee = strDeliveryFee.toInt()
        }

        val grandTotal = total - totalDisc - disc + deliveryFee
        logE("final => $total => $disc => $deliveryFee")
        tv_final_amount.text = grandTotal.toCurrencyID()
    }

    private fun setEmployeeNote() {
        if (TextUtils.isEmpty(employeeNote)){
            tv_edit_employee_note.gone()
            tv_add_employee_note.visible()
            tv_employee_note.text = getString(R.string.txt_not_set_note)
        }else{
            tv_employee_note.text = employeeNote
            tv_add_employee_note.gone()
            tv_edit_employee_note.visible()
        }
    }

    private fun setSaleNote() {
        if (TextUtils.isEmpty(saleNote)){
            tv_edit_sale_note.gone()
            tv_add_sale_note.visible()
            tv_sale_note.text = getString(R.string.txt_not_set_note)
        }else{
            tv_sale_note.text = saleNote
            tv_add_sale_note.gone()
            tv_edit_sale_note.visible()
        }
    }

    private fun showPopUpNote(title: String, saleNote: String, key: String) {
        val dialog = MyDialog()
        dialog.note(title, saleNote, context)
        dialog.listenerPositifNote = {
            if (key == KEY_SALE){
                this.saleNote = it
                (activity as AddSaleActivity).saleNote = this.saleNote
                setSaleNote()
            }else if (key == KEY_EMPLOYEE){
                this.employeeNote = it
                (activity as AddSaleActivity).employeeNote = this.employeeNote
                setEmployeeNote()
            }
        }
    }

    companion object {
        val TAG: String = PaymentAddSaleFragment::class.java.simpleName
        fun newInstance() =
            PaymentAddSaleFragment()
    }
}