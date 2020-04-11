package id.sisi.postoko.view.ui.pricegroup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.extensions.toUpper
import kotlinx.android.synthetic.main.list_customer_price_group_cart.view.*

class ListCartToCustomerAdapter<T>(
    private var masterData: MutableList<T>? = mutableListOf(),
    private var fragmentActivity: FragmentActivity? = null
) :
    RecyclerView.Adapter<ListCartToCustomerAdapter.MasterViewHolder<T>>() {
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
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.item_animation_fall_down)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class MasterViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: T?, adapter: ListCartToCustomerAdapter<T>) {
            when (value) {
                is Customer -> {
                    itemView.tv_list_customer_cart_title?.text = value.company
                    itemView.tv_alias_customer?.text = getAlias(value.company)
                    itemView.setOnClickListener {
                        adapter.unselected(value)
                    }
                }
            }
        }

        private fun getAlias(name: String?): String {
            if (name.isNullOrEmpty()) return "#"
            if (name.length == 1) return name.toUpper()
            return name.toUpper().substring(0, 2)
        }
    }

    private fun unselected(value: T) {
        val activity = (fragmentActivity as? AddCustomerPriceGroupActivity)
        when (value) {
            is Customer -> {
                value.isSelected = !value.isSelected
                activity?.validation(value as Customer)
            }
        }
    }

    fun removeCustomerFromCart(value: T) {
        lastPosition -= 1
        val index = masterData?.indexOf(value)
        masterData?.remove(value)
        if (index != null)
            notifyItemRemoved(index)
        else
            notifyDataSetChanged()
    }

    fun addData(value: T) {
        masterData?.add(value)
        if (itemCount > 0)
            notifyItemInserted(itemCount)
        else
            notifyDataSetChanged()
    }

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData?.toMutableList()
        notifyDataSetChanged()

        if (itemCount > 0)
            notifyItemInserted(itemCount)
        else
            notifyDataSetChanged()
    }
}