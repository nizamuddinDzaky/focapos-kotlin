package id.sisi.postoko.utils.extensions

import android.widget.EditText

fun List<EditText>.validation(): Boolean {
    var result = true
    this.forEach {
        result = if (result) (it.text?.toStr() != null) else false
        if (it.text?.toStr() == null) {
            it.error = "Periksa kembali"
        }
    }
    return result
}