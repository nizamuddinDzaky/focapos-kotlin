package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import kotlin.random.Random

class ListDetailSalesBookingAdapter : RecyclerView.Adapter<ListDetailSalesBookingAdapter.ProductViewHolder>(){
    val nData = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_product_sales_booking, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nData
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind("")
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: String) {
        }
    }
}