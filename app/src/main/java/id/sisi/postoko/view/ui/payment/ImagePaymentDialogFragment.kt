package id.sisi.postoko.view.ui.payment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
//import androidx.test.core.app.ApplicationProvider.getApplicationContext
import id.sisi.postoko.R
import id.sisi.postoko.utils.LoadImageFromUrl
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.dialog_image.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class ImagePaymentDialogFragment: DialogFragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        return inflater.inflate(R.layout.dialog_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadImage = LoadImageFromUrl(iv_image)
        loadImage.execute("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png")
    }
}