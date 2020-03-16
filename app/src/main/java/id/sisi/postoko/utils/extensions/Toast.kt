package id.sisi.postoko.utils.extensions

import android.view.Gravity
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG

fun Toast.showErrorL(msg: String) {
    setText(msg)
    setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
    duration = LENGTH_LONG
    show()
}