package id.sisi.postoko.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.list_item_order.view.*

class ListSalesAdapter(private var sales: List<Sales>? = arrayListOf()) : RecyclerView.Adapter<ListSalesAdapter.SalesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_order, parent, false)

        return SalesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sales?.size ?: 0
    }

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        holder.bind("")
    }

    class SalesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: String) {
            itemView.tv_action_detail_sales_booking?.setOnClickListener {
                logE("click action detail sales")
                val page = Intent(itemView.context, DetailSalesBookingActivity::class.java)
                page.putExtra("data", Bundle())
                itemView.context.startActivity(page)
            }
        }
    }

    fun updateSalesData(newMasterData: List<Sales>?) {
        sales = newMasterData
        notifyDataSetChanged()
    }
}