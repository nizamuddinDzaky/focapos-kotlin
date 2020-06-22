package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toAlias
import kotlinx.android.synthetic.main.list_item_warehouse_customer.view.*

class ListWareHouseOfCustomerAdapter (
    private var warehouses: List<Warehouse>? = listOf(),
    var listener: (Warehouse?) -> Unit = {},
    private var listenerEdit: (Warehouse?) -> Unit = {}
) : RecyclerView.Adapter<ListWareHouseOfCustomerAdapter.WarehouseViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_warehouse_customer, parent, false)

        return WarehouseViewHolder(view)
    }

    override fun getItemCount(): Int {
        logE("opop : ${warehouses?.size}")
        return warehouses?.size ?: 0
    }

    override fun onBindViewHolder(holder: WarehouseViewHolder, position: Int) {
        holder.bind(warehouses?.get(position), listener, listenerEdit)
    }

    class WarehouseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(warehouse: Warehouse? , listener: (Warehouse?) -> Unit, listenerEdit: (Warehouse?) -> Unit = {}) {
            itemView.tv_warehouse_name.text  = warehouse?.name
            itemView.tv_alias_warehous.text = warehouse?.name.toAlias()
        }
    }

    fun updateData(newWarehouses: List<Warehouse>?) {
        warehouses = newWarehouses
        notifyDataSetChanged()
    }
}