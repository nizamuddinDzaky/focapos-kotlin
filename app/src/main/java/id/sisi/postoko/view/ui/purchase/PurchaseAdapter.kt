package id.sisi.postoko.view.ui.purchase

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Purchases
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.toDisplayStatusColor
import id.sisi.postoko.utils.extensions.toNumberID
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.list_item_purchase.view.*
import kotlinx.android.synthetic.main.list_item_purchase.view.btn_menu_more
import kotlinx.android.synthetic.main.list_item_purchase.view.tv_date
import kotlinx.android.synthetic.main.list_item_purchase.view.tv_payment_status
import kotlinx.android.synthetic.main.list_item_purchase.view.tv_reference_no
import kotlinx.android.synthetic.main.list_item_purchase.view.tv_total_price
import kotlinx.android.synthetic.main.list_item_sales_booking.view.*


class PurchaseAdapter: PagedListAdapter<Purchases, PurchaseAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_purchase, parent, false)
        return ViewHolder(view)
    }
    /*var closeSaleListener: (id: Int) -> Unit = {}*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(purchase: Purchases?) {
            purchase?.let {
                itemView.tv_reference_no.text = purchase.reference_no
                itemView.tv_date.text = purchase.date.toDisplayDate()
                itemView.tv_total_price.text = purchase.total?.toCurrencyID()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.tv_payment_status.setTextColor(itemView.context.getColor(it.payment_status.toDisplayStatusColor()))
                    itemView.tv_purchase_status.setTextColor(itemView.context.getColor(it.status.toDisplayStatusColor()))
                }else{
                    itemView.tv_payment_status.setTextColor(ResourcesCompat.getColor(itemView.resources,
                        it.payment_status?.toDisplayStatusColor(), null))
                    itemView.tv_purchase_status.setTextColor(ResourcesCompat.getColor(itemView.resources,
                        it.status?.toDisplayStatusColor(), null))
                }
                itemView.tv_purchase_status.text = purchase.status.capitalize()
                itemView.tv_payment_status.text = purchase.payment_status.capitalize()
                val listAction: MutableList<() -> Unit?> = mutableListOf(
                    {
                        val page = Intent(itemView.context, DetailPurchaseActivity::class.java)
                        page.putExtra(KEY_ID_PURCHASE, purchase.id)
                        page.putExtra(KEY_PURCHASE_STATUS, purchase.status)
                        itemView.context.startActivity(page)
                    }
                )
                val listMenu: MutableList<String> = mutableListOf(
                    itemView.context.getString(R.string.txt_see_detail)
                )

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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Purchases>() {
            override fun areItemsTheSame(oldItem: Purchases, newItem: Purchases): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Purchases, newItem: Purchases): Boolean {
                return oldItem == newItem
            }
        }
    }
}