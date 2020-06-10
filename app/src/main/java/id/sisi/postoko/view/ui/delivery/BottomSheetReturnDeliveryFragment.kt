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
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.fragment_bottom_sheet_return_delivery.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BottomSheetReturnDeliveryFragment: BottomSheetDialogFragment(), ListItemDeliveryAdapter.OnClickListenerInterface {

    private var myDialog = MyDialog()
    private lateinit var adapter: ListItemDeliveryAdapter<DeliveryItem>
    private lateinit var vmDelivery: DeliveryDetailViewModel
    private var idDelivery = ""
    private var saleId = "0"
    private val progressBar = CustomProgressBar()
    private var deliveryItems: List<DeliveryItem>? = arrayListOf()
    private var requestBody: RequestBody? = null
    private var requestPart: MultipartBody.Part? = null
    private var strAttachment: String? = null
    private var note: String? = null

    var listener: () -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_return_delivery, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vmDelivery = ViewModelProvider(this).get(DeliveryDetailViewModel::class.java)

        vmDelivery.getDetailDelivery().observe(viewLifecycleOwner, Observer {delivery->
            delivery?.let { it ->

                et_reference_no.setText(it.do_reference_no)
                et_sales_ref.setText(it.sale_reference_no)
                et_customer_address.setText(it.address)
                et_customer_name.setText(it.customer)
                setUpNote(it.note)

                deliveryItems = it.deliveryItems
                saleId = it.sale_id
                deliveryItems?.forEach {delivery ->
                    delivery.tempDelivQty = delivery.quantity_sent
                }
                setUpUI(it.deliveryItems)
            }
        })
        vmDelivery.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        vmDelivery.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {
                context?.let { c ->
                    progressBar.show(c, getString(R.string.txt_please_wait))
                }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        arguments?.getString(KEY_ID_DELIVERY)?.let {
            vmDelivery.requestDetailDelivery(it.toInt())
            idDelivery = it
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        et_date?.setText(currentDate.toDisplayDate())
        et_date?.hint = currentDate.toDisplayDate()
        et_date?.tag = currentDate
        et_date.setOnClickListener {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val date = if (et_date.tag == null){
                inputDateFormat.format(Date())
            }else{
                et_date.tag.toString() + " 00:00:00"
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
                            et_date.setText(selectedDate.toDisplayDate())
                            et_date?.tag = selectedDate

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
            et_date,
            et_sales_ref,
            et_customer_name,
            et_reference_no
        )

        btn_confirmation.setOnClickListener {
            if (!mandatory.validation()) {
                return@setOnClickListener
            }
            actionReturnDelivery()
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as DeliveryFragment).refreshDataSale()
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

    private fun actionReturnDelivery() {
        val rest =  validationFormReturnDelivery()
        if (rest[KEY_VALIDATION_REST] as Boolean) {

            val delItems = deliveryItems?.map {
                return@map mutableMapOf(
                    "delivery_items_id" to it.id.toString(),
                    "return_quantity" to it.quantity_sent.toString(),
                    "delivered_quantity" to it.tempDelivQty.toString()
                )
            }

            val body = mutableMapOf(
                "date" to (et_date?.tag?.toString() ?: ""),
                "customer" to (et_customer_name?.text?.toString() ?: ""),
                "address" to (et_customer_address?.text?.toString() ?: ""),
                "sale_id" to (saleId),
                "sale_reference_no" to (et_sales_ref?.text?.toString() ?: ""),
                "do_reference_no" to (et_reference_no?.text?.toString() ?: ""),
                "note" to (note ?: ""),
                "status" to "returned",
                "products" to delItems
            )
            vmDelivery.postReturnDeliv(body, idDelivery, requestPart) {
                listener()
                this.dismiss()
            }
        }else{
            myDialog.alert(rest[KEY_MESSAGE] as String, context)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    private fun validationFormReturnDelivery(): Map<String, Any?> {
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

    private fun removeFile() {
        requestBody = null
        requestPart = null
        strAttachment = null
        iv_delete.gone()
        tv_prefix_image_name.visible()
        tv_image_name.text = getString(R.string.txt_upload_file)
    }

    private fun openMedia() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "*/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i,"Choose File to Upload.."),RC_UPLOAD_IMAGE)
    }
}