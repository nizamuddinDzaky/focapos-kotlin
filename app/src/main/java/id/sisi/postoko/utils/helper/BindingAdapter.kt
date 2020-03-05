package id.sisi.postoko.utils.helper

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import id.sisi.postoko.utils.extensions.toCurrencyID

@BindingAdapter("currencyID")
fun TextView.bindCurrencyID(amount: String?) {
    text = amount?.toCurrencyID()
}