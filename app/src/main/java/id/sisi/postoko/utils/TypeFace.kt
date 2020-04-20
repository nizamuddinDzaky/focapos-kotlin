package id.sisi.postoko.utils

import android.content.res.AssetManager
import android.graphics.Typeface
import android.widget.EditText
import android.widget.TextView

class TypeFace {
    fun typeFace(strFile: String, editText: TextView, asset: AssetManager){
        val typeface = Typeface.createFromAsset(asset, strFile)
        editText.typeface = typeface
    }
}