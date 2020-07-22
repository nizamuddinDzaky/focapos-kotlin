package id.sisi.postoko.view.ui.customer

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.sisi.postoko.R
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.validation
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.delivery.DialogFragmentSelectMedia
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.content_add_customer.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddCustomerActivity : AppCompatActivity() {

    private lateinit var viewModelCustomer: CustomerViewModel
    private var imageUri: Uri? = null
    private var requestBody: RequestBody? = null
    private var requestPart: MultipartBody.Part? = null
    lateinit var mViewModelWarehouse: WarehouseViewModel
    var listWarehouse: List<Warehouse>? = arrayListOf()
    private val myDialog = MyDialog()
    private val progressBar = CustomProgressBar()
    var idCustomerGroup: String? = null
    var idPriceGroup: String? = null
    var provinsiSelected: String? = null
    var districtSelected: String? = null
    var stateSelected: String? = null

    private val pages = listOf(
        AddDataCustomerFragment(),
        AddWarehouseCustomerFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        setSupportActionBar(toolbar_add_customer)
        supportActionBar?.title = null

        mViewModelWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)

        tabs_main_pagers.setSelectedTabIndicatorColor(Color.parseColor("#3F51B5"))
        main_view_pager?.let {
            it.adapter = CustomerPagerAdapter(supportFragmentManager, pages)
            tabs_main_pagers?.setupWithViewPager(it)
        }

        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCustomer.getMessage().observe(this, Observer {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModelCustomer.getIsExecute().observe(this, Observer {
            if (it && !progressBar.isShowing()) {

                progressBar.show(this, getString(R.string.txt_please_wait))
            } else {
                progressBar.dialog.dismiss()
            }
        })

        btn_action_submit.setOnClickListener {
            logE("$provinsiSelected")

            val mandatory = listOf<EditText>(
                findViewById(R.id.et_name_add_customer),
                findViewById(R.id.et_email_add_customer),
                findViewById(R.id.et_phone_add_customer),
                findViewById(R.id.et_address_add_customer)
            )
            if (!mandatory.validation()) {
                return@setOnClickListener
            }

            actionAddCustomer()
        }

        iv_logo.setOnClickListener {
            val dialogFragment = DialogFragmentSelectMedia()

            dialogFragment.show(supportFragmentManager, DialogFragmentSelectMedia().tag)
            dialogFragment.lGallery={
                actionOpenMedia()
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
            RC_PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    openCamera()
                }
                else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            RC_UPLOAD_IMAGE ->{
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RC_UPLOAD_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == RC_UPLOAD_IMAGE){
                imageUri = data?.data
            }

            val filePath= FilePath()
            val selectedPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                imageUri?.let { filePath.getPath(this, it) }
            } else {
                TODO("VERSION.SDK_INT < KITKAT")
            }

            val myOptions = RequestOptions().override(100,100)
            Glide.with(this)
                .load(selectedPath)
                .placeholder(R.drawable.toko2)
                .circleCrop()
                .apply(myOptions)
                .into(iv_logo)

            try {
                val file = File(selectedPath)

                requestBody = RequestBody.create(MediaType.parse(imageUri?.let { contentResolver?.getType(it) }), file)
                requestPart = MultipartBody.Part.createFormData("logo", file.name, requestBody)
            }catch (e: Exception){
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            android.app.AlertDialog.Builder(this@AddCustomerActivity)
                .setTitle("Konfirmasi")
                .setMessage("Apakah yakin ?")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->

                }
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actionOpenCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                )
                == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
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
        imageUri = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, RC_IMAGE_CAPTURE_CODE)
    }

    private fun actionOpenMedia() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED){
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, RC_UPLOAD_IMAGE)
            }else{
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RC_UPLOAD_IMAGE)
            }
        }
    }

    private fun validationFormAddCustomer(): Map<String, Any?> {
        var message = ""
        var cek = true

        if (findViewById<Spinner>(R.id.sp_customer_group_add_customer)?.selectedItem.toString() == ""){
            message += "- Customer Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (provinsiSelected == "" || provinsiSelected == null){
            message += "- Provinsi Tidak Boleh Kosong\n"
            cek = false
        }

        if (districtSelected == "" || districtSelected == null){
            message += "- Kabupaten/Kota Tidak Boleh Kosong\n"
            cek = false
        }

        if (stateSelected == "" || stateSelected == null){
            message += "- Kecamatan Tidak Boleh Kosong\n"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }

    private fun actionAddCustomer() {
        val validation =  validationFormAddCustomer()
        val listIdSelected: ArrayList<String> = arrayListOf()
        var defaultWarehouse = 0
        listWarehouse?.forEach {wh ->
            if (wh.isSelected)
                listIdSelected.add(wh.id)
            if (wh.isDefault)
                defaultWarehouse = wh.id.toInt()
        }

        if (validation["type"] as Boolean){
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (findViewById<EditText>(R.id.et_name_add_customer)?.text?.toString() ?: ""),
                "email" to (findViewById<EditText>(R.id.et_email_add_customer)?.text?.toString() ?: ""),
                "customer_group_id" to (idCustomerGroup ?: "0"),
                "price_group_id" to (idPriceGroup ?: "0"),
                "company" to (findViewById<EditText>(R.id.et_company_name_add_customer)?.text?.toString() ?: ""),
                "address" to (findViewById<EditText>(R.id.et_address_add_customer)?.text?.toString() ?: ""),
                "vat_no" to (findViewById<EditText>(R.id.et_npwp_add_customer)?.text?.toString() ?: ""),
                "postal_code" to (findViewById<EditText>(R.id.et_postal_code_add_customer)?.text?.toString() ?: ""),
                "phone" to (findViewById<EditText>(R.id.et_phone_add_customer)?.text?.toString() ?: ""),
                "province" to (findViewById<Spinner>(R.id.sp_provinsi_group_add_customer)?.selectedItem?.toString() ?: ""),
                "city" to (findViewById<Spinner>(R.id.sp_district_group_add_customer)?.selectedItem?.toString() ?: ""),
                "state" to (findViewById<Spinner>(R.id.sp_city_group_add_customer)?.selectedItem?.toString() ?: ""),
                "warehouses" to listIdSelected,
                "default" to defaultWarehouse
            )

            viewModelCustomer.postAddCustomer(body, requestPart){
                val returnIntent = Intent()
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }

        }else{
            myDialog.alert(validation["message"] as String, this)
        }
    }
}
