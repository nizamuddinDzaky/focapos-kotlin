package id.sisi.postoko.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.PurchaseItem
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toNumberID
import kotlinx.android.synthetic.main.list_product_good_received.view.*

class ListDetailProductGoodReceivedAdapter(var purchasesItem: List<PurchaseItem>? = arrayListOf()) :
    RecyclerView.Adapter<ListDetailProductGoodReceivedAdapter.DetailProductGoodReceivedViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailProductGoodReceivedViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_product_good_received, parent, false)

        return DetailProductGoodReceivedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return purchasesItem?.size ?: 0
    }

    override fun onBindViewHolder(holder: DetailProductGoodReceivedViewHolder, position: Int) {
        holder.bind(purchasesItem?.get(position))
    }

    class DetailProductGoodReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(purchaseItem: PurchaseItem?) {
            purchaseItem?.let {
                itemView.tv_good_received_detail_item_product_name?.text = it.product_name
                val quantity = "${it.quantity.toNumberID()} SAK"
                itemView.tv_good_received_detail_item_quantity?.text = quantity
                itemView.tv_good_received_detail_item_price?.text = it.unit_price.toCurrencyID()
            }
        }
    }

    fun updatePurchasesItem(newPurchasesItem: List<PurchaseItem>?) {
        purchasesItem = newPurchasesItem
        notifyDataSetChanged()
    }
}