package id.sisi.postoko

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Html
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import id.sisi.postoko.utils.extensions.logE
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MyNotificationManager(var mCtx : Context) {
    private val NOTIFICATION = 234
    private val NOTIFICATION1 = 235

    //the method will show a big notification with an image
//parameters are title for message title, message for message text, url of the big image and an intent that will open
//when you will tap on the notification
    fun showBigNotification(
        title: String?,
        message: String?,
        url: String,
        intent: Intent?
    ) {
        val resultPendingIntent = PendingIntent.getActivity(
            mCtx,
            NOTIFICATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val bigPictureStyle: NotificationCompat.BigPictureStyle =
            NotificationCompat.BigPictureStyle()
        bigPictureStyle.setBigContentTitle(title)
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
        bigPictureStyle.bigPicture(getBitmapFromURL(url))
        val mBuilder = NotificationCompat.Builder(mCtx)
        val notification: Notification
        notification = mBuilder.setSmallIcon(R.drawable.plus).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(title)
            .setStyle(bigPictureStyle)
            .setSmallIcon(R.drawable.plus)
            .setLargeIcon(BitmapFactory.decodeResource(mCtx.resources, R.drawable.plus))
            .setContentText(message)
            .build()
        /*notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL*/
        val notificationManager =
            mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION, notification)
    }

    //the method will show a small notification
//parameters are title for message title, message for message text and an intent that will open
//when you will tap on the notification
    fun showSmallNotification(
        title: String?,
        message: String?,
        intent: Intent?
    ) {

        val resultPendingIntent = PendingIntent.getActivity(
            mCtx,
            NOTIFICATION1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder = NotificationCompat.Builder(mCtx)
        val notification: Notification
        notification = mBuilder.setSmallIcon(R.drawable.plus).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.plus)
            .setLargeIcon(BitmapFactory.decodeResource(mCtx.resources, R.drawable.plus))
            .setContentText(message)
            .build()
        /*notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL*/
        val notificationManager =
            mCtx.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION1, notification)
        logE("sddsfsfdsfsfdf")
    }

    //The method will return Bitmap from an image URL
    private fun getBitmapFromURL(strURL: String): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}