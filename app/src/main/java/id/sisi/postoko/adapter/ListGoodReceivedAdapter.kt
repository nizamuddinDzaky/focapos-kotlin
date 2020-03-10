package id.sisi.postoko.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.utils.extensions.checkVisibility
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toDisplayDateFromDO
import id.sisi.postoko.view.ui.gr.DetailGoodReceivedActivity
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus.DELIVERING
import kotlinx.android.synthetic.main.list_item_gr.view.*


class ListGoodReceivedAdapter(
    private var goodsReceived: List<GoodReceived>? = arrayListOf(),
    private var status: GoodReceiveStatus = DELIVERING,
    private var listener: (GoodReceived?) -> Unit = {}
) : RecyclerView.Adapter<ListGoodReceivedAdapter.ViewHolder>() {

    companion object {
        val ITEM_VIEW_TYPE_CONTENT = 1
        val ITEM_VIEW_TYPE_LOADING = 2
    }

    private var isLoading = false

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        return when (viewType) {
//            ITEM_VIEW_TYPE_CONTENT -> DetailGoodReceivedViewHolder(
//                layoutInflater.inflate(R.layout.list_item_gr, null)
//            )
//            else -> LoadingViewHolder(
//                layoutInflater.inflate(R.layout.list_item_data_loading, null)
//            )
//        }
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailGoodReceivedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_gr, parent, false)

        return DetailGoodReceivedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return goodsReceived?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!isLastItem(position)) {
            (holder as? DetailGoodReceivedViewHolder)?.bind(
                goodsReceived?.get(position),
                status,
                listener
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (goodsReceived?.size ?: 0 < 10) {
            return ITEM_VIEW_TYPE_CONTENT
        }
        return if (isLastItem(position)) ITEM_VIEW_TYPE_LOADING else ITEM_VIEW_TYPE_CONTENT
    }

    private fun isLastItem(position: Int): Boolean {
        return goodsReceived?.size?.minus(1) == position
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : ViewHolder(itemView)

    class DetailGoodReceivedViewHolder(itemView: View) : ViewHolder(itemView) {

        fun bind(
            goodReceived: GoodReceived?,
            status: GoodReceiveStatus,
            listener: (GoodReceived?) -> Unit
        ) {
            goodReceived?.let {
                itemView.tv_good_received_do_number?.text = it.no_do
                itemView.tv_good_received_so_number?.text = it.no_so
                itemView.tv_good_received_date?.text = it.tanggal_do?.toDisplayDateFromDO()
                itemView.btn_action_receive_gr?.checkVisibility(status == DELIVERING)
            }
            itemView.setOnClickListener {
                logE("click action detail")
                val page = Intent(itemView.context, DetailGoodReceivedActivity::class.java)
                page.putExtra("good_received", goodReceived)
                itemView.context.startActivity(page)
            }
            itemView.btn_action_receive_gr?.setOnClickListener {
                logE("click action receive")
                listener(goodReceived)
            }
        }
    }

    fun updateGoodsReceivedData(newTransactionsData: List<GoodReceived>?) {
        val tempList = newTransactionsData?.toMutableList()
        logE("isi sebelum ${tempList?.size}")
        tempList?.add(GoodReceived())
        goodsReceived = tempList
        logE("isi sesudah ${goodsReceived?.size}")
        notifyDataSetChanged()
    }
}