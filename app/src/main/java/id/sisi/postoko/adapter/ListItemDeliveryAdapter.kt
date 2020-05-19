package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.DeliveryItem
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toNumberID
import kotlinx.android.synthetic.main.list_item_add_delivery.view.*


class ListItemDeliveryAdapter<T>(
    private var item: List<T>? = arrayListOf()
) : RecyclerView.Adapter<ListItemDeliveryAdapter.ProductViewHolder<T>>(){

    var listenerProduct: OnClickListenerInterface? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_add_delivery, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder<T>, position: Int) {
        holder.bind(item?.get(position), listenerProduct, position)
    }

    class ProductViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: T?, listenerProduct:OnClickListenerInterface?, position:Int) {
            when(value){
                is SaleItem -> {
                    itemView.tv_sale_item_name?.text = value.product_name
                    itemView.tv_sale_item_qty?.text = value.quantity?.toNumberID()
                    itemView.tv_product_code?.text = value.product_code
                    val strUnsentQty = "${(value.unit_quantity?.minus(value.sent_quantity))?.toNumberID()} ${value.product_unit_code}"
                    itemView.tv_qty_unsent?.text = strUnsentQty
                    val strQtySale = "${value.unit_quantity?.toNumberID()} ${value.product_unit_code}"
                    itemView.tv_sale_item_qty_unit_name?.text = strQtySale
                    itemView.tv_alias_product?.text = value.product_name.toAlias()
                    itemView.iv_remove_product_delivery.setOnClickListener {
                        value.quantity?.let { it1 -> listenerProduct?.onClickMinus(it1, position) }
                    }
                    itemView.iv_add_product_delivery.setOnClickListener {
                        value.quantity?.let { it1 -> listenerProduct?.onClickPlus(it1, position) }
                    }
                    itemView.btn_delete.setOnClickListener {
                        listenerProduct?.onClickDelete(position)
                    }
                }
                is DeliveryItem -> {
                    itemView.tv_sale_item_name?.text = value.product_name
                    itemView.tv_sale_item_qty?.text = value.quantity_sent?.toNumberID()
                    itemView.tv_product_code?.text = value.product_code
                    val strUnsentQty = "${(value.quantity_ordered?.minus(value.all_sent_qty ?: 0.0))?.toNumberID()} ${value.product_unit_code}"
                    itemView.tv_qty_unsent?.text = strUnsentQty
                    val strQtySale = "${value.quantity_ordered?.toNumberID()} ${value.product_unit_code}"
                    itemView.tv_sale_item_qty_unit_name?.text = strQtySale
                    itemView.tv_alias_product?.text = value.product_name.toAlias()
                    itemView.iv_remove_product_delivery.setOnClickListener {
                        value.quantity_sent?.let { it1 -> listenerProduct?.onClickMinus(it1, position) }
                    }
                    itemView.iv_add_product_delivery.setOnClickListener {
                        value.quantity_sent?.let { it1 -> listenerProduct?.onClickPlus(it1, position) }
                    }
                    itemView.btn_delete.setOnClickListener {
                        /*listenerProduct?.onClickDelete(position)*/
                    }
                }
            }


//            itemView.setOnClickListener {
//                listener(saleItem)
//            }
        }
    }

    fun updateDate(deliveryItems: List<T>?) {
        item = deliveryItems
        notifyDataSetChanged()
    }

    interface OnClickListenerInterface {
        fun onClickPlus(qty: Double, position: Int)
        fun onClickMinus(qty: Double, position: Int)
        fun onClickDelete(position: Int)
    }
}