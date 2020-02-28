package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.SaleItem
import kotlinx.android.synthetic.main.list_item_add_delivery.view.*


class ListItemDeliveryAdapter(
    private var payments: List<SaleItem>? = listOf(),
    var listener: (SaleItem?) -> Unit = {}
) : RecyclerView.Adapter<ListItemDeliveryAdapter.ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_add_delivery, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return payments?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(payments?.get(position), listener)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(saleItem: SaleItem?, listener: (SaleItem?) -> Unit) {
            saleItem?.let {
                itemView.tv_sale_item_name?.text = it.product_name
                itemView.tv_sale_item_qty?.text = it.quantity?.toNumberID()
                val quantity = "${it.quantity?.toNumberID()} SAK"
                itemView.tv_sale_item_qty_unit_name?.text = quantity
            }
            itemView.setOnClickListener {
                listener(saleItem)
            }
        }
    }

}