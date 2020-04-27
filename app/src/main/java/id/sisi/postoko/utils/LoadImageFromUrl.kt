package id.sisi.postoko.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import id.sisi.postoko.utils.extensions.logE
import java.net.URL

class LoadImageFromUrl(var imageView: ImageView) :
    AsyncTask<String?, Void?, Bitmap?>() {

    override fun doInBackground(vararg params: String?): Bitmap? {
        val imageURL = params[0]
        var bimage: Bitmap? = null
        try {
            val `in` = URL(imageURL).openStream()
            bimage = BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            logE("Error Message", e.message.toString())
            e.printStackTrace()
        }
        return bimage
    }

    override fun onPostExecute(result: Bitmap?) {
        imageView.setImageBitmap(result)
    }


    /*init {
        this.imageView = imageView
        Toast.makeText(
            ApplicationProvider.getApplicationContext(),
            "Please wait, it may take a few minute...",
            Toast.LENGTH_SHORT
        ).show()
    }*/
}