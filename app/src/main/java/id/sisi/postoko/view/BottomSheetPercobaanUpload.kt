package id.sisi.postoko.view

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vincent.filepicker.Constant.RESULT_PICK_IMAGE
import com.vincent.filepicker.filter.entity.ImageFile
import id.sisi.postoko.R
import id.sisi.postoko.utils.RC_UPLOAD_IMAGE
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setupFullHeight
import kotlinx.android.synthetic.main.fragment_bottom_sheet_percobaan_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class BottomSheetPercobaanUpload: BottomSheetDialogFragment() {

    lateinit var imagename:MultipartBody.Part
    private lateinit var viewModel: UploadFileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this
        ).get(UploadFileViewModel::class.java)

        return inflater.inflate(R.layout.fragment_bottom_sheet_percobaan_upload, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }
    private var selectedUri: Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout_upload_file.setOnClickListener {
            val i = Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(i, RC_UPLOAD_IMAGE)
        }

        btn_confirmation_add_delivery.setOnClickListener {
            /*val file =*/
            /*val pickedImg = data.getParcelableArrayListExtra<ImageFile>(RESULT_PICK_IMAGE)[0]?.path*/

           /* viewModel.postUploadFile(imagename) {
                logE("berhasil")
            }*/
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        logE("waw")
        if (requestCode==RC_UPLOAD_IMAGE && resultCode == Activity.RESULT_OK){
            try {

                /*val pickedImg = data?.getParcelableArrayListExtra<ImageFile>(RESULT_PICK_IMAGE)?.get(0)
                    ?.path
                val requestBody = RequestBody.create(MediaType.parse("multipart"), File(pickedImg))
                imagename = MultipartBody.Part.createFormData("imagename",File(pickedImg)?.name,requestBody)
                selectedUri = data?.data*/
                iv_file.setImageURI(selectedUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}