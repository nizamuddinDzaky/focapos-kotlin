package id.sisi.postoko.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class NumberSeparator {
    fun onTextChangedListener(editText : EditText): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                editText.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    val longval: Long
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    longval = originalString.toLong()
                    val formatter: DecimalFormat =
                        NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)
                    editText.setText(formattedString)
                    editText.setSelection(editText.text.length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                editText.addTextChangedListener(this)
            }
        }
    }
}