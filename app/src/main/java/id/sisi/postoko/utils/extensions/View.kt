package id.sisi.postoko.utils.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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

fun BottomSheetDialog.setupFullHeight(activity: Activity){
    val bottomSheet =
        this.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
    val layoutParams = bottomSheet.layoutParams
    val windowHeight = getWindowHeight(activity)
    if (layoutParams != null) {
        layoutParams.height = windowHeight
    }
    bottomSheet.layoutParams = layoutParams
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
}

private fun getWindowHeight(activity: Activity): Int { // Calculate window height for fullscreen use
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay
        .getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun String.copyText(activity: FragmentActivity?){
    val myClipboard: ClipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", this)
    myClipboard.setPrimaryClip(clip)
    Toast.makeText(activity, "Copied $this", Toast.LENGTH_SHORT).show()
}

fun EditText.onChange(listener: () ->Unit): (() -> Unit)? {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            return listener()
        }
        override fun beforeTextChanged(s: CharSequence,start: Int,count: Int,after: Int) {}

        override fun onTextChanged(s: CharSequence,start: Int,before: Int,count: Int) {}
    })
    return null
}