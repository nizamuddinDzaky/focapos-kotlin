package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import kotlin.random.Random

class ListDetailProductGoodReceivedAdapter : RecyclerView.Adapter<ListDetailProductGoodReceivedAdapter.DetailProductGoodReceivedViewHolder>(){
    val nData = Random.nextInt(2, 10)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailProductGoodReceivedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_product_good_received, parent, false)

        return DetailProductGoodReceivedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nData
    }

    override fun onBindViewHolder(holder: DetailProductGoodReceivedViewHolder, position: Int) {
        holder.bind("")
    }

    class DetailProductGoodReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: String) {
        }
    }
}