package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import kotlin.random.Random

class ListOrderAdapter : RecyclerView.Adapter<ListOrderAdapter.OrderViewHolder>() {
    private val nData = Random.nextInt(5, 10)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_order, parent, false)

        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nData
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind()
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
        }
    }
}