package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.utils.extensions.goneIfEmptyOrNull
import id.sisi.postoko.utils.extensions.strikeText
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toNumberID
import kotlinx.android.synthetic.main.list_product_sales_booking.view.*

class ListDetailSalesBookingAdapter(private var saleItems: List<SaleItem>? = arrayListOf()) :
    RecyclerView.Adapter<ListDetailSalesBookingAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_product_sales_booking, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return saleItems?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(saleItems?.get(position))
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(saleItem: SaleItem?) {
            itemView.tv_detail_sbo_item_price.text = saleItem?.unit_price?.toCurrencyID()
            itemView.tv_detail_sbo_item_name?.text = saleItem?.product_name
            val quantity = "${saleItem?.quantity?.toNumberID()} ${saleItem?.product_unit_code}"
            itemView.tv_detail_sbo_item_quantity?.text = quantity
            itemView.tv_detail_sbo_item_discount?.text =saleItem?.discount?.toCurrencyID()
        }
    }

    fun updateSaleItems(newSaleItems: List<SaleItem>?) {
        saleItems = newSaleItems
        notifyDataSetChanged()
    }
}