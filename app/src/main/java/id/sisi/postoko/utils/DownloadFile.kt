package id.sisi.postoko.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import id.sisi.postoko.utils.extensions.logE

class DownloadFile {
    fun downloadFile(url: String?, context: Context?) {
        try{
            val request = DownloadManager.Request(Uri.parse("$URL_FILE_ATTACHMENT$url"))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle("Download")
            request.setDescription("File is Downloading")

            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url)
            val manager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        }catch (e: Exception){
            logE("error: $e")
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
        }
    }
}