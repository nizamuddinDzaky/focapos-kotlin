package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.SaleItem
import kotlinx.android.synthetic.main.list_item_product_add_sales.view.tv_product_name
import kotlinx.android.synthetic.main.list_item_product_add_sales.view.tv_product_price
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
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            val price = value?.unit_price.toString().toDouble()

            itemView.tv_product_name?.text = value?.product_name
            itemView.tv_product_price?.text = formatRupiah.format(price).toString()

            if (value?.quantity == 0.0)
                itemView.et_qty_produk_sale_booking.setText("1")
            else
                itemView.et_qty_produk_sale_booking.setText(value?.quantity?.toInt().toString())

            var qty = itemView.et_qty_produk_sale_booking.text.toString().toDouble()
            val subtotal = getSubTotal(qty, price)
            itemView.tv_subtoal_add_sales_booking?.text = formatRupiah.format(subtotal).toString()

            itemView.iv_minus_product.setOnClickListener {
                qty -= 1
                value?.quantity = qty
                itemView.et_qty_produk_sale_booking.setText(qty.toInt().toString())
                val subTotal = getSubTotal(qty, price)
                itemView.tv_subtoal_add_sales_booking?.text =
                    formatRupiah.format(subTotal).toString()
                value?.subtotal = subTotal
                listener?.onClickMinus()
            }
            itemView.iv_add_product.setOnClickListener {
                qty += 1
                value?.quantity = qty
                itemView.et_qty_produk_sale_booking.setText(qty.toInt().toString())
                val subTotal = getSubTotal(qty, price)
                itemView.tv_subtoal_add_sales_booking?.text =
                    formatRupiah.format(subTotal).toString()
                value?.subtotal = subTotal
                listener?.onClickPlus()
            }
            itemView.tv_edit_product_add_sale.setOnClickListener {
                if (value != null) {
                    listener?.onClickEdit(value, position)
                }
            }
        }

        private fun getSubTotal(qty: Double, price: Double): Double {
            return qty * price
        }
    }

    fun updateMasterData(newMasterData: List<SaleItem>?) {
        masterData = newMasterData
        notifyDataSetChanged()
    }

    interface OnClickListenerInterface {
        fun onClickPlus()
        fun onClickMinus()
        fun onClickEdit(saleItem: SaleItem, position: Int)
    }
}