package id.sisi.postoko.view.ui.payment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.delivery.DialogFragmentSelectMedia
import kotlinx.android.synthetic.main.fragment_bottom_sheet_form_payment.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BottomSheetAddPaymentFragment : BottomSheetDialogFragment(){
    private val progressBar = CustomProgressBar()
    lateinit var viewModel: AddPaymentViewModel
    var listener: () -> Unit = {}
    private var sales: Sales? = null
    private var mustPaid: Double = 0.0
    private var myDialog = MyDialog()
    private var paymentNote: String? = null
    private var requestBody: RequestBody? = null
    private var requestPart: MultipartBody.Part? = null
    private var selectedUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_form_payment, container, false)
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

        val idSalesBooking = arguments?.getInt(KEY_ID_SALES_BOOKING) ?: 0
        sales = arguments?.getParcelable("sale")
        mustPaid = sales?.paid?.let { sales?.grand_total?.minus(it) } ?: 0.0

        viewModel = ViewModelProvider(
            this,
            AddPaymentFactory(idSalesBooking)
        ).get(AddPaymentViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            logE("progress : $it")
            if (it) {
                context?.let {c ->
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

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        et_add_payment_date?.setText(currentDate.toDisplayDate())
        et_add_payment_date?.hint = currentDate.toDisplayDate()
        et_add_payment_date?.tag = currentDate
        et_add_payment_date.setOnClickListener {

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val date = if (et_add_payment_date.tag == null){
                inputDateFormat.format(Date())
            }else{
                et_add_payment_date.tag.toString() + " 00:00:00"
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
                        val strTime = time.format(Date())
                        val parseDate =
                            inputDateFormat.parse("$year-${monthOfYear + 1}-$dayOfMonth $strTime")
                        parseDate?.let {
                            val selectedDate = inputDateFormat.format(parseDate)
                            et_add_payment_date.setText(selectedDate.toDisplayDate())
                            et_add_payment_date?.tag = selectedDate

                        }
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }
        et_add_payment_total.addTextChangedListener(NumberSeparator(et_add_payment_total))
        et_add_payment_total.setText(mustPaid.toInt().toString())

        setUpRadioGroup()

        btn_close.setOnClickListener {
            this.dismiss()
        }
        btn_confirmation_add_payment?.setOnClickListener {
            val mandatory = listOf<EditText>(et_add_payment_total)
            if (!mandatory.validation()){
                return@setOnClickListener
            }
            actionAddPayment()
        }

        tv_add_payment_note.setOnClickListener {
            showPopUpNote(getString(R.string.txt_note), paymentNote ?: "")
        }

        tv_edit_payment_note.setOnClickListener {
            showPopUpNote(getString(R.string.txt_note), paymentNote ?: "")
        }

        setPaymentNote()

        iv_delete.setOnClickListener {
            removeFile()
        }

        layout_upload_file.setOnClickListener {
            val dialogFragment = DialogFragmentSelectMedia()

            dialogFragment.show(childFragmentManager, DialogFragmentSelectMedia().tag)
            dialogFragment.lGallery={
                permissionOpenMedia()
            }

            dialogFragment.lCamera={
                actionOpenCamera()
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){

            if(requestCode == RC_UPLOAD_IMAGE){
                selectedUri = data?.data
            }

            val cR = context?.contentResolver
            val mime = MimeTypeMap.getSingleton()
            val type = mime.getExtensionFromMimeType(selectedUri?.let { cR?.getType(it) })
            val str: ArrayList<String> = arrayListOf(
                "jpg",
                "png",
                "gif",
                "jpeg"
            )

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

            if(str.indexOf(type) != -1){
                iv_file.gone()
                iv_prev_image.visible()
                val myOptions = RequestOptions().override(100,100)
                Glide.with(this)
                    .load(selectedPath)
                    .placeholder(R.drawable.toko2)
                    .circleCrop()
                    .apply(myOptions)
                    .into(iv_prev_image)
            }else{
                iv_file.visible()
                iv_prev_image.gone()
            }
            try {

                val file = File(selectedPath)
                tv_image_name.text = file.name

                tv_prefix_image_name.gone()
                iv_delete.visible()
                requestBody = RequestBody.create(MediaType.parse(selectedUri?.let { activity?.contentResolver?.getType(it) }), file)
                requestPart = MultipartBody.Part.createFormData("file", file.name, requestBody)
            }catch (e: Exception){
                logE("error: $e")
                Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeFile() {
        requestBody = null
        requestPart = null
        iv_delete.gone()
        iv_file.visible()
        iv_prev_image.gone()
        tv_prefix_image_name.visible()
        tv_image_name.text = getString(R.string.txt_upload_file)
    }

    private fun showPopUpNote(title: String, paymentNote: String) {
        val dialog = MyDialog()
        dialog.note(title, paymentNote, context)
        dialog.listenerPositifNote = {
            this.paymentNote = it
            setPaymentNote()
        }
    }

    private fun setUpRadioGroup() {
        for (i in 0 until (rg_payment_type?.childCount ?: 0)) {
            (rg_payment_type?.get(i) as? RadioButton)?.tag =
                PaymentType.values()[i].name.toLowerCase(
                    Locale.getDefault()
                )
        }
        rg_payment_type?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_payment_type?.tag = radioButton.tag
        }
        rg_payment_type?.check(rg_payment_type?.get(0)?.id ?: 0)

        rb_gift_card.setOnClickListener {
            layout_gift_card.expand(true)
            layout_credit_card.collapse(true)
        }
        rb_cash.setOnClickListener {
            layout_credit_card.collapse(true)
            layout_gift_card.collapse(true)
        }
        rb_bank.setOnClickListener {
            layout_credit_card.collapse(true)
            layout_gift_card.collapse(true)
        }
        rb_credit_card.setOnClickListener {
            layout_gift_card.collapse(true)
            layout_credit_card.expand(true)
        }
    }

    private fun actionAddPayment() {
        val validation = validationFormAddPayment()
        if (validation[KEY_VALIDATION_REST] as Boolean) {
            val body = mutableMapOf(
                "date" to (et_add_payment_date?.tag?.toString() ?: ""),
                "amount_paid" to (et_add_payment_total?.tag?.toString() ?: "0"),
                "note" to (paymentNote ?: ""),
                "payment_method" to (rg_payment_type.tag?.toString() ?: ""),
                "created_device" to KEY_DEVICE_TYPE
            )
            viewModel.postAddPayment(body, requestPart) {
                listener()
                this.dismiss()
            }
        }else{
            myDialog.alert(validation[KEY_MESSAGE] as String, context)
        }
    }

    private fun validationFormAddPayment(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (et_add_payment_total.tag.toString().toDouble() > mustPaid){
            message += "${getString(R.string.txt_overpayment)} ${mustPaid.toCurrencyID()} \n"
            cek = false
        }
        return mapOf(KEY_MESSAGE to message, KEY_VALIDATION_REST to cek)
    }

    private fun setPaymentNote() {
        if (TextUtils.isEmpty(paymentNote)){
            tv_edit_payment_note.gone()
            tv_add_payment_note.visible()
            tv_payment_note.text = getString(R.string.txt_not_set_note)
        }else{
            tv_payment_note.text = paymentNote
            tv_add_payment_note.gone()
            tv_edit_payment_note.visible()
        }
    }

    private fun openMedia() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "*/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i,"Choose File to Upload.."),RC_UPLOAD_IMAGE)
    }

    private fun permissionOpenMedia() {
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

    private fun actionOpenCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (context?.let {context ->
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    )
                }
                == PackageManager.PERMISSION_DENIED ||
                context?.let {context->
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
                == PackageManager.PERMISSION_DENIED){

                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, RC_PERMISSION_CAMERA)
            }
            else{
                openCamera()
            }
        }
        else{
            openCamera()
        }
    }

    private fun openCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        selectedUri = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedUri)
        startActivityForResult(cameraIntent, RC_IMAGE_CAPTURE_CODE)
    }
}