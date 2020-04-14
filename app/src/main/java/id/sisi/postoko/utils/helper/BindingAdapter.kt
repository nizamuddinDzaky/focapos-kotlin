package id.sisi.postoko.utils.helper

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.toDisplayDateFromDO
import id.sisi.postoko.utils.extensions.toNumberID
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus

@BindingAdapter("currencyID")
fun TextView.bindCurrencyID(amount: String?) {
    text = amount?.toCurrencyID()
}

@BindingAdapter("displayDateOnly")
fun TextView.bindDisplayDateOnly(date: String?) {
    text = date?.toDisplayDateFromDO()
}

@BindingAdapter("displayDateTime")
fun TextView.bindDisplayDateTime(date: String?) {
    text = date?.toDisplayDate()
}

@BindingAdapter("quantitySak")
fun TextView.quantitySak(quantity: String?) {
    val quantitySak = "${quantity?.toNumberID()} SAK"
    text = quantitySak
}

@BindingAdapter("checkVisibility")
fun View.setNewVisibility(isShown: Boolean?) {
    visibility = if (isShown == true) View.VISIBLE else View.GONE
}

@BindingAdapter("isReceived")
fun View.checkIsReceived(status_penerimaan: String?) {
    val isReceived = status_penerimaan?.equals(GoodReceiveStatus.RECEIVED.name, true)
    visibility = if (isReceived == false) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
    this.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}