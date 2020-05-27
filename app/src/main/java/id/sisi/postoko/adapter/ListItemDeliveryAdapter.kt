package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.DeliveryItem
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.utils.extensions.*
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
        private var unsentQty = 0.0
        private var quantity = 0.0
        private var strUnsentQty = ""

        fun bind(
            value: T?,
            listenerProduct: OnClickListenerInterface?,
            position: Int
        ) {
            when(value){
                is SaleItem -> {
                    unsentQty = value.unit_quantity?.minus(value.sent_quantity) ?: 0.0
                    strUnsentQty = "${unsentQty.toInt().toNumberID()} ${value.product_unit_code}"
                    quantity = value.quantity ?: 0.0

                    itemView.tv_sale_item_name?.text = value.product_name
                    itemView.tv_product_code?.text = value.product_code
                    val strQtySale = "${value.unit_quantity?.toNumberID()} ${value.product_unit_code}"
                    itemView.tv_sale_item_qty_unit_name?.text = strQtySale
                    itemView.tv_alias_product?.text = value.product_name.toAlias()

                }

                is DeliveryItem -> {
                    unsentQty = (value.quantity_ordered?.minus(value.all_sent_qty ?: 0.0)) ?: 0.0
                    strUnsentQty = "${unsentQty.toNumberID()} ${value.product_unit_code}"
                    quantity = value.quantity_sent ?: 0.0

                    itemView.tv_sale_item_name?.text = value.product_name
                    itemView.tv_product_code?.text = value.product_code
                    val strQtySale = "${value.quantity_ordered?.toNumberID()} ${value.product_unit_code}"
                    itemView.tv_sale_item_qty_unit_name?.text = strQtySale
                    itemView.tv_alias_product?.text = value.product_name.toAlias()
                    itemView.btn_delete.gone()
                }
            }

            itemView.iv_remove_product_delivery.setOnClickListener {
                quantity.let { it1 -> listenerProduct?.onClickMinus(it1, position) }
            }
            itemView.iv_add_product_delivery.setOnClickListener {
                quantity.let { it1 -> listenerProduct?.onClickPlus(it1, position) }
            }
            itemView.btn_delete.setOnClickListener {
                listenerProduct?.onClickDelete(position)
            }

            itemView.et_sale_item_qty.setText(quantity.toNumberID())
            itemView.et_sale_item_qty.setOnClickListener {
                listenerProduct?.onChange(position)
            }
            itemView.tv_qty_unsent?.text = strUnsentQty
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
        fun onChange(position: Int)
    }
}