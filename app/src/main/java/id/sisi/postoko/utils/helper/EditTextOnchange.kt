package id.sisi.postoko.utils.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class EditTextOnchange(private var editText: EditText): TextWatcher{

    override fun afterTextChanged(s: Editable) {
        editText.removeTextChangedListener(this)
        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence,start: Int,count: Int,after: Int) {}

    override fun onTextChanged(s: CharSequence,start: Int,before: Int,count: Int) {}
}