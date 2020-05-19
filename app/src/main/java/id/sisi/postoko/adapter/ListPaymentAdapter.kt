package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Payment
import id.sisi.postoko.utils.MyPopupMenu
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.toDisplayPaymentType
import kotlinx.android.synthetic.main.list_item_pembayaran.view.*

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
                itemView.tv_payment_date?.text = it.date.toDisplayDate()
                itemView.tv_payment_type.text = itemView.context.getText(it.paid_by.toDisplayPaymentType())
            }
            itemView.btn_menu_more.setOnClickListener {
                val listAction: MutableList<() -> Unit?> = mutableListOf({listenerEdit(payment)})
                val listMenu: MutableList<String> = mutableListOf(itemView.context.getString(R.string.txt_edit))
                MyPopupMenu(
                    it,
                    listMenu,
                    listAction,
                    highlight = itemView
                ).show()
            }
            itemView.tv_attachment.setOnClickListener {
                if (payment?.attachment == null){
                    Toast.makeText(itemView.context, "Tidak Ada lampiran", Toast.LENGTH_SHORT).show()
                }else{
                    listener(payment.attachment)
                }
            }
        }
    }

    fun updateData(newPayments: List<Payment>?) {
        payments = newPayments
        notifyDataSetChanged()
    }
}