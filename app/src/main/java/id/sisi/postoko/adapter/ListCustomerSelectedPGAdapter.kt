package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.toUpper
import kotlinx.android.synthetic.main.list_customer_selected.view.*

class ListCustomerSelectedPGAdapter (private var customer: List<Customer>? = arrayListOf(), private var fragmentActivity: FragmentActivity? = null): RecyclerView.Adapter<ListCustomerSelectedPGAdapter.CustomerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_customer_selected, parent, false)

        return CustomerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return customer?.size ?: 0
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(customer?.get(position))
    }

    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: Customer?) {
            itemView.tv_customer_name.text = value?.customer_name?.toUpper()
            itemView.tv_alias_customer.text = getAlias(value?.customer_name)
        }

        private fun getAlias(name: String?): String {
            if (name.isNullOrEmpty()) return "#"
            if (name.length == 1) return name.toUpper()
            return name.toUpper().substring(0, 2)
        }
    }

    fun updateCustomerData(newMasterData: List<Customer>?) {
        customer = newMasterData
        notifyDataSetChanged()
    }
}