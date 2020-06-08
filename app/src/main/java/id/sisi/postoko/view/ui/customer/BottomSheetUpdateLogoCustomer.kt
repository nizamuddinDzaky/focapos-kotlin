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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_upload_logo.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BottomSheetUpdateLogoCustomer: BottomSheetDialogFragment() {

    private val progressBar = CustomProgressBar()
    private var imageUri: Uri? = null
    private var requestBody: RequestBody? = null
    private var requestPart: MultipartBody.Part? = null
    private var idCustomer: String? = null
    private lateinit var mViewModel: CustomerViewModel
    var listener : () -> Unit ={}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)
        return inflater.inflate(R.layout.fragment_bottom_sheet_upload_logo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_title_bottom_sheet.text = getString(R.string.txt_logo)
        mViewModel.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {
                context?.let { c ->
                    progressBar.show(c, getString(R.string.txt_please_wait))
                }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        arguments?.getString(KEY_ID_CUSTOMER).let {
            idCustomer = it
        }

        btn_action_submit.setOnClickListener {
            requestPart?.let { req ->
                mViewModel.postUploadLogoCustomer(req, idCustomer ?: "0"){
                    listener()
                }
            }
        }

        btn_gallery.setOnClickListener {
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
                    val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(i, RC_UPLOAD_IMAGE)
                }
            }
        }

        btn_camera.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (context?.let { context ->
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        )
                    }
                    == PackageManager.PERMISSION_DENIED ||
                    context?.let { context ->
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

        btn_simpan.setOnClickListener {
            btn_action_submit.performClick()
        }

        btn_close.setOnClickListener {
            this.dismiss()
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
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
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
                iv_avatar.setImageURI(imageUri)
            }else{
                iv_avatar.setImageURI(imageUri)
            }
            try {
                val filePath= FilePath()
                val selectedPath = context?.let { context ->
                    imageUri?.let { uri ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            filePath.getPath(context, uri)
                        } else {
                            TODO("VERSION.SDK_INT < KITKAT")
                        }
                    }
                }
                val file = File(selectedPath)
                requestBody = RequestBody.create(MediaType.parse(imageUri?.let { activity?.contentResolver?.getType(it) }), file)
                requestPart = MultipartBody.Part.createFormData("logo", file.name, requestBody)
            }catch (e: Exception){
                Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, RC_PERMISSION_CAMERA)
    }

    companion object {
        var listener : () -> Unit ={}
        fun show(
            fragmentManager: FragmentManager,
            idCustomers: String?
        ) {
            val bottomSheetFragment = BottomSheetUpdateLogoCustomer()
            val bundle = Bundle()
            bundle.putString(KEY_ID_CUSTOMER, idCustomers)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
            bottomSheetFragment.listener = {
                listener()
            }
        }
    }
}