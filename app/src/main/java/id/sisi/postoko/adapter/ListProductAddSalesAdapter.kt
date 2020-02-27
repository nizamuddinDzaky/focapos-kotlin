package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import kotlinx.android.synthetic.main.list_item_product_add_sales.view.*

class ListProductAddSalesAdapter (private var masterData: List<Product>? = arrayListOf()) : RecyclerView.Adapter<ListProductAddSalesAdapter.SearchCustomerViewHolder>() {

    var listenerProductAdapter : (Product) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCustomerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_product_sales, parent, false)
        return SearchCustomerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: SearchCustomerViewHolder, position: Int) {
        holder.bind(masterData?.get(position), listenerProductAdapter)
    }

    class SearchCustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: Product?, listener: (Product) -> Unit) {
            itemView.tv_product_name?.text = value?.name
            itemView.tv_product_price?.text = "Rp " + value?.price
            itemView.setOnClickListener {
                if (value != null) {
                    listener(value)
                }
            }
        }
    }

    fun updateMasterData(newMasterData: List<Product>?) {
        masterData = newMasterData
        notifyDataSetChanged()
    }
}