package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.setDefault
import id.sisi.postoko.utils.extensions.toUpper
import id.sisi.postoko.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter.view.*
import kotlinx.android.synthetic.main.list_item_product_price_group.view.*

class ListProductPriceGroup(private var customer: List<Customer>? = arrayListOf()): RecyclerView.Adapter<ListProductPriceGroup.ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_product_price_group, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return customer?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(customer?.get(position))
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dataMultiple = mutableListOf<DataSpinner>()

        fun bind(value: Customer?) {

            dataMultiple.add(DataSpinner("Tidak", "0"))
            dataMultiple.add(DataSpinner("Ya", "1"))
            itemView.sp_multiple.adapter =
                MySpinnerAdapter(itemView.context, android.R.layout.simple_list_item_1, dataMultiple)
            itemView.expand_text.text = value?.company
            itemView.tv_alias_product?.text = getAlias(value?.company)
            if (value?.isSelected!!){
                itemView.expandable_layout.expand(true)
            }else{
                itemView.expandable_layout.collapse(true)
            }

            itemView.expand_button.setOnClickListener {
                
                value.isSelected = !(value.isSelected)
                if (value.isSelected){
                    itemView.iv_arrow_up.visible()
                    itemView.iv_arrow_down.gone()
                    itemView.expandable_layout.expand(true)
                }else{
                    itemView.iv_arrow_down.visible()
                    itemView.iv_arrow_up.gone()
                    itemView.expandable_layout.collapse(true)
                }
            }
        }

        private fun getAlias(name: String?): String {
            if (name.isNullOrEmpty()) return "#"
            if (name.length == 1) return name.toUpper()
            return name.toUpper().substring(0, 2)
        }
    }

    fun updateSalesData(newMasterData: List<Customer>?) {
        customer = newMasterData
        notifyDataSetChanged()
    }
}