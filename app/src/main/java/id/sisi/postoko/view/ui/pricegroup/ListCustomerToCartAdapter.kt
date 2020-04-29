package id.sisi.postoko.view.ui.pricegroup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toUpper
import id.sisi.postoko.utils.extensions.visibleOrGone
import kotlinx.android.synthetic.main.list_customer_price_group.view.*

class ListCustomerToCartAdapter<T>(
    private var masterData: List<T>? = listOf(),
    private var fragmentActivity: FragmentActivity? = null
) :
    RecyclerView.Adapter<ListCustomerToCartAdapter.MasterViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_customer_price_group, parent, false)

        return MasterViewHolder(view)
    }

    override fun getItemCount() = masterData?.size ?: 0

    override fun onBindViewHolder(holder: MasterViewHolder<T>, position: Int) {
        holder.bind(masterData?.get(position), this)
    }

    class MasterViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: T?, adapter: ListCustomerToCartAdapter<T>) {
            when (value) {
                is Customer -> {
                    value.isSelected.visibleOrGone(itemView.view_mark_selected)
                    val name = "${value.customer_company} (${value.customer_name})"
                    itemView.tv_customer_price_group_item_1?.text = name
                    itemView.tv_customer_price_group_item_2?.text = value.address
                    itemView.tv_alias_customer?.text =value.customer_company.toAlias()
                    itemView.setOnClickListener {
                        adapter.addCustomerToCart(value)
                    }
                }
            }
        }
    }

    fun addCustomerToCart(value: T) {
        val activity = (fragmentActivity as? AddCustomerPriceGroupActivity)
        when (value) {
            is Customer -> {
                value.isSelected = !value.isSelected
                activity?.validation(value as Customer)
            }
        }
        notifyDataSetChanged()
    }

    /*fun addData(value: T) {
        masterData?.add(value)
        notifyDataSetChanged()
    }*/

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData
        notifyDataSetChanged()
    }
}