package id.sisi.postoko.utils.extensions

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import android.widget.Toast.*

fun Toast.showErrorL(msg: String) {
    setText(msg)
    setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
    duration = LENGTH_LONG
    show()
}

object MyToast {
    fun make(context: Context?) = makeText(context, "", LENGTH_SHORT)
}