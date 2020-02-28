package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Payment
import kotlinx.android.synthetic.main.list_item_pembayaran.view.*

class ListPaymentAdapter(
    private var payments: List<Payment>? = listOf(),
    var listener: (Payment?) -> Unit = {}
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
        holder.bind(payments?.get(position), listener)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(payment: Payment?, listener: (Payment?) -> Unit) {
            payment?.let {
                itemView.tv_payment_reference_no?.text = it.reference_no
                itemView.tv_payment_amount?.text = it.amount.toCurrencyID()
                itemView.tv_payment_date?.text = it.date.toDisplayDate()
            }
            itemView.setOnClickListener {
                listener(payment)
            }
        }
    }

    fun updateData(newPayments: List<Payment>?) {
        payments = newPayments
        notifyDataSetChanged()
    }
}