package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toCurrencyID
import kotlinx.android.synthetic.main.list_product_sales.view.*
import java.text.NumberFormat
import java.util.*


class ListProductAddSalesAdapter(private var masterData: List<SaleItem>? = arrayListOf()) :
    RecyclerView.Adapter<ListProductAddSalesAdapter.ProductAddSalesViewHolder>() {

    var listenerProduct: OnClickListenerInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAddSalesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_product_sales, parent, false)
        return ProductAddSalesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductAddSalesViewHolder, position: Int) {
        holder.bind(masterData?.get(position), listenerProduct, position)
    }

    class ProductAddSalesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: SaleItem?, listener: OnClickListenerInterface?, position: Int) {
            val price = value?.unit_price.toString().toDouble()

            itemView.tv_product_name_add_sale?.text = value?.product_name
            itemView.tv_product_price_add_sale?.text = price.toCurrencyID()
            itemView.tv_alias_product.text = value?.product_name.toAlias()
            if (value?.quantity == 0.0)
                itemView.et_qty_produk_add_sale.text = "1"
            else
                itemView.et_qty_produk_add_sale.text = value?.quantity?.toInt().toString()

            itemView.tv_subtoal_add_sale?.text = value?.subtotal?.toCurrencyID()

            itemView.iv_remove_product_add_sale.setOnClickListener {
                value?.quantity?.let { it1 -> listener?.onClickMinus(it1, position) }
            }
            itemView.iv_add_product_add_sale.setOnClickListener {
                value?.quantity?.let { it1 -> listener?.onClickPlus(it1, position) }
            }
            itemView.tv_edit_product_add_sale.setOnClickListener {
                if (value != null) {
                    listener?.onClickEdit(value, position)
                }
            }
        }
    }

    fun updateMasterData(newMasterData: List<SaleItem>?) {
        masterData = newMasterData
        notifyDataSetChanged()
    }

    interface OnClickListenerInterface {
        fun onClickPlus(qty: Double, position: Int)
        fun onClickMinus(qty: Double, position: Int)
        fun onClickEdit(saleItem: SaleItem, position: Int)
    }
}