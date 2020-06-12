package id.sisi.postoko.view.sales

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_DELIVERY_STATUS_SALE
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.KEY_SALE_STATUS
import id.sisi.postoko.utils.MyPopupMenu
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.toDisplayStatus
import id.sisi.postoko.utils.extensions.toDisplayStatusColor
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.list_item_sales_booking.view.*

class SBAdapter :
    PagedListAdapter<Sales, SBAdapter.ViewHolder>(DIFF_CALLBACK) {
    /*private var listener: (Sales?) -> Unit = {}*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_sales_booking, parent, false)
        return ViewHolder(view)
    }
    var closeSaleListener: (id: Int) -> Unit = {}

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position), closeSaleListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(sale: Sales?, closeSaleListener: (id: Int) -> Unit = {}) {


            sale?.let {
                itemView.tv_sales_reference_no?.text = it.reference_no
                val date = " ${it.date.toDisplayDate()}"
                itemView.tv_sales_date?.text = date
                itemView.tv_sales_delevery_status?.text =
                    it.delivery_status?.toDisplayStatus()?.let { it1 -> itemView.context.getText(it1) }

                itemView.tv_sales_payment_status?.text =
                    itemView.context.getText(it.payment_status?.toDisplayStatus() ?: 0)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.tv_sales_payment_status.setTextColor(itemView.context.getColor(it.payment_status?.toDisplayStatusColor() ?: 0))
                    it.delivery_status?.toDisplayStatusColor()?.let { statusColor ->
                        itemView.context.getColor(
                            statusColor
                        )
                    }?.let { context -> itemView.tv_sales_delevery_status.setTextColor(context) }
                }else{
                    itemView.tv_sales_payment_status.setTextColor(ResourcesCompat.getColor(itemView.resources, it.payment_status?.toDisplayStatusColor() ?: 0, null))
                    it.delivery_status?.toDisplayStatusColor()?.let { it1 ->
                        ResourcesCompat.getColor(itemView.resources,
                            it1, null)
                    }?.let { it2 -> itemView.tv_sales_delevery_status.setTextColor(it2) }
                }
                itemView.tv_customer_name?.text = it.customer
                itemView.tv_sales_total_price?.text = it.grand_total?.toCurrencyID()
                val seeDetail = "Di Buat Oleh ${it.created_by}"
                itemView.tv_sales_detail?.text = seeDetail

                val listAction: MutableList<() -> Unit?> = mutableListOf(
                    {
                        val page = Intent(itemView.context, DetailSalesBookingActivity::class.java)
                        page.putExtra(KEY_ID_SALES_BOOKING, sale.id)
                        page.putExtra(KEY_SALE_STATUS, sale.sale_status)
                        page.putExtra(KEY_DELIVERY_STATUS_SALE, sale.delivery_status)
                        itemView.context.startActivity(page)
                    }
                )
                val listMenu: MutableList<String> = mutableListOf(
                    itemView.context.getString(R.string.txt_see_detail)
                )

                if (sale.sale_status == "reserved"){
                    listAction.add {
                        closeSaleListener(sale.id)
                    }
                    listMenu.add(itemView.context.getString(R.string.txt_close_sale))
                }

                itemView.btn_menu_more.setOnClickListener {v->
                    MyPopupMenu(
                        v,
                        listMenu,
                        listAction,
                        highlight = itemView
                    ).show()
                }
                itemView.setOnClickListener {
                    itemView.btn_menu_more?.performClick()
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Sales>() {
            override fun areItemsTheSame(oldItem: Sales, newItem: Sales): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Sales, newItem: Sales): Boolean {
                return oldItem == newItem
            }
        }
    }
}