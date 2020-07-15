package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toCurrencyID
import kotlinx.android.synthetic.main.list_cart_add_sale.view.*

class ListCartAddSaleAdapter(
    private var listCart: MutableList<Product>? = mutableListOf()
) : RecyclerView.Adapter<ListCartAddSaleAdapter.CartViewHolder>() {

    var listenerProduct: OnClickListenerInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_cart_add_sale, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCart?.size ?: 0
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(listCart?.get(position), listenerProduct)
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            product: Product?,
            listenerProduct: OnClickListenerInterface?
        ) {


            itemView.et_sale_item_qty.setOnClickListener {
                listenerProduct?.onChange(product)
            }

            itemView.tv_discount_item.text = product?.discount?.toCurrencyID()
            itemView.et_sale_item_qty.text = product?.sale_qty.toString()
            itemView.tv_product_name.text = product?.name
            itemView.tv_product_price.text = product?.price?.toCurrencyID()
            itemView.tv_alias_product.text = product?.name.toAlias()

            itemView.iv_minus.setOnClickListener {
                listenerProduct?.onClickMinus(product)
            }
            itemView.iv_plus.setOnClickListener {
                listenerProduct?.onClickPlus(product)
            }
            itemView.iv_delete.setOnClickListener {
                listenerProduct?.onClickDelete(product)
            }

            itemView.tv_edit.setOnClickListener {
                listenerProduct?.onClickEdit(product)
            }

        }
    }

    fun insertData(newData: Product) {
        listCart?.add(0, newData)
        if (itemCount > 0)
            notifyItemInserted(0)
        else
            notifyDataSetChanged()
    }

    fun removeData(data: Product){
        val index = listCart?.indexOf(data)
        listCart?.remove(data)
        if (index != null)
            notifyItemRemoved(index)
        else
            notifyDataSetChanged()
    }

    interface OnClickListenerInterface {
        fun onClickPlus(product: Product?)
        fun onClickMinus(product: Product?)
        fun onClickDelete(product: Product?)
        fun onClickEdit(product: Product?)
        fun onChange(product: Product?)
    }
}