package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import kotlin.random.Random

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
        }
    }

    fun updateSalesData(newMasterData: List<Sales>?) {
        sales = newMasterData
        notifyDataSetChanged()
    }
}