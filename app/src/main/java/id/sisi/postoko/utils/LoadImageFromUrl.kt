package id.sisi.postoko.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.logE
import java.net.URL

class LoadImageFromUrl(private var imageView: ImageView, var context: Context, var defaultIcon: Int, var isCircle: Boolean = false) :
    AsyncTask<String?, Void?, Bitmap?>() {

    override fun doInBackground(vararg params: String?): Bitmap? {
        val imageURL = params[0]
        var bimage: Bitmap? = null
        try {
            val `in` = URL(imageURL).openStream()
            logE("$`in`")
            bimage = BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            logE("Error Message", e.message.toString())
            e.printStackTrace()
        }
        return bimage
    }

    override fun onPostExecute(result: Bitmap?) {

        val myOptions = RequestOptions().override(100,100)
        if (isCircle){
            try {
                Glide.with(context)
                    .load(result)
                    .placeholder(defaultIcon)
                    .circleCrop()
                    .apply(myOptions)
                    .into(imageView)
            }catch (e: Exception) {

            }
        }else{
            try {
                Glide.with(context)
                    .load(result)
                    .placeholder(defaultIcon)
                    .apply(myOptions)
                    .into(imageView)
            }catch (e: Exception) {

            }

        }

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