package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.DeliveryItem
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.onChange
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
        holder.bind(item?.get(position), listenerProduct, position, this)
    }

    class ProductViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var alert = MyDialog()
        private var unsentQty = 0.0
        private var quantity = ""
        private var strUnsentQty = ""

        fun bind(
            value: T?,
            listenerProduct: OnClickListenerInterface?,
            position: Int,
            listItemDeliveryAdapter: ListItemDeliveryAdapter<T>
        ) {
            when(value){
                is SaleItem -> {
                    unsentQty = value.unit_quantity?.minus(value.sent_quantity) ?: 0.0
                    strUnsentQty = "${unsentQty.toInt().toNumberID()} ${value.product_unit_code}"
                    quantity = value.quantity?.toNumberID() ?: ""
                    /*itemView.et_sale_item_qty.setText(value.quantity?.toNumberID())*/

                    itemView.tv_sale_item_name?.text = value.product_name
                    itemView.tv_product_code?.text = value.product_code
                    val strQtySale = "${value.unit_quantity?.toNumberID()} ${value.product_unit_code}"
                    itemView.tv_sale_item_qty_unit_name?.text = strQtySale
                    itemView.tv_alias_product?.text = value.product_name.toAlias()

                }

                is DeliveryItem -> {
                    unsentQty = (value.quantity_ordered?.minus(value.all_sent_qty ?: 0.0)) ?: 0.0
                    strUnsentQty = "${unsentQty.toNumberID()} ${value.product_unit_code}"
                    quantity = value.quantity_sent?.toNumberID() ?: ""

                    itemView.tv_sale_item_name?.text = value.product_name
                    itemView.tv_product_code?.text = value.product_code
                    val strQtySale = "${value.quantity_ordered?.toNumberID()} ${value.product_unit_code}"
                    itemView.tv_sale_item_qty_unit_name?.text = strQtySale
                    itemView.tv_alias_product?.text = value.product_name.toAlias()
                }
            }

            itemView.iv_remove_product_delivery.setOnClickListener {
                val qty = itemView.et_sale_item_qty.text.toString().toInt().minus(1)
                itemView.et_sale_item_qty.setText(qty.toString())
                /*value.quantity?.let { it1 -> listenerProduct?.onClickMinus(it1, position) }*/
            }
            itemView.iv_add_product_delivery.setOnClickListener {
                /*value.quantity?.let { it1 -> listenerProduct?.onClickPlus(it1, position) }*/
            }
            itemView.btn_delete.setOnClickListener {
                listenerProduct?.onClickDelete(position)
            }

            itemView.et_sale_item_qty.setText(quantity)
            /*itemView.et_sale_item_qty.onChange {
                val res = listenerProduct?.onChange(position, itemView.et_sale_item_qty.text.toString())
                logE("debugging : $res => ")
                if (res == true){
                    listItemDeliveryAdapter.notifyDataSetChanged()
                }
            }*/
            itemView.tv_qty_unsent?.text = strUnsentQty
            /*itemView.et_sale_item_qty.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    val strQty = itemView.et_sale_item_qty.text.toString()
                    if (!TextUtils.isEmpty(strQty)){

                        when(value){
                            is SaleItem ->{
                                value.quantity = itemView.et_sale_item_qty.text.toString().toDouble()
                            }
                            is DeliveryItem ->{
                                value.quantity_sent = itemView.et_sale_item_qty.text.toString().toDouble()
                            }
                        }

                        val qty = itemView.et_sale_item_qty.text.toString().toDouble()

                        if (unsentQty < qty){
                            alert.alert(itemView.context.getString(R.string.txt_alert_out_of_qty), itemView.context)
                            alert.listenerPositif = {
                                itemView.et_sale_item_qty.setText(quantity)
                            }
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence,start: Int,count: Int,after: Int) {}

                override fun onTextChanged(s: CharSequence,start: Int,before: Int,count: Int) {}
            })*/


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
        /*fun onClickPlus(qty: Double, position: Int)
        fun onClickMinus(qty: Double, position: Int)*/
        fun onClickDelete(position: Int)
        fun onChange(position: Int, qty: String): Boolean
    }
}