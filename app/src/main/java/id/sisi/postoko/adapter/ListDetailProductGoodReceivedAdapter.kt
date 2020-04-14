package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.databinding.ListProductGoodReceivedBinding
import id.sisi.postoko.model.PurchaseItem

class ListDetailProductGoodReceivedAdapter(private var purchasesItem: List<PurchaseItem>? = arrayListOf()) :
    RecyclerView.Adapter<ListDetailProductGoodReceivedAdapter.DetailProductGoodReceivedViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailProductGoodReceivedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListProductGoodReceivedBinding.inflate(layoutInflater)
        return DetailProductGoodReceivedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return purchasesItem?.size ?: 0
    }

    override fun onBindViewHolder(holder: DetailProductGoodReceivedViewHolder, position: Int) {
        holder.binding.purchaseItem = purchasesItem?.get(position)
        holder.binding.executePendingBindings()
    }

    class DetailProductGoodReceivedViewHolder(val binding: ListProductGoodReceivedBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updatePurchasesItem(newPurchasesItem: List<PurchaseItem>?) {
        purchasesItem = newPurchasesItem
        notifyDataSetChanged()
    }
}