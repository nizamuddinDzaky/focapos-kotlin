package id.sisi.postoko.utils.extensions

import android.util.Log
import id.sisi.postoko.BuildConfig

fun logE(message: String, tag: String = "TesPos") {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message)
    }
}