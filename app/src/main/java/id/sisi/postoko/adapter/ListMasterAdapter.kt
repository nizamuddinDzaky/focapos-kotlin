package id.sisi.postoko.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Product
import id.sisi.postoko.model.Supplier
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.KEY_ID_CUSTOMER
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.customer.DetailCustomerActivity
import kotlinx.android.synthetic.main.list_item_master.view.*

class ListMasterAdapter<T>(private var masterData: List<T>? = arrayListOf()) : RecyclerView.Adapter<ListMasterAdapter.MasterViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_master, parent, false)

        return MasterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: MasterViewHolder<T>, position: Int) {
        holder.bind(masterData?.get(position))
    }

    class MasterViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: T?) {
            when (value) {
                is Warehouse -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.address
                }
                is Supplier -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.address
                }
                is Customer -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.address
                    itemView.setOnClickListener {
                        val page = Intent(itemView.context, DetailCustomerActivity::class.java)
                        page.putExtra(KEY_ID_CUSTOMER, value.id?.toInt())
                        itemView.context.startActivity(page)
                    }
                }
                is Product -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.code
                }
            }
        }
    }

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData
        notifyDataSetChanged()
    }
}