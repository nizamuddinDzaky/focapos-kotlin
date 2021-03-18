package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.PurchaseItem
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toNumberID
import kotlinx.android.synthetic.main.list_product_pruchase.view.*

class ListDetailPurchaseAdapter(private var purchaseItem: List<PurchaseItem>? = arrayListOf()) :
    RecyclerView.Adapter<ListDetailPurchaseAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_product_pruchase, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return purchaseItem?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(purchaseItem?.get(position))
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(purchaseItem: PurchaseItem?) {
            itemView.tv_detail_purchase_item_name.text = purchaseItem?.product_name
            itemView.tv_detail_purchase_item_price.text = purchaseItem?.real_unit_cost?.toCurrencyID()
            val quantity = "${purchaseItem?.quantity?.toNumberID()} ${purchaseItem?.product_unit_code}"
            itemView.tv_detail_purchase_item_quantity.text = quantity
            /*itemView.tv_detail_purchase_item_name.text = purchaseItem?.product_name*/
            /*itemView.tv_detail_sbo_item_price.text = saleItem?.unit_price?.toCurrencyID()
            itemView.tv_detail_sbo_item_name?.text = saleItem?.product_name
            val quantity = "${saleItem?.quantity?.toNumberID()} ${saleItem?.product_unit_code}"
            itemView.tv_detail_sbo_item_quantity?.text = quantity
            itemView.tv_detail_sbo_item_discount?.text =saleItem?.discount?.toCurrencyID()*/
        }
    }

    fun updateSaleItems(newPurchaseItem: List<PurchaseItem>?) {
        purchaseItem = newPurchaseItem
        notifyDataSetChanged()
    }
}