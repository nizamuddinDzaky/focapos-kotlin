package id.sisi.postoko.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.activity_upload_file.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UploadFileActivity : AppCompatActivity() {

    private lateinit var viewModel: UploadFileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_file)

        viewModel = ViewModelProvider(this).get(UploadFileViewModel::class.java)

        button.setOnClickListener {
            //opening file chooser
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 100)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedImage = data?.getData()

        //creating a file
        val file = File(selectedImage?.let { getRealPathFromURI(it) })
        //calling the upload file method after choosing the file
        //creating request body for file
        val requestFile =
            RequestBody.create(MediaType.parse(selectedImage?.let { contentResolver.getType(it) }), file)
        val descBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "hallo")
        val body =  mutableMapOf(
            "desc" to descBody,
            "image\"; filename=\"myfile.jpg\" " to requestFile
        )

        viewModel.cobaUpload(body)
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(this, contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }
}
