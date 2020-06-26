package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.toAlias
import kotlinx.android.synthetic.main.list_warehouse_detail_customer.view.*

class ListWareHouseOfDetailCustomerAdapter(
    private var warehouses: List<Warehouse>? = listOf()
): RecyclerView.Adapter<ListWareHouseOfDetailCustomerAdapter.WarehouseViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_warehouse_detail_customer, parent, false)

        return WarehouseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return warehouses?.size ?: 0
    }

    override fun onBindViewHolder(holder: WarehouseViewHolder, position: Int) {
        holder.bind(warehouses?.get(position))
    }

    class WarehouseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            warehouse: Warehouse?
        ) {
            warehouse?.let {wh ->
                /*itemView.tv_alias_warehous.text = wh.name.toAlias()*/
                itemView.tv_warehouse_name.text = wh.name
            }

        }
    }

    fun updateData(newWarehouses: List<Warehouse>?) {
        warehouses = newWarehouses
        notifyDataSetChanged()
    }
}