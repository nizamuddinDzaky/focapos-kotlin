package id.sisi.postoko.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Delivery
import id.sisi.postoko.utils.MyPopupMenu
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.toDisplayStatus
import id.sisi.postoko.utils.extensions.toDisplayStatusColor
import id.sisi.postoko.view.ui.delivery.DeliveryStatus
import kotlinx.android.synthetic.main.list_item_pengiriman.view.*
import java.util.*

class ListPengirimanAdapter(
    private var deliveries: List<Delivery>? = listOf()
) : RecyclerView.Adapter<ListPengirimanAdapter.ProductViewHolder>() {

    var listenerItem: OnClickListenerInterface? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_pengiriman, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return deliveries?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(deliveries?.get(position), listenerItem)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            delivery: Delivery?,
            listenerItem:OnClickListenerInterface?
        ) {

            delivery?.let {
                logE("$it")
                itemView.tv_delivery_date?.text = it.date.toDisplayDate()
                itemView.tv_delivery_sales_order_number?.text = it.sale_reference_no
                itemView.tv_delivery_delivery_order_number?.text = it.do_reference_no
                itemView.tv_status_deliv?.text =itemView.context.getText(it.status.toDisplayStatus())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.tv_status_deliv.setTextColor(itemView.context.getColor(it.status.toDisplayStatusColor()))
                }else{
                    itemView.tv_status_deliv.setTextColor(ResourcesCompat.getColor(itemView.resources, it.status.toDisplayStatusColor(), null))
                }

                itemView.tv_delivery_driver_name?.text = it.delivered_by
            }
            itemView.btn_menu_more.setOnClickListener {
                val listAction: MutableList<() -> Unit?>
                val listMenu: MutableList<String>
                if (delivery?.status == DeliveryStatus.PACKING.toString().toLowerCase(Locale.ROOT)
                    || delivery?.status == DeliveryStatus.DELIVERING.toString().toLowerCase(Locale.ROOT)){
                    listAction = mutableListOf(
                        {
                            listenerItem?.onClickDetail(delivery)
                        },
                        {
                            listenerItem?.onClickEdit(delivery)
                        },
                        {
                            listenerItem?.onClickAttachment(delivery.attachment)
                        }
                    )
                    listMenu = mutableListOf(
                        itemView.context.getString(R.string.txt_see_detail),
                        itemView.context.getString(R.string.txt_edit_delivery),
                        itemView.context.getString(R.string.txt_see_attachment)
                    )
                    MyPopupMenu(
                        it,
                        listMenu,
                        listAction,
                        highlight = itemView
                    ).show()
                }else if (delivery?.status == DeliveryStatus.DELIVERED.toString().toLowerCase(Locale.ROOT)){
                    listAction = mutableListOf(
                        {
                            listenerItem?.onClickDetail(delivery)
                        },
                        {
                            listenerItem?.onClickReturn(delivery)
                        },
                        {
                            listenerItem?.onClickAttachment(delivery.attachment)
                        }
                    )
                    listMenu = mutableListOf(
                        itemView.context.getString(R.string.txt_see_detail),
                        itemView.context.getString(R.string.txt_return_delivery),
                        itemView.context.getString(R.string.txt_see_attachment)
                    )
                    MyPopupMenu(
                        it,
                        listMenu,
                        listAction,
                        highlight = itemView
                    ).show()

                }else if (delivery?.status == DeliveryStatus.RETURNED.toString().toLowerCase(Locale.ROOT)){
                    listAction = mutableListOf(
                        {
                            listenerItem?.onClickDetail(delivery)
                        },
                        {
                            listenerItem?.onClickAttachment(delivery.attachment)
                        }
                    )
                    listMenu = mutableListOf(
                        itemView.context.getString(R.string.txt_see_detail),
                        itemView.context.getString(R.string.txt_see_attachment)
                    )
                    MyPopupMenu(
                        it,
                        listMenu,
                        listAction,
                        highlight = itemView
                    ).show()
                }
            }
            itemView.setOnClickListener {
                itemView.btn_menu_more?.performClick()
            }
        }
    }

    fun updateData(newData: List<Delivery>?) {
        deliveries = newData
        notifyDataSetChanged()
    }

    interface OnClickListenerInterface {
        fun onClickDetail(delivery: Delivery)
        fun onClickEdit(delivery: Delivery)
        fun onClickReturn(delivery: Delivery)
        fun onClickAttachment(url: String?)
    }
}