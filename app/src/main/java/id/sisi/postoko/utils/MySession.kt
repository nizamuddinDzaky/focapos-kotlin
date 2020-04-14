package id.sisi.postoko.utils

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.MessageEvent
import id.sisi.postoko.view.MainActivity

object MySession {
    fun logOut(activity: FragmentActivity?) {
        MyApp.prefs.deleteLogout()
        activity?.apply {
            val page = Intent(this, MainActivity::class.java)
            page.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(page)
            finish()
        }
    }

    fun eventLogOut(activity: FragmentActivity?, messageEvent: MessageEvent) {
        if (messageEvent.isTokenExpired) {
            logOut(activity)
        }
    }
}