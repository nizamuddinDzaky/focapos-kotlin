package id.sisi.postoko.view.ui.addsales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toCurrencyID
import kotlinx.android.synthetic.main.dialog_fragment_item_add_sale.*

class ItemAddSaleDialogFragment(var product: Product): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_item_add_sale, container, false)
    }
    var listener: (Product) -> Unit = {}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_alias_product.text = product.name.toAlias()
        tv_product_name.text = product.name
        tv_product_price.text = product.price.toCurrencyID()
        tv_sale_item_qty.text = product.sale_qty.toString()

        iv_plus.setOnClickListener {
            product.sale_qty += 1
            tv_sale_item_qty.text = product.sale_qty.toString()
        }

        iv_minus.setOnClickListener {
            product.sale_qty -= 1
            tv_sale_item_qty.text = product.sale_qty.toString()
        }

        btn_add_to_cart.setOnClickListener {
            listener(product)
            this.dismiss()
        }
    }

}