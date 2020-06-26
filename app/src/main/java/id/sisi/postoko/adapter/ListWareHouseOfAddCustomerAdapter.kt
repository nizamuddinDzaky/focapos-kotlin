package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.toAlias
import kotlinx.android.synthetic.main.list_warehouse_form_customer.view.*


class ListWareHouseOfAddCustomerAdapter (
    private var warehouses: List<Warehouse>? = listOf()
) : RecyclerView.Adapter<ListWareHouseOfAddCustomerAdapter.WarehouseViewHolder>() {

    var listenerItem: OnClickListenerInterface? = null
    private var lastSelectedPosition = -1
    var lastID = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_warehouse_form_customer, parent, false)

        return WarehouseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return warehouses?.size ?: 0
    }

    override fun onBindViewHolder(holder: WarehouseViewHolder, position: Int) {
        holder.bind(warehouses?.get(position), listenerItem)
    }

    class WarehouseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            warehouse: Warehouse?,
            listenerItem: OnClickListenerInterface?
        ) {
            warehouse?.let {wh ->
                itemView.tv_warehouse_name.text  = wh.name
                itemView.tv_alias_warehous.text = wh.name.toAlias()
                when(wh.isSelected){
                    true -> {
                        itemView.cb_select_warehouse.isChecked = true
                    }
                    else -> {
                        itemView.cb_select_warehouse.isChecked = false
                    }
                }

                itemView.cb_select_warehouse.setOnCheckedChangeListener { buttonView, isChecked ->
                    listenerItem?.onClickSelected(wh, isChecked)
                }

                when(wh.isDefault){
                    true ->{
                        itemView.cb_default_warehouse.isChecked = true
                    }
                    else -> {
                        itemView.cb_default_warehouse.isChecked = false
                    }
                }

                itemView.cb_default_warehouse.setOnClickListener {
                    listenerItem?.onClickDefault(wh)
                }

            }

        }
    }

    fun updateData(newWarehouses: List<Warehouse>?) {
        warehouses = newWarehouses
        notifyDataSetChanged()
    }

    interface OnClickListenerInterface {
        fun onClickSelected(warehouse: Warehouse, isSelected: Boolean)
        fun onClickDefault(warehouse: Warehouse)
    }
}