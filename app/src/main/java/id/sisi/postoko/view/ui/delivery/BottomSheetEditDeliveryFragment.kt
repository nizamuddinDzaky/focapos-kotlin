package id.sisi.postoko.view.ui.delivery

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemDeliveryAdapter
import id.sisi.postoko.model.DeliveryItem
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_delivery.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BottomSheetEditDeliveryFragment : BottomSheetDialogFragment(), ListItemDeliveryAdapter.OnClickListenerInterface {

    private lateinit var adapter: ListItemDeliveryAdapter<DeliveryItem>
    private lateinit var vmDelivery: DeliveryDetailViewModel
    private var deliveryItems: List<DeliveryItem>? = arrayListOf()
    private val myDialog = MyDialog()
    private var idDelivery = ""
    var listener: () -> Unit = {}
    private var requestBody: RequestBody? = null
    private var requestPart: MultipartBody.Part? = null
    private var strAttachment: String? = null
    private var note: String? = null

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
        var idRadioGroupStatusDelivery = 0
        vmDelivery.getDetailDelivery().observe(viewLifecycleOwner, Observer {delivery->
            delivery?.let { it ->
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDate = sdf.parse(it.date)
                val strCurrentDate = sdf.format(currentDate)
                et_add_delivery_date.setText(strCurrentDate.toDisplayDate())
                et_add_delivery_date.hint = strCurrentDate.toDisplayDate()
                et_add_delivery_date.tag = strCurrentDate
                et_add_delivery_reference_no.setText(it.do_reference_no)
                et_add_delivery_sales_ref.setText(it.sale_reference_no)
                et_add_delivery_delivered_by.setText(it.delivered_by)
                et_add_delivery_received_by.setText(it.received_by)
                et_add_delivery_customer_name.setText(it.customer)
                et_add_delivery_customer_name.setText(it.customer)
                et_add_delivery_customer_address.setText(it.address)

                setUpNote(it.note)

                deliveryItems = it.deliveryItems

                deliveryItems?.forEach {delivery ->
                    delivery.tempDelivQty = delivery.quantity_sent
                }

                if (!TextUtils.isEmpty(it.attachment)){
                    iv_delete.visible()
                    tv_prefix_image_name.gone()
                    tv_image_name.text = it.attachment
                    strAttachment = it.attachment
                }else{
                    removeFile()
                }

                setUpUI(it.deliveryItems)

                for (i in 0 until (rg_add_delivery_status?.childCount ?: 0)) {
                    (rg_add_delivery_status?.get(i) as? RadioButton)?.tag =
                        DeliveryStatus.values()[i].name.toLowerCase(
                            Locale.getDefault()
                        )
                    if (DeliveryStatus.values()[i].name.toLowerCase(Locale.getDefault()) == it.status) {
                        idRadioGroupStatusDelivery = i
                    }
                }

                if (it.status.equals(DeliveryStatus.DELIVERING.toString(), true)){
                    rbtn_packing.gone()
                }

                rg_add_delivery_status?.check(rg_add_delivery_status?.get(idRadioGroupStatusDelivery)?.id ?: 0)
            }
        })

        rg_add_delivery_status?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_add_delivery_status?.tag = radioButton.tag
        }

        vmDelivery.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {
                context?.let { c ->
                    progressBar.show(c, getString(R.string.txt_please_wait))
                }
            } else {
                progressBar.dialog.dismiss()
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
                        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        val strTime = time.format(Date())
                        val parseDate =
                            inputDateFormat.parse("$year-${monthOfYear + 1}-$dayOfMonth $strTime")
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

        val mandatory = listOf<EditText>(
            et_add_delivery_date,
            et_add_delivery_sales_ref,
            et_add_delivery_customer_name,
            et_add_delivery_customer_address
        )

        btn_confirmation_add_delivery.setOnClickListener {
            if (!mandatory.validation()) {
                return@setOnClickListener
            }
            actionUpdateDelivery()
        }

        btn_close.setOnClickListener {
            this.dismiss()
        }

        tv_add_note.setOnClickListener {
            myDialog.note(getString(R.string.txt_delivery_note), note ?: "", context)
            myDialog.listenerPositifNote = {
                setUpNote(it)
            }
        }

        tv_edit_note.setOnClickListener {
            myDialog.note(getString(R.string.txt_delivery_note), note ?: "", context)
            myDialog.listenerPositifNote = {
                setUpNote(it)
            }
        }

        iv_delete.setOnClickListener {
            removeFile()
        }

        layout_upload_file.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (context?.let { context ->
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    }
                    == PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, RC_UPLOAD_IMAGE)
                }else{
                    openMedia()
                }
            }else{
                openMedia()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            RC_UPLOAD_IMAGE ->{
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    openMedia()
                }
                else{
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == RC_UPLOAD_IMAGE){
                if(data != null){
                    try {
                        val selectedUri = data.data
                        val filePath= FilePath()
                        val selectedPath = context?.let { context ->
                            selectedUri?.let { uri ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    filePath.getPath(context, uri)
                                } else {
                                    TODO("VERSION.SDK_INT < KITKAT")
                                }
                            }
                        }
                        val file = File(selectedPath)
                        tv_image_name.text = file.name
                        tv_prefix_image_name.gone()
                        iv_delete.visible()
                        requestBody = RequestBody.create(MediaType.parse(selectedUri?.let { activity?.contentResolver?.getType(it) }), file)
                        requestPart = MultipartBody.Part.createFormData("file", file.name, requestBody)
                    }catch (e: Exception){
                        Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    removeFile()
                }
            }
        }
    }

    override fun onClickPlus(qty: Double, position: Int) {
        val deliveryItem = deliveryItems?.get(position)

        val unsentQty = deliveryItem?.quantity_ordered?.minus(deliveryItem.all_sent_qty ?: 0.0) ?: 0.0
        val maxQty = unsentQty.plus(deliveryItem?.tempDelivQty ?: 0.0 )
        val newQty = deliveryItem?.quantity_sent?.plus(1) ?: 0.0
        if (newQty > maxQty){
            myDialog.alert(getString(R.string.txt_alert_out_of_qty), context)
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
            myDialog.alert(getString(R.string.txt_alert_must_more_than_one), context)
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

    override fun onChange(position: Int) {
        val deliveryItem = deliveryItems?.get(position)
        myDialog.qty(
            deliveryItem?.product_name ?: "",
            getString(R.string.txt_delivery_total_qty),
            deliveryItem?.quantity_sent?.toInt() ?: 0,
            context,
            deliveryItem?.product_unit_code ?: "")

        myDialog.listenerPositifNote={ qty ->
            if (TextUtils.isEmpty(qty)){
                myDialog.alert(getString(R.string.txt_alert_must_more_than_one), context)
            }else{
                val newQty = qty.toDouble()
                val unsentQty = deliveryItem?.quantity_ordered?.minus(deliveryItem.all_sent_qty ?: 0.0) ?: 0.0
                val maxQty = unsentQty.plus(deliveryItem?.tempDelivQty ?: 0.0 )
                val minQty = 1.0
                when {
                    newQty > maxQty -> {
                        myDialog.alert(getString(R.string.txt_alert_out_of_qty), context)
                    }
                    newQty < minQty -> {
                        myDialog.alert(getString(R.string.txt_alert_must_more_than_one), context)
                    }
                    else -> {
                        deliveryItem?.quantity_sent = newQty
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun setUpNote(note: String) {
        if (TextUtils.isEmpty(note)){
            tv_add_note.visible()
            tv_edit_note.gone()
            tv_note.text = getString(R.string.txt_not_set_note)
        }else{
            tv_add_note.gone()
            tv_edit_note.visible()
            tv_note.text = note
            this.note = note
        }
    }

    private fun actionUpdateDelivery() {
        val rest =  validationFormUpdateDelivery()
        if (rest[KEY_VALIDATION_REST] as Boolean) {

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
                "note" to (note ?: ""),
                "products" to deliItems,
                "attachment" to (strAttachment ?: "")
            )
            vmDelivery.putEditDeliv(body, idDelivery, requestPart) {
                listener()
                this.dismiss()
            }
        }else{
            myDialog.alert(rest[KEY_MESSAGE] as String, context)
        }
    }

    private fun removeFile() {
        requestBody = null
        requestPart = null
        strAttachment = null
        iv_delete.gone()
        tv_prefix_image_name.visible()
        tv_image_name.text = getString(R.string.txt_upload_file)
    }

    private fun validationFormUpdateDelivery(): Map<String, Any?> {
        var message = ""
        var cek = true
        if ((deliveryItems?.size ?: listOf<DeliveryItem>().size) < 1){
            message += "- ${getString(R.string.txt_alert_item_sale)}\n"
            cek = false
        }
        return mapOf(KEY_MESSAGE to message, KEY_VALIDATION_REST to cek)
    }

    private fun setUpUI(deliveryItems: List<DeliveryItem>?) {
        adapter = ListItemDeliveryAdapter()
        adapter.updateDate(deliveryItems)
        adapter.listenerProduct = this
        rv_list_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_data?.setHasFixedSize(false)
        rv_list_data?.adapter = adapter
    }

    private fun openMedia() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "*/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i,"Choose File to Upload.."),RC_UPLOAD_IMAGE)
    }
}