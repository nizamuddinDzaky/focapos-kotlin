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


class ListProductAddSalesAdapter (private var masterData: List<SaleItem>? = arrayListOf()) : RecyclerView.Adapter<ListProductAddSalesAdapter.ProductAddSalesViewHolder>() {

    var listenerProductAdapter : (SaleItem) -> Unit = {}
    var listenerProduct : OnClickListenerInterface? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAddSalesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_product_sales, parent, false)
        return ProductAddSalesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductAddSalesViewHolder, position: Int) {
        holder.bind(masterData?.get(position), listenerProduct)
    }

    class ProductAddSalesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: SaleItem?, listener: OnClickListenerInterface?) {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            var price = value?.unit_price.toString().toDouble()

            itemView.tv_product_name?.text = value?.product_name
            itemView.tv_product_price?.text = formatRupiah.format(price).toString()

            if(value?.quantity == 0.0)
                itemView.et_qty_produk_sale_booking.setText("1")
            else
                itemView.et_qty_produk_sale_booking.setText(value?.quantity?.toInt().toString())

            var qty = itemView.et_qty_produk_sale_booking.text.toString().toDouble()
            var subtotal = hitungSubtotal(qty.toDouble(), price)
            value?.subtotal = subtotal
            itemView.tv_subtoal_add_sales_booking?.text = formatRupiah.format(subtotal).toString()

            itemView.iv_minus_product.setOnClickListener {
                qty = qty - 1
                value?.quantity = qty
                itemView.et_qty_produk_sale_booking.setText(qty.toInt().toString())
                var subtotal =hitungSubtotal(qty.toDouble(), price)
                itemView.tv_subtoal_add_sales_booking?.text = formatRupiah.format(subtotal).toString()
                value?.subtotal = subtotal
                listener?.onClickMinus()
            }
            itemView.iv_add_product.setOnClickListener {
                qty = qty + 1
                value?.quantity = qty
                itemView.et_qty_produk_sale_booking.setText(qty.toInt().toString())
                var subtotal = hitungSubtotal(qty.toDouble(), price)
                itemView.tv_subtoal_add_sales_booking?.text = formatRupiah.format(subtotal).toString()
                value?.subtotal = subtotal
                listener?.onClickPlus()
            }
        }
        private fun hitungSubtotal(qty: Double, price: Double) : Double{
            var subtotal : Double
            subtotal = qty * price
            return subtotal
        }
    }

    fun updateMasterData(newMasterData: List<SaleItem>?) {
        masterData = newMasterData
        notifyDataSetChanged()
    }


}

interface OnClickListenerInterface {
    fun onClickPlus()
    fun onClickMinus()
}