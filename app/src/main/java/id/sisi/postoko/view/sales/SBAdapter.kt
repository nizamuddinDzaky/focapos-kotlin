package id.sisi.postoko.view.sales

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_DELIVERY_STATUS_SALE
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.toDisplayStatus
import id.sisi.postoko.utils.extensions.toDisplayStatusColor
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.list_item_sales_booking.view.*

class SBAdapter(private var fragmentActivity: FragmentActivity? = null) :
    PagedListAdapter<Sales, SBAdapter.ViewHolder>(DIFF_CALLBACK) {
    /*private var listener: (Sales?) -> Unit = {}*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_sales_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position), fragmentActivity)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeface = TypeFace()
        fun bindTo(sale: Sales?,  fragmentActivity: FragmentActivity? = null) {

            fragmentActivity?.assets?.let {
                typeface.typeFace("robot_font/Roboto-Bold.ttf",itemView.tv_sales_reference_no,it)
                typeface.typeFace("robot_font/Roboto-Bold.ttf",itemView.tv_label_sales_payment,it)
                typeface.typeFace("robot_font/Roboto-Bold.ttf",itemView.tv_sales_payment_status,it)
                typeface.typeFace("robot_font/Roboto-Bold.ttf",itemView.tv_label_sales_deliv,it)
                typeface.typeFace("robot_font/Roboto-Bold.ttf",itemView.tv_sales_delevery_status,it)
                typeface.typeFace("robot_font/Roboto-Bold.ttf",itemView.tv_sales_total_price,it)
                typeface.typeFace("robot_font/Roboto-Regular.ttf",itemView.tv_sales_detail,it)
            }

            sale?.let {
                itemView.tv_sales_reference_no?.text = it.reference_no
                val date = " ${it.date.toDisplayDate()}"
                itemView.tv_sales_date?.text = date
                itemView.tv_sales_delevery_status?.text =
                    it.delivery_status?.toDisplayStatus()?.let { it1 -> itemView.context.getText(it1) }
                it.delivery_status?.toDisplayStatusColor()?.let { it1 ->
                    itemView.tv_sales_delevery_status.setTextColor(
                        it1
                    )
                }

                itemView.tv_sales_payment_status?.text =
                    itemView.context.getText(it.payment_status.toDisplayStatus())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.tv_sales_payment_status.setTextColor(itemView.context.getColor(it.payment_status.toDisplayStatusColor()))
                }else{
                    itemView.tv_sales_payment_status.setTextColor(ResourcesCompat.getColor(itemView.resources, it.payment_status.toDisplayStatusColor(), null))
                }

                itemView.tv_sales_total_price?.text = it.grand_total.toCurrencyID()
                val seeDetail = "Lihat ${it.total_items} Rincian Item"
                itemView.tv_sales_detail?.text = seeDetail
                itemView.setOnClickListener {
                    val page = Intent(itemView.context, DetailSalesBookingActivity::class.java)
                    page.putExtra(KEY_ID_SALES_BOOKING, sale.id)
                    page.putExtra(KEY_DELIVERY_STATUS_SALE, sale.delivery_status)
                    itemView.context.startActivity(page)
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