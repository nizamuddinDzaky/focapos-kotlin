package id.sisi.postoko.utils.helper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.sisi.postoko.utils.extensions.logE
import java.io.IOException

inline fun <reified T> Gson.fromJson(context: Context, fileName: String): T {
    var json = ""
    try {
        json = JsonHelper(context).parsingFileToString(fileName).toString()
    } catch (e: Exception) {
        logE("error loadJson ${e.message} $e")
    }
    return fromJson<T>(json, object: TypeToken<T>() {}.type)
}

inline fun <reified T> String.json2obj(): T {
    return Gson().fromJson<T>(this, object: TypeToken<T>() {}.type)
}

class JsonHelper(private val context: Context) {

    fun parsingFileToString(fileName: String): String? {
        return try {
            val `is` = context.assets.open(fileName)
            val buffer = ByteArray(`is`.available())
            `is`.read(buffer)
            `is`.close()
            String(buffer)

        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}