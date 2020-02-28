package id.sisi.postoko.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
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
                val date = " ${it.date.toDisplayDate()}"
                itemView.tv_sales_date?.text = date
                itemView.tv_sales_delevery_status?.text =
                    itemView.context.getText(it.delivery_status.toDisplayStatus())
                itemView.tv_sales_payment_status?.text =
                    itemView.context.getText(it.payment_status.toDisplayStatus())
                itemView.tv_sales_total_price?.text = it.grand_total.toCurrencyID()
                val seeDetail = "Lihat ${it.total_items} Rincian Item"
                itemView.tv_sales_detail?.text = seeDetail
                itemView.setOnClickListener {
                    logE("click action detail sales ${sale.id}")
                    val page = Intent(itemView.context, DetailSalesBookingActivity::class.java)
                    page.putExtra(KEY_ID_SALES_BOOKING, sale.id)
                    itemView.context.startActivity(page)
                }
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