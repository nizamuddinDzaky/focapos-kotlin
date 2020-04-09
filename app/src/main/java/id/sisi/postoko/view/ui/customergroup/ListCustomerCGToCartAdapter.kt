package id.sisi.postoko.view.ui.pricegroup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.view.ui.customergroup.AddCustomerToCustomerGoupActivity
import kotlinx.android.synthetic.main.list_item_master.view.*

class ListCustomerCGToCartAdapter<T>(
    private var masterData: MutableList<T>? = mutableListOf(),
    private var fragmentActivity: FragmentActivity? = null
) :
    RecyclerView.Adapter<ListCustomerCGToCartAdapter.MasterViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_master, parent, false)

        return MasterViewHolder(view)
    }

    override fun getItemCount() = masterData?.size ?: 0

    override fun onBindViewHolder(holder: MasterViewHolder<T>, position: Int) {
        holder.bind(masterData?.get(position), this)
    }

    class MasterViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: T?, adapter: ListCustomerCGToCartAdapter<T>) {
            when (value) {
                is Customer -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.address
                    itemView.setOnClickListener {
                        adapter.addCustomerToCart(value)
                    }
                }
            }
        }
    }

    fun addCustomerToCart(value: T) {
        masterData?.remove(value)
        (fragmentActivity as? AddCustomerToCustomerGoupActivity)?.countInc(value as Customer)
        notifyDataSetChanged()
    }

    fun addData(value: T) {
        masterData?.add(value)
        notifyDataSetChanged()
    }

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData?.toMutableList()
        notifyDataSetChanged()
    }
}