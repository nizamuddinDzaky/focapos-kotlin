package id.sisi.postoko.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Payment
import id.sisi.postoko.utils.MyPopupMenu
import id.sisi.postoko.utils.extensions.*
import kotlinx.android.synthetic.main.list_item_pembayaran.view.*
import kotlinx.android.synthetic.main.list_item_pembayaran.view.expandable_layout
import kotlinx.android.synthetic.main.list_item_product_price_group.view.*


class ListPaymentAdapter(
    private var payments: List<Payment>? = listOf(),
    var listener: (String?) -> Unit = {},
    var listenerEdit: (Payment?) -> Unit = {}
) : RecyclerView.Adapter<ListPaymentAdapter.ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_pembayaran, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return payments?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(payments?.get(position), listener, listenerEdit)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(payment: Payment?, listener: (String?) -> Unit, listenerEdit: (Payment?) -> Unit = {}) {
            payment?.let {
                itemView.tv_payment_reference_no?.text = it.reference_no
                itemView.tv_payment_amount?.text = it.amount.toCurrencyID()
                itemView.tv_payment_date?.text = it.date.toDisplayDateTime()
                itemView.tv_payment_type.text = itemView.context.getText(it.paid_by.toDisplayPaymentType())

                if (it.isCollapse){
                    itemView.expandable_layout.expand(true)
                }else{
                    itemView.expandable_layout.collapse(true)
                }

                itemView.tv_note.text = payment.note
                /*when(TextUtils.isEmpty(payment.note)){
                    is true ->{
                        itemView.tv_note.text = payment.note
                    }
                    is false -> {
                        itemView.tv_note.text = payment.note
                    }
                }*/
                if (!TextUtils.isEmpty(payment.note)){
                    itemView.tv_note.text = payment.note
                }else{
                    itemView.tv_note.text = itemView.context.getText(R.string.txt_not_set_note)
                }
                itemView.tv_see_note.setOnClickListener {v ->
                    it.isCollapse = !(it.isCollapse)
                    if (it.isCollapse){
                        itemView.expandable_layout.expand(true)
                    }else{
                        itemView.expandable_layout.collapse(true)
                    }
                }
            }
            itemView.btn_menu_more.setOnClickListener {
                val listAction: MutableList<() -> Unit?> = mutableListOf(
                    {
                        listenerEdit(payment)
                    },
                    {
                        if (payment?.attachment == null){
                            Toast.makeText(itemView.context, itemView.context.getString(R.string.txt_no_attachment), Toast.LENGTH_SHORT).show()
                        }else{
                            listener(payment.attachment)
                        }
                    }
                )
                val listMenu: MutableList<String> = mutableListOf(
                    itemView.context.getString(R.string.txt_edit),
                    itemView.context.getString(R.string.txt_see_attachment)
                )
                MyPopupMenu(
                    it,
                    listMenu,
                    listAction,
                    highlight = itemView
                ).show()
            }


            itemView.main_layout.setOnClickListener {
                itemView.btn_menu_more?.performClick()
            }
        }
    }

    fun updateData(newPayments: List<Payment>?) {
        payments = newPayments
        notifyDataSetChanged()
    }
}