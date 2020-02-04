package id.sisi.postoko

import android.app.Application
import id.sisi.postoko.utils.helper.Prefs

class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp

        val prefs: Prefs by lazy {
            Prefs(instance)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}