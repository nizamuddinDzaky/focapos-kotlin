package id.sisi.postoko.utils.extensions

import id.sisi.postoko.adapter.toCurrencyID
import id.sisi.postoko.adapter.toNumberID

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun String.toCurrencyID(): String {
    return (this.toDoubleOrNull() ?: 0.0).toCurrencyID()
}

fun String.toNumberID(): String {
    return (this.toDoubleOrNull() ?: 0.0).toNumberID()
}