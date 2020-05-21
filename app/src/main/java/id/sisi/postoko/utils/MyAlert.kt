package id.sisi.postoko.utils

import android.app.Dialog
import android.content.Context
import android.text.Html
import android.view.Window
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import kotlinx.android.synthetic.main.dialog_alert_confirmation.*

class MyAlert() {

    var listenerPositif: () -> Unit = {}
    var listenerNegatif: () -> Unit = {}

    fun alert(message: String, activity: Context?){
        val dialog = activity?.let { Dialog(it, R.style.MyCustomDialogFullScreen) }
        val strOke = "Oke"
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_alert_confirmation)
        dialog?.tv_message?.text = Html.fromHtml(message)
        dialog?.tv_cancel?.gone()
        dialog?.tv_sure?.text = strOke
        dialog?.tv_sure?.setOnClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    fun confirmation( message: String, activity: Context?){
        val dialog = activity?.let { Dialog(it, R.style.MyCustomDialogFullScreen) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_alert_confirmation)
        dialog?.tv_message?.text = Html.fromHtml(message)
        dialog?.tv_cancel?.setOnClickListener {
            listenerNegatif()
            dialog.dismiss()
        }

        dialog?.tv_sure?.setOnClickListener {
            listenerPositif()
            dialog.dismiss()
        }
        dialog?.show()
    }
}