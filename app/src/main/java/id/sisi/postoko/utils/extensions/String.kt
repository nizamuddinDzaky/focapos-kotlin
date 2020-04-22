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

fun String.toUpper() = this.toUpperCase(Locale.getDefault())

fun String.toDisplayStatusColor(): Int{
    var idString = 0
    when {
        this == "pending" -> {
            idString = R.color.main_orange
        }
        this == "confirmed" -> {
            idString = R.color.main_green
        }
        this == "reserved" -> {
            idString = R.color.main_green
        }
        this == "closed" -> {
            idString = R.color.main_red
        }
        this == "canceled" -> {
            idString = R.color.main_red
        }
        this == "partial" -> {
            idString = R.color.main_blue
        }
        this == "due" -> {
            idString = R.color.main_red
        }
        this == "waiting" -> {
            idString = R.color.main_orange
        }
        this == "paid" -> {
            idString = R.color.text_green
        }
        this == "done" -> {
            idString = R.color.text_green
        }
        this == "packing" -> {
            idString = R.color.main_orange
        }
        this == "delivering" -> {
            idString = R.color.main_blue
        }
        this == "delivered" -> {
            idString = R.color.text_green
        }
        this == "returned" -> {
            idString = R.color.main_red
        }
        this == "sent" -> {
            idString = R.color.text_green
        }
        this == "completed" -> {
            idString = R.color.text_green
        }
        this == "received" -> {
            idString = R.color.text_green
        }
    }

    return idString
}

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

fun String.toDisplayPaymentType(): Int{
    var idString = 0
    when{
        this == "cash" -> {
            idString = R.string.txt_payment_cash
        }
        this == "bank" -> {
            idString = R.string.txt_bank
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