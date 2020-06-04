package id.sisi.postoko.utils.extensions

import android.text.Editable
import android.util.Log
import android.widget.Toast
import id.sisi.postoko.BuildConfig
import java.lang.Exception

fun logE(message: String, tag: String = "TesPos") {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message)
    }
}

fun tryMe(runMe: () -> Unit) {
    try {
        runMe()
    } catch (e: Exception) {
        logE(e.message ?: "undefine error")
    }
}

fun Editable.toStr(): String? {
    if (!this.isBlank() && this.isNotEmpty()) {
        return this.toString()
    }
    return null
}