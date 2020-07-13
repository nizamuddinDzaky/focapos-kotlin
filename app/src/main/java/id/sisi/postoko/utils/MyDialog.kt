package id.sisi.postoko.utils

import android.app.Dialog
import android.content.Context
import android.text.Html
import android.view.Window
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import kotlinx.android.synthetic.main.dialog_alert_confirmation.*
import kotlinx.android.synthetic.main.dialog_alert_confirmation.tv_cancel
import kotlinx.android.synthetic.main.dialog_alert_confirmation.tv_sure
import kotlinx.android.synthetic.main.dialog_fragment_free_text_add_sale.*
import kotlinx.android.synthetic.main.dialog_fragment_free_text_add_sale.tv_title_free_text
import kotlinx.android.synthetic.main.dialog_fragment_free_text_qty.*

class MyDialog {

    var listenerPositif: () -> Unit = {}
    var listenerPositifNote: (String) -> Unit = {}
    var listenerNegatif: () -> Unit = {}

    fun alert(message: String, activity: Context?){
        val dialog = activity?.let { Dialog(it, R.style.MyCustomDialogFullScreen) }
        val strOke = "Oke"
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_alert_confirmation)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        dialog?.tv_message?.text = Html.fromHtml(message)
        dialog?.tv_cancel?.gone()
        dialog?.tv_sure?.text = strOke
        dialog?.tv_sure?.setOnClickListener {
            listenerPositif()
            dialog.dismiss()
        }
        dialog?.setCancelable(false)
        dialog?.show()
    }

    fun confirmation( message: String, activity: Context?){
        val dialog = activity?.let { Dialog(it, R.style.MyCustomDialogFullScreen) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_alert_confirmation)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        dialog?.tv_message?.text = Html.fromHtml(message)
        dialog?.tv_cancel?.setOnClickListener {
            listenerNegatif()
            dialog.dismiss()
        }

        dialog?.tv_sure?.setOnClickListener {
            listenerPositif()
            dialog.dismiss()
        }
        dialog?.setCancelable(false)
        dialog?.show()
    }

    fun note(title: String, note: String, activity: Context?){
        val dialog = activity?.let { Dialog(it, R.style.MyCustomDialogFullScreen) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_fragment_free_text_add_sale)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        dialog?.tv_title_free_text?.text = title
        dialog?.ta_notes?.setText(note)
        dialog?.tv_cancel?.setOnClickListener {
            listenerNegatif()
            dialog.dismiss()
        }

        dialog?.tv_sure?.setOnClickListener {
            listenerPositifNote(dialog.ta_notes.text.toString())
            dialog.dismiss()
        }
        dialog?.setCancelable(false)
        dialog?.show()
    }

    fun qty(title: String, text: String, qty: Int, activity: Context?, unitName: String? = "SAK"){
        val dialog = activity?.let { Dialog(it, R.style.MyCustomDialogFullScreen) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_fragment_free_text_qty)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        dialog?.tv_title_free_text?.text = title
        dialog?.tv_text?.text = text
        dialog?.et_qty?.addTextChangedListener(NumberSeparator( dialog.et_qty))
        dialog?.et_qty?.setText(qty.toString())
        dialog?.tv_unit_name?.text = unitName
        dialog?.tv_cancel?.setOnClickListener {
            listenerNegatif()
            dialog.dismiss()
        }

        dialog?.tv_sure?.setOnClickListener {
            listenerPositifNote(dialog.et_qty.tag.toString())
            dialog.dismiss()
        }
        dialog?.setCancelable(false)
        dialog?.show()
    }
}