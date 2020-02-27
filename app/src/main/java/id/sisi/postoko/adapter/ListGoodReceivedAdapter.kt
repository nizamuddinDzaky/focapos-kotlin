package id.sisi.postoko.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.utils.extensions.checkVisibility
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.goodreveived.DetailGoodReceivedActivity
import id.sisi.postoko.view.ui.goodreveived.GoodReceiveStatus
import id.sisi.postoko.view.ui.goodreveived.GoodReceiveStatus.DELIVERING
import kotlinx.android.synthetic.main.list_item_gr.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.sisi.postoko.view.ui.goodreveived.BottomSheetGoodReceiveFragment


class ListGoodReceivedAdapter(
    private var goodsReceived: List<GoodReceived>? = arrayListOf(),
    private var status: GoodReceiveStatus = DELIVERING,
    private var listener: () -> Unit = {}
) : RecyclerView.Adapter<ListGoodReceivedAdapter.DetailGoodReceivedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailGoodReceivedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_gr, parent, false)

        return DetailGoodReceivedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return goodsReceived?.size ?: 0
    }

    override fun onBindViewHolder(holder: DetailGoodReceivedViewHolder, position: Int) {
        holder.bind(goodsReceived?.get(position), status, listener)
    }

    class DetailGoodReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(goodReceived: GoodReceived?, status: GoodReceiveStatus, listener: () -> Unit) {
            goodReceived?.let {
                itemView.tv_good_received_do_number?.text = it.no_do
                itemView.tv_good_received_so_number?.text = it.no_so
                itemView.tv_good_received_date?.text = it.tanggal_do.toDisplayDateFromDO()
                itemView.btn_action_receive_gr?.checkVisibility(status == DELIVERING)
            }
            itemView.tv_action_detail_gr?.setOnClickListener {
                logE("click action detail")
                val page = Intent(itemView.context, DetailGoodReceivedActivity::class.java)
                page.putExtra("data", Bundle())
                itemView.context.startActivity(page)
            }
            itemView.btn_action_receive_gr?.setOnClickListener {
                logE("click action receive")
                listener()
            }
        }
    }

    fun updateGoodsReceivedData(newTransactionsData: List<GoodReceived>?) {
        goodsReceived = newTransactionsData
        notifyDataSetChanged()
    }
}

fun String.toDisplayDateFromDO(): String {
    try {
        val dateInFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateOutFormat = SimpleDateFormat("dd MMM yy", Locale("id", "ID"))
        dateInFormat.parse(this)?.let {
            return dateOutFormat.format(it)
        }
    } catch (e: Exception) { }

    return this
}