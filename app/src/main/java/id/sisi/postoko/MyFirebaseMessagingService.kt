package id.sisi.postoko

import android.R
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import org.json.JSONException
import org.json.JSONObject


class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "MyFirebaseMsgService"
    @SuppressLint("WrongConstant")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(
            TAG,
            "Data qwqwwq: " + remoteMessage.notification.toString()
        )
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(
                TAG,
                "Data Payload: " + remoteMessage.data.toString()
            )
            /*try {*/
                val json = JSONObject(remoteMessage.data.toString())
                val notifManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    val nc: NotificationChannel = NotificationChannel("nizamuddin", "Bismillah", NotificationManager.IMPORTANCE_MAX)
                    nc.description="sadadadasdsasdsa"
                    nc.enableLights(true)
                    nc.lightColor = Color.RED
                    nc.enableVibration(true)
                    notifManager.createNotificationChannel(nc)
                }
            val notify: NotificationCompat.Builder = NotificationCompat.Builder(this,"nizamuddin")
            notify.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.btn_plus)
                    .setTicker("bismillah")
                    .setContentText("Bismillah")
                    .setContentInfo("Percobaan")
            notifManager.notify(1, notify.build())
                /*sendPushNotification(json)*/
            /*} catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }*/
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: " + token);
    }

    private fun sendPushNotification(json: JSONObject) { //optionally we can display the json into log
        Log.e(TAG, "Notification JSON $json")
        try { //getting the json data

            //parsing json data
            val title = json.getString("title")
            val message = json.getString("message")
            val imageUrl = json.getString("image")
            val mNotificationManager =
                MyNotificationManager(applicationContext)
            //creating an intent for the notification
            val intent = Intent(applicationContext, DetailSalesBookingActivity::class.java)
            //            intent.putExtras(bundle);
//            startActivity(intent);
//if there is no image
            if (imageUrl == "null") { //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent)
            } else {
                logE("123")
                mNotificationManager.showBigNotification(title, message, imageUrl, intent)
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }
}