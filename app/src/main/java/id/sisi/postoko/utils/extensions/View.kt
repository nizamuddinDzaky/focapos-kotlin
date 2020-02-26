package id.sisi.postoko.utils.extensions

import android.graphics.Paint
import android.view.View
import android.widget.EditText
import android.widget.TextView

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

fun View.invisible() {
    this.visibility = View.INVISIBLE
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