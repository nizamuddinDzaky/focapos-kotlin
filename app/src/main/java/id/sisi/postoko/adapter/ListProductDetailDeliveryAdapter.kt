package id.sisi.postoko.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.DeliveryItem
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toNumberID
import kotlinx.android.synthetic.main.list_product_detail_delivery.view.*

class ListProductDetailDeliveryAdapter(
    private var sales: List<DeliveryItem>? = arrayListOf()
) : RecyclerView.Adapter<ListProductDetailDeliveryAdapter.DeliveryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_product_detail_delivery, parent, false)

        return DeliveryItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sales?.size ?: 0
    }

    override fun onBindViewHolder(holder: DeliveryItemViewHolder, position: Int) {
        holder.bind(sales?.get(position))
    }

    class DeliveryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(deliveryItem: DeliveryItem?) {
            itemView.tv_product_name.text = deliveryItem?.product_name
            itemView.tv_product_code.text = deliveryItem?.product_unit_code
            itemView.tv_quantity.text = "${deliveryItem?.all_sent_qty?.toNumberID()} ${deliveryItem?.product_unit_code}"
            itemView.tv_qty_good.text = "${deliveryItem?.good_quantity?.toNumberID()} ${deliveryItem?.product_unit_code}"
            itemView.tv_qty_bad.text = "${deliveryItem?.bad_quantity?.toNumberID()} ${deliveryItem?.product_unit_code}"
            itemView.tv_alias_product.text = deliveryItem?.product_name.toAlias()
        }
    }

    fun updateSalesData(newMasterData: List<DeliveryItem>?) {
        sales = newMasterData
        notifyDataSetChanged()
    }
}