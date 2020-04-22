package id.sisi.postoko.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_DELIVERY_STATUS_SALE
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.list_item_sales_booking.view.*

class ListSalesAdapter(
    private var sales: List<Sales>? = arrayListOf(),
    private var fragmentActivity: FragmentActivity? = null
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
        holder.bind(sales?.get(position), fragmentActivity)
    }

    class SalesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(sale: Sales?, fragmentActivity: FragmentActivity? = null) {

            sale?.let {
                itemView.tv_sales_reference_no?.text = it.reference_no
                val date = " ${it.date.toDisplayDate()}"
                itemView.tv_sales_date?.text = date
                itemView.tv_sales_delevery_status?.text =
                    it.delivery_status?.toDisplayStatus()?.let { it1 -> itemView.context.getText(it1) }
                itemView.tv_sales_payment_status?.text =
                    itemView.context.getText(it.payment_status.toDisplayStatus())
                itemView.tv_sales_total_price?.text = it.grand_total.toCurrencyID()
                val seeDetail = "Lihat ${it.total_items} Rincian Item"
                itemView.tv_sales_detail?.text = seeDetail
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