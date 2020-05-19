package id.sisi.postoko.view.ui.delivery

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import id.sisi.postoko.model.DeliveryItem
import id.sisi.postoko.utils.KEY_ID_DELIVERY
import id.sisi.postoko.utils.MyAlert
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_delivery.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BottomSheetEditDeliveryFragment : BottomSheetDialogFragment(), ListItemDeliveryAdapter.OnClickListenerInterface {

    private lateinit var adapter: ListItemDeliveryAdapter<DeliveryItem>
    private lateinit var vmDelivery: DeliveryDetailViewModel
    private var deliveryItems: List<DeliveryItem>? = arrayListOf()
    private var deliveryItemsTemp: List<MutableMap<String, Double?>>? =  mutableListOf()
    private val alert = MyAlert()
    private var idDelivery = ""
    private val progressBar = CustomProgressBar()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add_delivery, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_title_bottom_sheet.text = getString(R.string.txt_edit_delivery)
        vmDelivery = ViewModelProvider(this).get(DeliveryDetailViewModel::class.java)
        var idRadioGroupStatusDeliv = 0
        vmDelivery.getDetailDelivery().observe(viewLifecycleOwner, Observer {deliv ->
            deliv?.let { it ->
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDate = sdf.parse(it.date)
                val strCurentDate = sdf.format(currentDate)
                et_add_delivery_date.setText(strCurentDate.toDisplayDate())
                et_add_delivery_date.hint = strCurentDate.toDisplayDate()
                et_add_delivery_date.tag = strCurentDate
                et_add_delivery_reference_no.setText(it.do_reference_no)
                et_add_delivery_sales_ref.setText(it.sale_reference_no)
                et_add_delivery_delivered_by.setText(it.delivered_by)
                et_add_delivery_received_by.setText(it.received_by)
                et_add_delivery_customer_name.setText(it.customer)
                et_add_delivery_customer_name.setText(it.customer)
                et_add_delivery_customer_address.setText(it.address)

                deliveryItems = it.deliveryItems

                deliveryItemsTemp = deliveryItems?.map {deliv ->
                    return@map mutableMapOf(
                        "sent_quantity" to deliv.quantity_sent
                    )
                }

                setUpUI(it.deliveryItems)

                for (i in 0 until (rg_add_delivery_status?.childCount ?: 0)) {
                    (rg_add_delivery_status?.get(i) as? RadioButton)?.tag =
                        DeliveryStatus.values()[i].name.toLowerCase(
                            Locale.getDefault()
                        )
                    if (DeliveryStatus.values()[i].name.toLowerCase(Locale.getDefault()) == it.status) {
                        idRadioGroupStatusDeliv = i
                    }
                }

                if (it.status.equals(DeliveryStatus.DELIVERING.toString(), true)){
                    rbtn_packing.gone()
                }

                rg_add_delivery_status?.check(rg_add_delivery_status?.get(idRadioGroupStatusDeliv)?.id ?: 0)
            }
        })

        rg_add_delivery_status?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_add_delivery_status?.tag = radioButton.tag
        }

        vmDelivery.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it) {
                logE("progress")
            } else {
                logE("done")
            }
        })

        vmDelivery.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        arguments?.getString(KEY_ID_DELIVERY)?.let {
            vmDelivery.requestDetailDelivery(it.toInt())
            idDelivery = it
        }

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

        btn_confirmation_add_delivery.setOnClickListener {
            actionUpdateDeliv()
        }
    }

    private fun actionUpdateDeliv() {
        val rest =  validationFormUpdateDelivery()
        if (rest["type"] as Boolean) {
            context?.let { progressBar.show(it, "Silakan tunggu...") }
            val deliItems = deliveryItems?.map {
                return@map mutableMapOf(
                    "delivery_items_id" to it.id.toString(),
                    "sent_quantity" to it.quantity_sent.toString()
                )
            }

            val body = mutableMapOf(
                "date" to (et_add_delivery_date?.tag?.toString() ?: ""),
                "customer" to (et_add_delivery_customer_name?.text?.toString() ?: ""),
                "address" to (et_add_delivery_customer_address?.text?.toString() ?: ""),
                "status" to (rg_add_delivery_status?.tag?.toString() ?: ""),
                "delivered_by" to (et_add_delivery_delivered_by?.text?.toString() ?: ""),
                "received_by" to (et_add_delivery_received_by?.text?.toString() ?: ""),
                "note" to (et_add_delivery_note?.text?.toString() ?: ""),
                "products" to deliItems
            )
            vmDelivery.putEditDeliv(body, idDelivery) {
                progressBar.dialog.dismiss()
                this.dismiss()
            }
        }else{
            alert.alert("sorry", context)
        }
    }

    private fun validationFormUpdateDelivery(): Map<String, Any?> {
        var message = ""
        var cek = true
        if ((deliveryItems?.size ?: listOf<DeliveryItem>().size) < 1){
            message += "- Product Item Tidak Boleh Kosong\n"
            cek = false
        }
        return mapOf("message" to message, "type" to cek)
    }

    private fun setUpUI(deliveryItems: List<DeliveryItem>?) {
        adapter = ListItemDeliveryAdapter()
        adapter.updateDate(deliveryItems)
        adapter.listenerProduct = this
        rv_list_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_data?.setHasFixedSize(false)
        rv_list_data?.adapter = adapter
    }

    override fun onClickPlus(qty: Double, position: Int) {
        val deliveryItem = deliveryItems?.get(position)

        val unsentQty = deliveryItem?.quantity_ordered?.minus(deliveryItem.all_sent_qty ?: 0.0) ?: 0.0
        val maxQty = unsentQty.plus(deliveryItemsTemp?.get(position)?.get("sent_quantity") ?: 0.0 )
        val newQty = deliveryItem?.quantity_sent?.plus(1) ?: 0.0
        if (newQty > maxQty){
            alert.alert(getString(R.string.txt_alert_out_of_qty), context)
        }else{
            deliveryItem?.quantity_sent = newQty
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClickMinus(qty: Double, position: Int) {
        val deliveryItem = deliveryItems?.get(position)
        val minQty = 1.0
        val newQty = deliveryItem?.quantity_sent?.minus(1) ?: 0.0
        if (newQty < minQty){
            alert.alert(getString(R.string.txt_alert_must_more_than_one), context)
        }else{
            deliveryItem?.quantity_sent = newQty
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClickDelete(position: Int) {

    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as DeliveryFragment).refreshDataSale()
    }


}