package id.sisi.postoko.view.research

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.utils.KEY_GOOD_RECEIVED
import id.sisi.postoko.utils.extensions.checkVisibility
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toDisplayDateFromDO
import id.sisi.postoko.view.ui.gr.DetailGoodReceivedActivity
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus
import kotlinx.android.synthetic.main.list_item_gr.view.*

class GRAdapter(private var listener: (GoodReceived?) -> Unit = {}) :
    PagedListAdapter<GoodReceived, GRAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_gr, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position), listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(goodReceived: GoodReceived?, listener: (GoodReceived?) -> Unit) {
            goodReceived?.let {
                itemView.tv_product_name?.text = it.nama_produk
                val strQty = "${it.qty_do} ${it.uom}"
                itemView.tv_qty?.text = strQty
                itemView.tv_good_received_do_number?.text = it.no_do
                itemView.tv_good_received_so_number?.text = it.no_so
                itemView.tv_good_received_date?.text = it.tanggal_do?.toDisplayDateFromDO()
                it.status_penerimaan?.equals(GoodReceiveStatus.RECEIVED.name, true)?.let { show ->
                    itemView.btn_action_receive_gr?.checkVisibility(
                        !show
                    )
                }
            }
            itemView.setOnClickListener {
                val page = Intent(itemView.context, DetailGoodReceivedActivity::class.java)
                page.putExtra(KEY_GOOD_RECEIVED, goodReceived)
                itemView.context.startActivity(page)
            }
            itemView.btn_action_receive_gr?.setOnClickListener {
                listener(goodReceived)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GoodReceived>() {
            override fun areItemsTheSame(oldItem: GoodReceived, newItem: GoodReceived): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GoodReceived, newItem: GoodReceived): Boolean {
                return oldItem == newItem
            }
        }
    }
}