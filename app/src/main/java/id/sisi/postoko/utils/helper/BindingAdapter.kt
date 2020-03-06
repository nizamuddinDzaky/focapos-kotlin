package id.sisi.postoko.utils.helper

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.toDisplayDateFromDO

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