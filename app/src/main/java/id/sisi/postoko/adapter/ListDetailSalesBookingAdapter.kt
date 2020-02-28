package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.SaleItem

class ListDetailSalesBookingAdapter(var saleItems: List<SaleItem>? = arrayListOf()) : RecyclerView.Adapter<ListDetailSalesBookingAdapter.ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_product_sales_booking, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return saleItems?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(saleItems?.get(position))
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(saleItem: SaleItem?) {
        }
    }

    fun updateSaleItems(newSaleItems: List<SaleItem>?) {
        saleItems = newSaleItems
        notifyDataSetChanged()
    }
}