package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Delivery
import kotlinx.android.synthetic.main.list_item_pengiriman.view.*

class ListPengirimanAdapter(
    private var deliveries: List<Delivery>? = listOf(),
    var listener: (Delivery?) -> Unit = {}
) : RecyclerView.Adapter<ListPengirimanAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_pengiriman, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return deliveries?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(deliveries?.get(position), listener)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(delivery: Delivery?, listener: (Delivery?) -> Unit) {
            delivery?.let {
                itemView.tv_delivery_date?.text = it.date.toDisplayDate()
                itemView.tv_delivery_sales_order_number?.text = it.sale_reference_no
                itemView.tv_delivery_delivery_order_number?.text = it.do_reference_no
//                itemView.tv_delivery_quantity?.text = it.
//                itemView.tv_delivery_total?.text = it.
                itemView.tv_delivery_driver_name?.text = it.delivered_by
            }
            itemView.setOnClickListener {
                listener(delivery)
            }
        }
    }

    fun updateData(newData: List<Delivery>?) {
        deliveries = newData
        notifyDataSetChanged()
    }
}