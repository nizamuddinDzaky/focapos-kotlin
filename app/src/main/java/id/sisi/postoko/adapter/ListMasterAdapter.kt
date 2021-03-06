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
import id.sisi.postoko.utils.MyPopupMenu
import id.sisi.postoko.utils.extensions.MyToast
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.showErrorL
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.ui.customer.DetailCustomerActivity
import id.sisi.postoko.view.ui.customergroup.AddCustomerToCustomerGoupActivity
import id.sisi.postoko.view.ui.pricegroup.AddCustomerPriceGroupActivity
import id.sisi.postoko.view.ui.pricegroup.DetailPriceGroupActivity
import kotlinx.android.synthetic.main.list_item_master.view.*
import java.text.NumberFormat
import java.util.*

class ListMasterAdapter<T>(
    private var masterData: List<T>? = arrayListOf(),
    private var fragmentActivity: FragmentActivity? = null
) :
    RecyclerView.Adapter<ListMasterAdapter.MasterViewHolder<T>>() {

    var listenerCustomerGroup: (CustomerGroup) -> Unit = {}
    var listenerPriceGroup: (PriceGroup) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_master, parent, false)

        return MasterViewHolder(view, fragmentActivity)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: MasterViewHolder<T>, position: Int) {
        holder.bind(masterData?.get(position), listenerCustomerGroup, listenerPriceGroup)
    }

    class MasterViewHolder<T>(
        itemView: View,
        private val fragmentActivity: FragmentActivity? = null
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(value: T?, listenerCustomerGroup: (CustomerGroup) -> Unit = {}, listenerPriceGroup: (PriceGroup) -> Unit = {}) {
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
                    val name = "${value.company}"
                    itemView.tv_master_data_name?.text = name
                    val address = "${value.city}, ${value.country}"
                    itemView.tv_master_data_description?.text = address
                    itemView.tv_master_data_description2?.text = value.cf1
                    itemView.setOnClickListener {
                        val page = Intent(itemView.context, DetailCustomerActivity::class.java)
                        page.putExtra(KEY_ID_CUSTOMER, value.id?.toInt())
                        itemView.context.startActivity(page)
                    }
                }
                /*is Product -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.code
                }*/
                is PriceGroup -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.warehouse_name
                    itemView.btn_menu_more?.visible()
                    itemView.tv_master_data_description2.gone()
                    itemView.tv_master_data_description.gone()
                    itemView.btn_menu_more?.setOnClickListener {
                        showPopupPriceGroup(it, value, listenerPriceGroup)
                    }
                    itemView.setOnClickListener {
                        itemView.btn_menu_more?.performClick()
                    }
                }

                is CustomerGroup -> {
                    val localeID = Locale("in", "ID")
                    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = formatRupiah.format(value.kredit_limit).toString()
                    itemView.btn_menu_more?.visible()
                    itemView.tv_master_data_description2.gone()
                    itemView.btn_menu_more?.setOnClickListener {
                        showPopupCustomerGroup(it, value, listenerCustomerGroup)
                    }
                    itemView.setOnClickListener {
                        itemView.btn_menu_more?.performClick()
                    }
                }
            }
        }

        private fun showPopupPriceGroup(view: View, priceGroup: PriceGroup, listenerPriceGroup: (PriceGroup) -> Unit = {}) {
            val listAction: MutableList<() -> Unit?> = mutableListOf(
                {
                    AddCustomerPriceGroupActivity.show(
                        fragmentActivity as FragmentActivity,
                        priceGroup
                    )
                },
                {
                    listenerPriceGroup(priceGroup)
                },
                {
                    DetailPriceGroupActivity.show(
                        fragmentActivity as FragmentActivity,
                        priceGroup
                    )
                }
            )

            val listMenu: MutableList<String> = mutableListOf(
                itemView.context.getString(R.string.txt_insert_customer_to_price_group),
                itemView.context.getString(R.string.txt_edit_price_group),
                itemView.context.getString(R.string.txt_see_detail)
            )

            MyPopupMenu(
                view,
                listMenu,
                listAction,
                highlight = itemView
            ).show()
        }

        private fun showPopupCustomerGroup(view: View, customerGroup: CustomerGroup, listenerCustomerGroup: (CustomerGroup) -> Unit = {}) {

            val listAction: MutableList<() -> Unit?> = mutableListOf(
                {
                    AddCustomerToCustomerGoupActivity.show(
                        fragmentActivity as FragmentActivity,
                        customerGroup
                    )
                },
                {
                    listenerCustomerGroup(customerGroup)
                }
            )

            val listMenu: MutableList<String> = mutableListOf(
                itemView.context.getString(R.string.txt_insert_customer_to_customer_group),
                itemView.context.getString(R.string.txt_edit_customer_group)
            )

            if (customerGroup.id == "1"){
                listAction.removeAt(1)
                listMenu.removeAt(1)
            }

            MyPopupMenu(
                view,
                listMenu,
                listAction,
                highlight = itemView
            ).show()
        }
    }

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData
        notifyDataSetChanged()
    }
}