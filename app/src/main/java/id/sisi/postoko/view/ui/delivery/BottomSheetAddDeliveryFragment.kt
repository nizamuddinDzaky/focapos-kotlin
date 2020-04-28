package id.sisi.postoko.view.ui.delivery

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemDeliveryAdapter
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.MasterDetailViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_delivery.*
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetAddDeliveryFragment : BottomSheetDialogFragment(), ListItemDeliveryAdapter.OnClickListenerInterface {

    private val progressBar = CustomProgressBar()
    private lateinit var viewModel: AddDeliveryViewModel
    private lateinit var viewModelCustomer: MasterDetailViewModel
    var listener: () -> Unit = {}
    private lateinit var adapter: ListItemDeliveryAdapter
    private var customer: Customer? = null
    private var sale: Sales? = null
    private var listSaleItems  = ArrayList<SaleItem>()
    private var saleItemTemp: List<MutableMap<String, Double?>>? =  mutableListOf()
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
        for (x in 0 until sale?.saleItems?.size!!){
            sale?.saleItems?.get(x)?.let { listSaleItems.add(it) }
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
            if (it) {
                logE("progress")
            } else {
                logE("done")
            }
        })

        viewModelCustomer = ViewModelProvider(this).get(MasterDetailViewModel::class.java)
        viewModelCustomer.getDetailCustomer().observe(viewLifecycleOwner, Observer {
            customer = it
            et_add_delivery_customer_name?.setText(customer?.name)
            et_add_delivery_customer_address?.setText(customer?.address)
        })
        viewModelCustomer.requestDetailCustomer(sale?.customer_id ?: 1)

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
        btn_confirmation_add_delivery?.setOnClickListener {
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
    }

    private fun actionAddDelivery() {

        val numbersMap =  validationFormAddDelivery()
        if (numbersMap["type"] as Boolean) {
            context?.let { progressBar.show(it, "Silakan tunggu...") }
            val saleItems = listSaleItems.map {
                return@map mutableMapOf(
                    "sale_items_id" to it.id.toString(),
                    "sent_quantity" to it.quantity.toString()
                )
            }

            val body = mutableMapOf(
                "date" to (et_add_delivery_date?.tag?.toString() ?: ""),
                "sale_reference_no" to (et_add_delivery_sales_ref?.text?.toString() ?: ""),
                "customer" to (et_add_delivery_customer_name?.text?.toString() ?: ""),
                "address" to (et_add_delivery_customer_address?.text?.toString() ?: ""),
                "status" to (rg_add_delivery_status?.tag?.toString() ?: ""),
                "delivered_by" to (et_add_delivery_delivered_by?.text?.toString() ?: ""),
                "received_by" to (et_add_delivery_received_by?.text?.toString() ?: ""),
                "products" to saleItems,
                "note" to (et_add_delivery_note?.text?.toString() ?: "")
            )
            viewModel.postAddDelivery(body) {
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    this.dismiss()
                }
                Toast.makeText(context, "" + it["message"], Toast.LENGTH_SHORT).show()
                progressBar.dialog.dismiss()
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

    private fun validationFormAddDelivery(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (listSaleItems.size < 1){
            message += "- Product Item Tidak Boleh Kosong\n"
            cek = false
        }
        return mapOf("message" to message, "type" to cek)
    }

    override fun onClickPlus(qty: Double, position: Int) {
        val quantity = listSaleItems[position].quantity?.plus(1)
        if (quantity != null) {
            if (quantity > saleItemTemp?.get(position)?.get("sent_quantity")!!){
                AlertDialog.Builder(context)
                    .setTitle("Konfirmasi")
                    .setMessage("Quantity Melebihi yang di Pesan")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                    }
                    .show()
            }else{
                listSaleItems[position].quantity = quantity
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClickMinus(qty: Double, position: Int) {
        val quantity = listSaleItems[position].quantity?.minus(1.0)
        if (quantity != null) {
            if (quantity < 1){
                AlertDialog.Builder(context)
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah yakin ?")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        listSaleItems[position].quantity = quantity
                        listSaleItems.removeAt(position)
                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ ->
                        adapter.notifyDataSetChanged()
                    }
                    .show()
            }else{
                listSaleItems[position].quantity = quantity
                listSaleItems[position].subtotal = listSaleItems[position].quantity?.times(listSaleItems[position].unit_price!!)
                adapter.notifyDataSetChanged()
            }
        }
    }
}