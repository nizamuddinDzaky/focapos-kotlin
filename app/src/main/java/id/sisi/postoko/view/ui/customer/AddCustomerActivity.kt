package id.sisi.postoko.view.ui.customer

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tiper.MaterialSpinner
import id.sisi.postoko.R
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.FilePath
import id.sisi.postoko.utils.RC_IMAGE_CAPTURE_CODE
import id.sisi.postoko.utils.RC_PERMISSION_CAMERA
import id.sisi.postoko.utils.RC_UPLOAD_IMAGE
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import id.sisi.postoko.view.ui.delivery.DialogFragmentSelectMedia
import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.content_add_customer.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddCustomerActivity : AppCompatActivity() {
    private lateinit var viewModelCustomer: CustomerViewModel
    private lateinit var mViewModelDaerah: DaerahViewModel

    private var listCustomerGroupName = ArrayList<String>()
    private var listCustomerGroup: List<CustomerGroup> = ArrayList()
    private var listPriceGroupName = ArrayList<String>()
    private var listPriceGroup: List<PriceGroup> = ArrayList()
    private var idCustomerGroup: String? = null
    private var idPriceGroup: String? = null
    private val progressBar = CustomProgressBar()

    private var provinceList: Array<String> = arrayOf()
    private var cityList: Array<String> = arrayOf()
    private var villageList: Array<String> = arrayOf()

    private var imageUri: Uri? = null
    private var requestBody: RequestBody? = null
    private var requestPart: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        setSupportActionBar(toolbar_add_customer)
        supportActionBar?.title = null

        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCustomer.getListCustomerGroup()
        viewModelCustomer.getListCustomerGroups().observe(this, Observer {
            it?.let {
                for (x in it.indices)
                    listCustomerGroupName.add(it[x].name)
                listCustomerGroup = it
            }
        })

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

        val adapterCustomerGroup = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listCustomerGroupName)
        sp_customer_group_add_customer.adapter = adapterCustomerGroup
        sp_customer_group_add_customer.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                idCustomerGroup = listCustomerGroup[position].id
            }
            override fun onNothingSelected(parent: MaterialSpinner): Unit = Unit
        }


        viewModelCustomer.getListPriceGroup()
        viewModelCustomer.getListPriceGroups().observe(this, Observer {
            it?.let {
                logE("price goup $it")
                for (x in it.indices)
                    listPriceGroupName.add(it[x].name ?: "~")
                listPriceGroup = it
            }
        })

        val adapterPriceGroup = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listPriceGroupName)
        sp_price_group_add_customer.adapter = adapterPriceGroup
        sp_price_group_add_customer.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                idPriceGroup = listPriceGroup[position].id.toString()
            }
            override fun onNothingSelected(parent: MaterialSpinner): Unit = Unit
        }

        sp_provinsi_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getCity(provinceList[position])
            }
        }

        sp_district_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getStates(cityList[position])
            }
        }

        btn_confirmation_add_customer.setOnClickListener {
            actionAddCustomer()
        }

        for (i in 0 until (rg_status_add_customer?.childCount ?: 0)) {
            (rg_status_add_customer?.get(i) as? RadioButton)?.tag = i
        }

        rg_status_add_customer?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_add_customer?.tag = radioButton.tag
        }
        rg_status_add_customer?.check(rg_status_add_customer?.get(1)?.id ?: 0)

        mViewModelDaerah = ViewModelProvider(this).get(DaerahViewModel::class.java)

        mViewModelDaerah.getAllProvince().observe(this, Observer {
            it?.let {listDataDaerah ->
                provinceList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.province_name ?: ""
                }.toTypedArray()
            }
            setUIProvince()
        })

        mViewModelDaerah.getAllCity().observe(this, Observer {
            it?.let {listDataDaerah ->
                cityList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.kabupaten_name ?: ""
                }.toTypedArray()
            }
            setUICity()
        })

        mViewModelDaerah.getAllStates().observe(this, Observer {
            it?.let {listDataDaerah ->
                villageList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.kecamatan_name ?: ""
                }.toTypedArray()
            }
            setUIStates()
        })

        mViewModelDaerah.getProvince()

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

    private fun setUIProvince(){
        sp_provinsi_group_add_customer.adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            provinceList
        )
    }

    private fun setUICity(){
        sp_district_group_add_customer.adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            cityList
        )
    }

    private fun setUIStates(){
        sp_city_group_add_customer.adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            villageList
        )
    }

    private fun actionAddCustomer() {
        val numbersMap =  validationFormAddCustomer()

        if (numbersMap["type"] as Boolean){
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_name_add_customer?.text?.toString() ?: ""),
                "email" to (et_email_add_customer?.text?.toString() ?: ""),
                "customer_group_id" to (idCustomerGroup ?: ""),
                "price_group_id" to (idPriceGroup ?: ""),
                "company" to (et_company_name_add_customer?.text?.toString() ?: ""),
                "address" to (et_address_add_customer?.text?.toString() ?: ""),
                "vat_no" to (et_npwp_add_customer?.text?.toString() ?: ""),
                "postal_code" to (et_postal_code_add_customer?.text?.toString() ?: ""),
                "phone" to (et_phone_add_customer?.text?.toString() ?: ""),
                "cf1" to (et_cf1_add_customer?.text?.toString() ?: ""),
                "cf2" to (et_cf2_add_customer?.text?.toString() ?: ""),
                "cf3" to (et_cf3_add_customer?.text?.toString() ?: ""),
                "cf4" to (et_cf4_add_customer?.text?.toString() ?: ""),
                "cf5" to (et_cf5_add_customer?.text?.toString() ?: ""),
                "province" to (sp_provinsi_group_add_customer?.selectedItem?.toString() ?: ""),
                "city" to (sp_district_group_add_customer?.selectedItem?.toString() ?: ""),
                "state" to (sp_city_group_add_customer?.selectedItem?.toString() ?: "")

            )
            viewModelCustomer.postAddCustomer(body, requestPart){
                val returnIntent = Intent()
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }else{
            AlertDialog.Builder(this@AddCustomerActivity)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    private fun validationFormAddCustomer(): Map<String, Any?> {
        var message = ""
        var cek = true

        if (et_company_name_add_customer?.text.toString() == ""){
            message += "- Nama Perusahaan/Toko Tidak Boleh Kosong\n"
            cek = false
        }

        if (et_name_add_customer?.text.toString() == ""){
            message += "- Nama Pemilik Tidak Boleh Kosong\n"
            cek = false
        }

        if (idCustomerGroup == null){
            message += "- Customer Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (et_phone_add_customer?.text.toString() == ""){
            message += "- No Telp Tidak Boleh Kosong\n"
            cek = false
        }

        if (et_address_add_customer?.text.toString() == ""){
            message += "- Alamat Tidak Boleh Kosong\n"
            cek = false
        }

        if (sp_provinsi_group_add_customer?.selectedItem.toString() == ""){
            message += "- Provinsi Tidak Boleh Kosong\n"
            cek = false
        }

        if (sp_district_group_add_customer?.selectedItem.toString() == ""){
            message += "- Kabupaten/Kota Tidak Boleh Kosong\n"
            cek = false
        }

        if (sp_city_group_add_customer?.selectedItem.toString() == ""){
            message += "- Kecamatan Tidak Boleh Kosong\n"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }
}
