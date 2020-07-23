package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.extensions.*
import kotlinx.android.synthetic.main.list_item_product_price_group.view.*

class ListProductPriceGroupAdapter(private var product: List<Product>? = arrayListOf()): RecyclerView.Adapter<ListProductPriceGroupAdapter.ProductViewHolder>(){

    var listener: (Product) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_product_price_group, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return product?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(product?.get(position), listener)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dataMultiple = mutableListOf<DataSpinner>()
        private var isMultiple: String = "0"
//        private val numberSparator = id.sisi.postoko.utils.NumberSeparator()

        fun bind(value: Product?, listener: (Product) -> Unit = {}) {

            dataMultiple.add(DataSpinner("Tidak", "0"))
            dataMultiple.add(DataSpinner("Ya", "1"))
            itemView.sp_multiple.adapter =
                MySpinnerAdapter(itemView.context, android.R.layout.simple_list_item_1, dataMultiple)

            itemView.sp_multiple.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    isMultiple = dataMultiple[position].value
                }
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }

            itemView.tv_product_name.text = value?.product_name
            itemView.tv_product_code.text = value?.product_code
            itemView.tv_alias_product?.text = getAlias(value?.product_name)

            itemView.et_price.addTextChangedListener(NumberSeparator(itemView.et_price))
            itemView.et_price_credit.addTextChangedListener(NumberSeparator(itemView.et_price_credit))
            /*itemView.et_price_credit.addTextChangedListener(numberSparator.onTextChangedListener(itemView.et_price_credit))
            itemView.et_price.addTextChangedListener(numberSparator.onTextChangedListener(itemView.et_price))*/

            if (value?.price_kredit != null){
                itemView.et_price_credit.setText(value.price_kredit.toString())
            }
            itemView.et_price.setText(value?.price.toString())
            itemView.et_min_order.setText(value?.min_order.toString())
            itemView.sp_multiple.setIfExist(value?.is_multiple.toString())

            if (value?.isCollapse!!){
                itemView.expandable_layout.expand(true)
            }else{
                itemView.expandable_layout.collapse(true)
            }

            itemView.expand_button.setOnClickListener {
                value.isCollapse = !(value.isCollapse)
                if (value.isCollapse){
                    itemView.iv_arrow_up.visible()
                    itemView.iv_arrow_down.gone()
                    itemView.expandable_layout.expand(true)
                }else{
                    itemView.et_price_credit.clearFocus()
                    itemView.et_price.clearFocus()
                    itemView.et_min_order.clearFocus()
                    itemView.iv_arrow_down.visible()
                    itemView.iv_arrow_up.gone()
                    itemView.expandable_layout.collapse(true)
                }
            }

            itemView.btn_action_submit.setOnClickListener {
                val mandatory = listOf<EditText>(itemView.et_price)
                if (!mandatory.validation()) {
                    return@setOnClickListener
                }
                val newProduct = Product(
                    id = value.id,
                    price = itemView.et_price.tag.toString(),
                    price_kredit =  itemView.et_price_credit.tag.toString().toInt(),
                    min_order =  itemView.et_min_order.text.toString().toInt(),
                    code = value.product_code ?: "",
                    name = value.product_name ?: "",
                    is_multiple = isMultiple.toInt(),
                    priceGroup_id = 1
                )
                listener(newProduct)
            }
        }

        private fun getAlias(name: String?): String {
            if (name.isNullOrEmpty()) return "#"
            if (name.length == 1) return name.toUpper()
            return name.toUpper().substring(0, 2)
        }
    }

    fun updateSalesData(newMasterData: List<Product>?) {
        product = newMasterData
        notifyDataSetChanged()
    }
}