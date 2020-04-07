package id.sisi.postoko.adapter

import android.content.Intent
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.*
import id.sisi.postoko.utils.KEY_ID_CUSTOMER
import id.sisi.postoko.utils.extensions.MyToast
import id.sisi.postoko.utils.extensions.showErrorL
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.ui.customer.DetailCustomerActivity
import id.sisi.postoko.view.ui.pricegroup.AddCustomerPriceGroupActivity
import id.sisi.postoko.view.ui.pricegroup.AddPriceGroupActivity
import id.sisi.postoko.view.ui.pricegroup.BottomSheetEditPriceGroupFragment
import kotlinx.android.synthetic.main.list_item_master.view.*

class ListMasterAdapter<T>(
    private var masterData: List<T>? = arrayListOf(),
    private var fragmentActivity: FragmentActivity? = null
) :
    RecyclerView.Adapter<ListMasterAdapter.MasterViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_master, parent, false)

        return MasterViewHolder(view, fragmentActivity)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: MasterViewHolder<T>, position: Int) {
        holder.bind(masterData?.get(position))
    }

    class MasterViewHolder<T>(
        itemView: View,
        private val fragmentActivity: FragmentActivity? = null
    ) :
        RecyclerView.ViewHolder(itemView) {

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
                is PriceGroup -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.warehouse_name
                    itemView.btn_menu_more?.visible()
                    itemView.btn_menu_more?.setOnClickListener {
                        showPopup(it, value)
                    }
                    itemView.setOnClickListener {
                        itemView.btn_menu_more?.performClick()
                    }
                }
            }
        }

        private fun showPopup(view: View, priceGroup: PriceGroup) {
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.menu_more_price_group)

            popup.setOnMenuItemClickListener { item: MenuItem? ->
                when (item?.itemId) {
                    R.id.menu_more_price_group_add_customer -> {
                        AddCustomerPriceGroupActivity.show(
                            fragmentActivity as FragmentActivity,
                            priceGroup
                        )
                    }
                    R.id.menu_more_price_group_edit -> {
                        fragmentActivity?.let {
                            BottomSheetEditPriceGroupFragment.show(
                                it.supportFragmentManager,
                                priceGroup
                            )
                        }
                    }
                    R.id.menu_more_price_group_detail -> {
                        MyToast.make(view.context).showErrorL("coming soon")
                    }
                    else -> {
                    }
                }
                true
            }

            popup.setOnDismissListener {
                val outValue = TypedValue()
                fragmentActivity?.theme?.resolveAttribute(
                    android.R.attr.selectableItemBackground,
                    outValue,
                    true
                )
                itemView.setBackgroundResource(outValue.resourceId)
            }

            itemView.setBackgroundColor(Color.LTGRAY)
            popup.show()
        }
    }

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData
        notifyDataSetChanged()
    }
}