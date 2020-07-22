package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.toNumberID
import kotlinx.android.synthetic.main.list_qty_warehouse.view.*

class QuantityWarehouseAdapter(private var qtyWarehouse: List<Warehouse>? = listOf()): RecyclerView.Adapter<QuantityWarehouseAdapter.QuantityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuantityViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_qty_warehouse, parent, false)

        return QuantityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return qtyWarehouse?.size ?: 0
    }

    override fun onBindViewHolder(holder: QuantityViewHolder, position: Int) {
        holder.bind(qtyWarehouse?.get(position))
    }

    class QuantityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(qtyWarehouse: Warehouse?) {
            itemView.tv_warehouse_name.text = qtyWarehouse?.name
            itemView.tv_qty.text = qtyWarehouse?.quantity?.toNumberID()
            itemView.tv_qty_booking.text = qtyWarehouse?.quantity_booking?.toNumberID()
        }
    }

    fun updateData(newProduct: List<Warehouse>?) {
        qtyWarehouse = newProduct
        notifyDataSetChanged()
    }
}