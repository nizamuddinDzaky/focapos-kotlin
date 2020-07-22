package id.sisi.postoko.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.ui.product.DetailProductActivity
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.content_detail_customer.*
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
            itemView.tv_product_name.text = product?.name
            itemView.tv_product_price.text = product?.price?.toCurrencyID()
            if (product?.image.equals(DEFAULT_LOGO_PRODUCT)){
                itemView.layout_alias.visible()
                itemView.layout_thumb.gone()
                itemView.tv_alias_product.text = product?.name.toAlias()
            }else{
                itemView.layout_alias.gone()
                itemView.layout_thumb.visible()
                val loadImage = LoadImageFromUrl(itemView.iv_icon_product, itemView.context, R.drawable.toko2)
                loadImage.execute("${product?.image}")
            }
            itemView.setOnClickListener {
                val page = Intent(itemView.context, DetailProductActivity::class.java)
                page.putExtra(KEY_PRODUCT_ID, product?.id)
                itemView.context.startActivity(page)
            }

            itemView.tv_qty.text = product?.quantity?.toNumberID()
            itemView.tv_qty_booking.text = product?.quantity_booking?.toNumberID()
        }
    }

    fun updateData(newProduct: List<Product>?) {
        products = newProduct
        notifyDataSetChanged()
    }
}