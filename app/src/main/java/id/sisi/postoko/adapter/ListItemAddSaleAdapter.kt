package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toCurrencyID
import kotlinx.android.synthetic.main.list_item_add_sale.view.*

class ListItemAddSaleAdapter(
    private var listProduct: List<Product>? = arrayListOf()
) : RecyclerView.Adapter<ListItemAddSaleAdapter.ProductViewHolder>() {

    var listener: (Product) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_add_sale, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProduct?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(listProduct?.get(position), listener)
    }

    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(
            product: Product?,
            listener: (Product) -> Unit = {}
        ) {

            val price = "${product?.price?.toCurrencyID()}/${product?.unit_name}"
            itemView.tv_product_name?.text = product?.name
            itemView.tv_product_price?.text = price
            itemView.tv_alias_product?.text = product?.name.toAlias()
            itemView.setOnClickListener {
                product?.let { item -> listener(item) }
            }
        }
    }

    fun updateData(newData: List<Product>){
        listProduct = newData
        notifyDataSetChanged()
    }
}