package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.PurchaseItem
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toNumberID
import kotlinx.android.synthetic.main.list_item_add_good_received.view.*

class ListItemAddGoodReceiveAdapter(
    private var listPurchase: List<PurchaseItem>? = arrayListOf()
) : RecyclerView.Adapter<ListItemAddGoodReceiveAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_add_good_received, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPurchase?.size ?: 0
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        holder.bind(listPurchase?.get(position))
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(purchaseItem: PurchaseItem?) {
            itemView.tv_detail_good_received_name.text =  purchaseItem?.product_name
            itemView.tv_detail_good_received_price.text = purchaseItem?.unit_price?.toCurrencyID()
            itemView.tv_detail_good_received_quantity.text = purchaseItem?.quantity?.toNumberID()

            itemView.et_detail_good_received_new_price.addTextChangedListener(NumberSeparator(itemView.et_detail_good_received_new_price))
            itemView.et_detail_good_received_new_price?.setText(purchaseItem?.unit_price?.toDouble()?.toInt().toString())
        }
    }

    fun updateData(newData: List<PurchaseItem>){
        listPurchase = newData
        notifyDataSetChanged()
    }
}