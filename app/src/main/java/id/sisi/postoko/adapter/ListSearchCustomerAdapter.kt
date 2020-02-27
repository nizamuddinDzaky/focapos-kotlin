package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import kotlinx.android.synthetic.main.list_item_master.view.*

class ListSearchCustomerAdapter (private var masterData: List<Customer>? = arrayListOf()) : RecyclerView.Adapter<ListSearchCustomerAdapter.SearchCustomerViewHolder>() {

    var listener: (Customer) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCustomerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_master, parent, false)

        return SearchCustomerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: SearchCustomerViewHolder, position: Int) {
        holder.bind(masterData?.get(position), listener)
    }

    class SearchCustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: Customer?, listener: (Customer) -> Unit) {
            itemView.tv_master_data_name?.text = value?.company
            itemView.tv_master_data_description?.text = value?.address
            itemView.setOnClickListener {
                if (value != null) {
                    listener(value)
                }
            }
        }
    }

    fun updateMasterData(newMasterData: List<Customer>?) {
        masterData = newMasterData
        notifyDataSetChanged()
    }
}