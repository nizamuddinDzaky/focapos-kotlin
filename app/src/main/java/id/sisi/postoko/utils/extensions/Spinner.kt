package id.sisi.postoko.utils.extensions

import android.widget.Spinner
import id.sisi.postoko.utils.MySpinnerAdapter

fun Spinner.getSelectedValue() =
    if (selectedItemPosition == -1) null else (this.adapter as MySpinnerAdapter).objects[selectedItemPosition].value

fun Spinner.setDefault() {
    setSelection(0, true)
}

fun Spinner.setIfExist(value: String) {
    var found: Int? = null
    (this.adapter as MySpinnerAdapter).objects.forEachIndexed { index, dataSpinner ->
        if (dataSpinner.value == value) {
            found = index
        }
    }
    found?.let {
        setSelection(it, true)
    }
}
