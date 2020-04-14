package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.list_item_master.view.*

class ListSearchDialogFragmentAdapter<T> (private var masterData: List<T>? = arrayListOf()) : RecyclerView.Adapter<ListSearchDialogFragmentAdapter.SearchMasterViewHolder>() {

    var listener: (Customer) -> Unit = {}
    var listenerWarehouse: (Warehouse)-> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMasterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_master, parent, false)

        return SearchMasterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: SearchMasterViewHolder, position: Int) {
        holder.bind(masterData?.get(position), listener, listenerWarehouse)
    }

    class SearchMasterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: Any?, listener: (Customer) -> Unit, listenerWarehouse: (Warehouse)-> Unit) {
            when (value) {
                is Customer -> {
                    itemView.tv_master_data_name?.text = value.company
                    itemView.tv_master_data_description?.text = value.address
                    itemView.setOnClickListener {
                        listener(value)
                    }
                }
                is Warehouse -> {

                    itemView.tv_master_data_name?.text = value.name

                    itemView.tv_master_data_description?.text = value.address
                    itemView.setOnClickListener {
                        listenerWarehouse(value)
                    }
                }

            }
        }
    }

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData
        logE("nizamuddin123: $masterData")
        notifyDataSetChanged()
    }
}