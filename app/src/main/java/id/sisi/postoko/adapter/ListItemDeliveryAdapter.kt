package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.utils.extensions.toNumberID
import kotlinx.android.synthetic.main.list_item_add_delivery.view.*


class ListItemDeliveryAdapter(
    private var saleItem: List<SaleItem>? = arrayListOf()
) : RecyclerView.Adapter<ListItemDeliveryAdapter.ProductViewHolder>(){

    var listenerProduct: OnClickListenerInterface? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_add_delivery, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return saleItem?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(saleItem?.get(position), listenerProduct, position)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(saleItem: SaleItem?, listenerProduct:OnClickListenerInterface?, position:Int) {
            saleItem?.let {
                itemView.tv_sale_item_name?.text = it.product_name
                itemView.tv_sale_item_qty?.text = it.quantity?.toNumberID()
                val quantity = "${it.quantity?.toNumberID()} SAK"
                itemView.tv_sale_item_qty_unit_name?.text = quantity
            }
            itemView.iv_remove_product_delivery.setOnClickListener {
                    saleItem?.quantity?.let { it1 -> listenerProduct?.onClickMinus(it1, position) }
//                saleItem?.quantity?.let { it1 -> listenerProduct?.onClickMinus(it1, position) }
            }
            itemView.iv_add_product_delivery.setOnClickListener {
                saleItem?.quantity?.let { it1 -> listenerProduct?.onClickPlus(it1, position) }
            }
//            itemView.setOnClickListener {
//                listener(saleItem)
//            }
        }
    }
    interface OnClickListenerInterface {
        fun onClickPlus(qty: Double, position: Int)
        fun onClickMinus(qty: Double, position: Int)
    }
}