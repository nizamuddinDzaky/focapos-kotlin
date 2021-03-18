package id.sisi.postoko.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_DELIVERY_STATUS_SALE
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.list_item_sales_booking.view.*

class ListSalesAdapter(
    private var sales: List<Sales>? = arrayListOf()
) : RecyclerView.Adapter<ListSalesAdapter.SalesViewHolder>() {

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
                itemView.tv_reference_no?.text = it.reference_no
                val date = " ${it.date.toDisplayDate()}"
                itemView.tv_date?.text = date
                itemView.tv_delevery_status?.text =
                    it.delivery_status?.toDisplayStatus()?.let { it1 -> itemView.context.getText(it1) }
                itemView.tv_payment_status?.text =
                    itemView.context.getText(it.payment_status?.toDisplayStatus() ?: 0)
                itemView.tv_total_price?.text = it.grand_total?.toCurrencyID()
                val seeDetail = "Lihat ${it.total_items} Rincian Item"
                itemView.tv_detail?.text = seeDetail
                itemView.setOnClickListener {
                    val page = Intent(itemView.context, DetailSalesBookingActivity::class.java)
                    page.putExtra(KEY_ID_SALES_BOOKING, sale.id)
                    page.putExtra(KEY_DELIVERY_STATUS_SALE, sale.delivery_status)
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