package id.sisi.postoko.utils.extensions

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import id.sisi.postoko.R

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

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.checkVisibility(isShow: Boolean) {
    this.visibility = if (isShow) View.VISIBLE else View.GONE
}

fun View.strikeText() {
    if (this is TextView && this.text.isNullOrBlank()) {
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}

fun View.goneIfEmptyOrNull() {
    var check = 0
    if (this is TextView && this.text.isNullOrBlank()) {
        check = check.plus(1)
    }
    this.visibility = if (check == 0) View.VISIBLE else View.GONE
}

fun Context.showToast(msg: String? = "") {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showToastAccessDenied() {
    Toast.makeText(this, getString(R.string.txt_warning_access_denied), Toast.LENGTH_SHORT).show()
}