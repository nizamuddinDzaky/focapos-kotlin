package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import kotlin.random.Random

class ListPurchaseAdapter : RecyclerView.Adapter<ListPurchaseAdapter.ProductViewHolder>() {
    val nData = Random.nextInt(5, 10)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_gr, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nData
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind("")
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: String) {
        }
    }
}