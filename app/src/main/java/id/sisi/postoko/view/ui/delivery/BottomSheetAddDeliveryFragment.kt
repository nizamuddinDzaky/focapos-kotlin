package id.sisi.postoko.view.ui.delivery

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemDeliveryAdapter
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_DATA_DELIVERY
import id.sisi.postoko.utils.KEY_MESSAGE
import id.sisi.postoko.utils.KEY_VALIDATION_REST
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_delivery.*
import java.text.SimpleDateFormat
import java.util.*
class BottomSheetAddDeliveryFragment : BottomSheetDialogFragment(), ListItemDeliveryAdapter.OnClickListenerInterface {

    private val progressBar = CustomProgressBar()
    private lateinit var viewModel: AddDeliveryViewModel
    var listener: () -> Unit = {}
    private lateinit var adapter: ListItemDeliveryAdapter<SaleItem>
    private var customer: Customer? = null
    private var sale: Sales? = null
    private var listSaleItems  = ArrayList<SaleItem>()
    private var saleItemTemp: List<MutableMap<String, Double?>>? =  mutableListOf()
    private var alert = MyDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add_delivery, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())

        sale = arguments?.getParcelable("sale_booking")
        customer = arguments?.getParcelable(KEY_DATA_DELIVERY)
        customer.let {
            et_add_delivery_customer_name?.setText(customer?.name)
            et_add_delivery_customer_address?.setText(customer?.address)
        }
        for (x in 0 until sale?.saleItems?.size!!){
            sale?.saleItems?.get(x)?.let {
                it.quantity = it.quantity?.minus(it.sent_quantity)
                listSaleItems.add(it)
            }
        }

        saleItemTemp = listSaleItems.map {
            return@map mutableMapOf(
                "sale_items_id" to it.id?.toDouble(),
                "sent_quantity" to it.quantity
            )
        }

        viewModel = ViewModelProvider(
            this,
            AddDeliveryFactory(sale?.id ?: 0)
        ).get(AddDeliveryViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {
                context?.let { c ->
                    progressBar.show(c, getString(R.string.txt_please_wait))
                }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        viewModel.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })


        /*viewModelCustomer = ViewModelProvider(this).get(MasterDetailViewModel::class.java)
        viewModelCustomer.getDetailCustomer().observe(viewLifecycleOwner, Observer {
            customer = it
            et_add_delivery_customer_name?.setText(customer?.name)
            et_add_delivery_customer_address?.setText(customer?.address)
        })
        viewModelCustomer.requestDetailCustomer(sale?.customer_id ?: 1)*/

        //setupUI
        et_add_delivery_date?.setText(currentDate.toDisplayDate())
        et_add_delivery_date?.hint = currentDate.toDisplayDate()
        et_add_delivery_date?.tag = currentDate
        et_add_delivery_date.setOnClickListener {

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val date = if (et_add_delivery_date.tag == null){
                inputDateFormat.format(Date())
            }else{
                et_add_delivery_date.tag.toString() + " 00:00:00"
            }
            val resultDate = inputDateFormat.parse(date)
            val calendar: Calendar = GregorianCalendar()
            resultDate?.let {
                calendar.time = resultDate
            }
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]

            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, _, monthOfYear, dayOfMonth ->
                        val parseDate =
                            inputDateFormat.parse("$year-${monthOfYear + 1}-$dayOfMonth 00:00:00")
                        parseDate?.let {
                            val selectedDate = inputDateFormat.format(parseDate)
                            et_add_delivery_date.setText(selectedDate.toDisplayDate())
                            et_add_delivery_date?.tag = selectedDate

                        }
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }

        et_add_delivery_sales_ref?.setText(sale?.reference_no)

        adapter = ListItemDeliveryAdapter(listSaleItems)
        adapter.listenerProduct = this

        rv_list_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_data?.setHasFixedSize(false)
        rv_list_data?.adapter = adapter

        val mandatory = listOf<EditText>(
            et_add_delivery_date,
            et_add_delivery_sales_ref,
            et_add_delivery_customer_name,
            et_add_delivery_customer_address
        )
        btn_confirmation_add_delivery?.setOnClickListener {
            if (!mandatory.validation()) {
                return@setOnClickListener
            }
            actionAddDelivery()
        }

        for (i in 0 until (rg_add_delivery_status?.childCount ?: 0)) {
            (rg_add_delivery_status?.get(i) as? RadioButton)?.tag =
                DeliveryStatus.values()[i].name.toLowerCase(
                    Locale.getDefault()
                )
        }
        rg_add_delivery_status?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_add_delivery_status?.tag = radioButton.tag
        }
        rg_add_delivery_status?.check(rg_add_delivery_status?.get(0)?.id ?: 0)

        btn_close.setOnClickListener {
            this.dismiss()
        }

        tv_add_note.setOnClickListener {
            val dialog = MyDialog()
            dialog.note(getString(R.string.txt_delivery_note), tv_note.text.toString(), context)
            dialog.listenerPositifNote = {
                tv_add_note.gone()
                tv_edit_note.visible()
                tv_note.text = it
            }
        }

        tv_edit_note.setOnClickListener {
            val dialog = MyDialog()
            dialog.note(getString(R.string.txt_delivery_note), tv_note.text.toString(), context)
            dialog.listenerPositifNote = {
                tv_add_note.gone()
                tv_edit_note.visible()
                tv_note.text = it
            }
        }
        /*deliveryNote*/
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as DeliveryFragment).refreshDataSale()
    }

    private fun actionAddDelivery() {

        val validation =  validationFormAddDelivery()
        if (validation[KEY_VALIDATION_REST] as Boolean) {
            val saleItems = listSaleItems.map {
                return@map mutableMapOf(
                    "sale_items_id" to it.id.toString(),
                    "sent_quantity" to it.quantity.toString()
                )
            }
            logE("items : $saleItems")

            /*val body = mutableMapOf(
                "date" to (et_add_delivery_date?.tag?.toString() ?: ""),
                "sale_reference_no" to (et_add_delivery_sales_ref?.text?.toString() ?: ""),
                "customer" to (et_add_delivery_customer_name?.text?.toString() ?: ""),
                "address" to (et_add_delivery_customer_address?.text?.toString() ?: ""),
                "status" to (rg_add_delivery_status?.tag?.toString() ?: ""),
                "delivered_by" to (et_add_delivery_delivered_by?.text?.toString() ?: ""),
                "received_by" to (et_add_delivery_received_by?.text?.toString() ?: ""),
                "products" to saleItems,
                "note" to (tv_note.text.toString())
            )
            viewModel.postAddDelivery(body) {
                listener()
                this.dismiss()
            }*/
        }else{
            alert.alert(validation[KEY_MESSAGE] as String, context)
        }
    }

    private fun validationFormAddDelivery(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (listSaleItems.size < 1){
            message += "- ${getString(R.string.txt_alert_item_sale)}\n"
            cek = false
        }
        return mapOf(KEY_MESSAGE to message, KEY_VALIDATION_REST to cek)
    }

   /* override fun onClickPlus(qty: Double, position: Int) {
        val quantity = listSaleItems[position].quantity?.plus(1)
        if (quantity != null) {
            if (quantity > (saleItemTemp?.get(position)?.get("sent_quantity") ?: 0.0)){
                alert.alert(getString(R.string.txt_alert_out_of_qty), context)
            }else{
                listSaleItems[position].quantity = quantity
                adapter.notifyDataSetChanged()
            }
        }
    }*/

    /*override fun onClickMinus(qty: Double, position: Int) {
        val quantity = listSaleItems[position].quantity?.minus(1.0)
        if (quantity != null) {
            if (quantity < 1){
                alert.confirmation(getString(R.string.txt_are_you_sure), context)
                alert.listenerPositif = {
                    listSaleItems[position].quantity = quantity
                    listSaleItems.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                alert.listenerNegatif = {
                    adapter.notifyDataSetChanged()
                }
            }else{
                listSaleItems[position].quantity = quantity
                listSaleItems[position].subtotal = listSaleItems[position].quantity?.times(listSaleItems[position].unit_price!!)
                adapter.notifyDataSetChanged()
            }
        }
    }*/

    override fun onClickDelete(position: Int) {
        alert.confirmation(getString(R.string.txt_are_you_sure), context)
        alert.listenerPositif = {
            listSaleItems.removeAt(position)
            adapter.notifyDataSetChanged()
        }
        alert.listenerNegatif = {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onChange(position: Int, qty: String): Boolean {
        var ret = false
        if (!TextUtils.isEmpty(qty)){
            if ((qty.toDouble()) > (saleItemTemp?.get(position)?.get("sent_quantity") ?: 0.0)){
                alert.alert(getString(R.string.txt_alert_out_of_qty), context)
            }else if ((qty.toDouble()) < 1){
                alert.confirmation(getString(R.string.txt_are_you_sure), context)
                alert.listenerPositif = {
                    listSaleItems[position].quantity = qty.toDouble()
                    listSaleItems.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                alert.listenerNegatif = {
                    ret = true
                }
            }
            else{
                listSaleItems[position].quantity = (qty.toDouble())
            }
        }
        return ret
    }
}


