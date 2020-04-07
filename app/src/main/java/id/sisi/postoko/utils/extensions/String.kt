package id.sisi.postoko.utils.extensions

import android.content.Context
import id.sisi.postoko.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun String.toCurrencyID(): String {
    return (this.toDoubleOrNull() ?: 0.0).toCurrencyID()
}

fun String.toNumberID(): String {
    return (this.toDoubleOrNull() ?: 0.0).toNumberID()
}

fun String.toDisplayDateFromDO(): String {
    try {
        val dateInFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateOutFormat = SimpleDateFormat("dd MMM yy", Locale("id", "ID"))
        dateInFormat.parse(this)?.let {
            return dateOutFormat.format(it)
        }
    } catch (e: Exception) { }

    return this
}

fun String.toLower() = this.toLowerCase(Locale.getDefault())

fun String.toDisplayStatus(): Int {
    var idString = 0
    when {
        this == "pending" -> {
            idString = R.string.txt_status_pending
        }
        this == "confirmed" -> {
            idString = R.string.txt_status_confirmed
        }
        this == "reserved" -> {
            idString = R.string.txt_status_reserved
        }
        this == "closed" -> {
            idString = R.string.txt_status_closed
        }
        this == "canceled" -> {
            idString = R.string.txt_status_canceled
        }
        this == "partial" -> {
            idString = R.string.txt_status_partial
        }
        this == "due" -> {
            idString = R.string.txt_status_due
        }
        this == "waiting" -> {
            idString = R.string.txt_status_waiting
        }
        this == "paid" -> {
            idString = R.string.txt_status_paid
        }
        this == "done" -> {
            idString = R.string.txt_status_done
        }
        this == "packing" -> {
            idString = R.string.txt_status_packing
        }
        this == "delivering" -> {
            idString = R.string.txt_status_delivering
        }
        this == "delivered" -> {
            idString = R.string.txt_status_delivered
        }
        this == "returned" -> {
            idString = R.string.txt_status_returned
        }
        this == "sent" -> {
            idString = R.string.txt_status_sent
        }
        this == "completed" -> {
            idString = R.string.txt_status_completed
        }
        this == "received" -> {
            idString = R.string.txt_status_received
        }
    }
    return idString
}

fun String.toDisplayDate(): String {
    try {
        val dateInFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val dateOutFormat = SimpleDateFormat("dd MMM yy", Locale("id", "ID"))
        dateInFormat.parse(this)?.let {
            return dateOutFormat.format(it)
        }
    } catch (e: Exception) { }

    return this
}

fun Number.toCurrencyID(): String {
    return NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this)
}

fun Number.toNumberID(): String {
    return NumberFormat.getNumberInstance(Locale("id", "ID")).format(this)
}

fun Context.getTryString(resId: Int?): String {
    if (resId == null) return ""
    return getString(resId)
}