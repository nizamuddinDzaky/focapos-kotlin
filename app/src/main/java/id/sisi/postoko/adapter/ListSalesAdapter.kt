package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import kotlinx.android.synthetic.main.list_item_sales_booking.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class ListSalesAdapter(private var sales: List<Sales>? = arrayListOf()) : RecyclerView.Adapter<ListSalesAdapter.SalesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_sales_booking, parent, false)

        return SalesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sales?.size ?: 0
    }

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        holder.bind(sales?.get(position))
    }

    class SalesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(sale: Sales?) {
            sale?.let {
                itemView.tv_sales_reference_no?.text = it.reference_no
                itemView.tv_sales_date?.text = " ${it.date.toDisplayDate()}"
                itemView.tv_sales_delevery_status?.text = itemView.context.getText(it.delivery_status.toDisplayStatus())
                itemView.tv_sales_payment_status?.text = itemView.context.getText(it.payment_status.toDisplayStatus())
                itemView.tv_sales_total_price?.text = it.grand_total.toCurrencyID()
                itemView.tv_sales_detail?.text = "Lihat ${it.total_items} Rincian Item"
            }
        }
    }

    fun updateSalesData(newMasterData: List<Sales>?) {
        sales = newMasterData
        notifyDataSetChanged()
    }
}

fun String.toDisplayStatus(): Int {
    var idString = 0
    if (this == "pending") {
        idString = R.string.txt_status_pending
    } else if (this == "confirmed") {
        idString = R.string.txt_status_confirmed
    } else if (this == "reserved") {
        idString = R.string.txt_status_reserved
    } else if (this == "closed") {
        idString = R.string.txt_status_closed
    } else if (this == "canceled") {
        idString = R.string.txt_status_canceled
    } else if (this == "partial") {
        idString = R.string.txt_status_partial
    } else if (this == "due") {
        idString = R.string.txt_status_due
    } else if (this == "waiting") {
        idString = R.string.txt_status_waiting
    } else if (this == "paid") {
        idString = R.string.txt_status_paid
    } else if (this == "done") {
        idString = R.string.txt_status_done
    } else if (this == "packing") {
        idString = R.string.txt_status_packing
    } else if (this == "delivering") {
        idString = R.string.txt_status_delivering
    } else if (this == "delivered") {
        idString = R.string.txt_status_delivered
    } else if (this == "returned") {
        idString = R.string.txt_status_returned
    } else if (this == "sent") {
        idString = R.string.txt_status_sent
    } else if (this == "completed") {
        idString = R.string.txt_status_completed
    } else if (this == "received") {
        idString = R.string.txt_status_received
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

fun Number.toCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this)
}

fun Number.toCurrencyID(): String {
    return NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this)
}