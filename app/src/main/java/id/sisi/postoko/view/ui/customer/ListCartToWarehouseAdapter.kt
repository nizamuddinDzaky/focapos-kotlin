package id.sisi.postoko.view.ui.customergroup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.view.ui.customer.AddCustomerWarehouseActivity
import kotlinx.android.synthetic.main.list_customer_price_group_cart.view.*

class ListCartToWarehouseAdapter<T>(
    private var masterData: MutableList<T>? = mutableListOf(),
    private var fragmentActivity: FragmentActivity? = null
) :
    RecyclerView.Adapter<ListCartToWarehouseAdapter.MasterViewHolder<T>>() {
    private var lastPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_customer_price_group_cart, parent, false)

        return MasterViewHolder(view)
    }

    override fun getItemCount() = masterData?.size ?: 0

    override fun onBindViewHolder(holder: MasterViewHolder<T>, position: Int) {
        holder.bind(masterData?.get(position), this)
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(
        viewToAnimate: View,
        position: Int
    ) { // If the bound view wasn't previously displayed on screen, it's animated
        if (position == 0 && itemCount > 1) {
            val animation: Animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.item_animation_fall_down)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class MasterViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: T?, adapter: ListCartToWarehouseAdapter<T>) {
            when (value) {
                is Warehouse -> {
                    itemView.tv_list_customer_cart_title?.text = value.name
                    itemView.tv_alias_customer?.text = value.name.toAlias()
                    itemView.setOnClickListener {
                        adapter.unselected(value)
                    }
                }
            }
        }
    }

    private fun unselected(value: T) {
        val activity = (fragmentActivity as? AddCustomerWarehouseActivity)
        when (value) {
            is Warehouse -> {
                value.isSelected = !value.isSelected
                activity?.validation(value as Warehouse)
            }
        }
    }


    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData?.toMutableList()
        notifyDataSetChanged()
        if (itemCount > 0)
            notifyItemInserted(itemCount)
        else
            notifyDataSetChanged()
    }

    fun insertData(newData: T) {
        masterData?.add(0, newData)
        if (itemCount > 0)
            notifyItemInserted(0)
        else
            notifyDataSetChanged()
    }

    fun removeData(data: T){
        lastPosition -= 1
        val index = masterData?.indexOf(data)
        masterData?.remove(data)
        if (index != null)
            notifyItemRemoved(index)
        else
            notifyDataSetChanged()
    }
}