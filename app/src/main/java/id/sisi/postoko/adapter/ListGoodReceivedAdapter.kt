package id.sisi.postoko.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.GoodReceived.DetailGoodReceivedActivity
import kotlinx.android.synthetic.main.list_item_gr.view.*
import kotlin.random.Random

class ListGoodReceivedAdapter : RecyclerView.Adapter<ListGoodReceivedAdapter.DetailGoodReceivedViewHolder>() {
    val nData = Random.nextInt(5, 10)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailGoodReceivedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_gr, parent, false)

        return DetailGoodReceivedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nData
    }

    override fun onBindViewHolder(holder: DetailGoodReceivedViewHolder, position: Int) {
        holder.bind("")
    }

    class DetailGoodReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: String) {
            itemView.tv_action_detail_gr?.setOnClickListener {
                logE("click action detail")
                val page = Intent(itemView.context, DetailGoodReceivedActivity::class.java)
                page.putExtra("data", Bundle())
                itemView.context.startActivity(page)
            }
            itemView.btn_action_receive_gr?.setOnClickListener {
                logE("click action receive")
            }
        }
    }
}