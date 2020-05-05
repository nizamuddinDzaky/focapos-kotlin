package id.sisi.postoko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toCurrencyID
import kotlinx.android.synthetic.main.list_master_produk.view.*

class ListMasterProdukAdapter (
    private var products: List<Product>? = listOf(),
    var listener: (Product?) -> Unit = {},
    private var listenerEdit: (Product?) -> Unit = {}
) : RecyclerView.Adapter<ListMasterProdukAdapter.ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_master_produk, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products?.get(position), listener, listenerEdit)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product? , listener: (Product?) -> Unit, listenerEdit: (Product?) -> Unit = {}) {
            itemView.tv_alias_product.text = product?.name.toAlias()
            itemView.tv_product_name.text = product?.name
            itemView.tv_product_price.text = product?.price?.toCurrencyID()
        }
    }

    fun updateData(newProduct: List<Product>?) {
        products = newProduct
        notifyDataSetChanged()
    }
}