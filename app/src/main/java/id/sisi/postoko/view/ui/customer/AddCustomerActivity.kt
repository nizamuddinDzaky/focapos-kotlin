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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.sisi.postoko.R
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.FilePath
import id.sisi.postoko.utils.RC_IMAGE_CAPTURE_CODE
import id.sisi.postoko.utils.RC_PERMISSION_CAMERA
import id.sisi.postoko.utils.RC_UPLOAD_IMAGE
import id.sisi.postoko.utils.extensions.logE
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

    private var imageUri: Uri? = null
    private var requestBody: RequestBody? = null
    private var requestPart: MultipartBody.Part? = null
    lateinit var mViewModelWarehouse: WarehouseViewModel
    var listWarehouse: List<Warehouse>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        setSupportActionBar(toolbar_add_customer)
        supportActionBar?.title = null

        mViewModelWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)


        main_view_pager?.let {
            it.adapter = AddCustomerPagerAdapter(supportFragmentManager)
            tabs_main_pagers?.setupWithViewPager(it)
        }

        btn_action_submit.setOnClickListener {
            /*listWarehouse?.forEach {wh ->
                logE("warehouse : ${wh.isDefault}")
            }*/
            logE("${findViewById<EditText>(R.id.et_name_add_customer).text}")
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
}
