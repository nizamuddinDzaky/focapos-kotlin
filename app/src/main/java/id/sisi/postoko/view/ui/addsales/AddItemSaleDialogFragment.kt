package id.sisi.postoko.view.ui.addsales

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toCurrencyID
import kotlinx.android.synthetic.main.dialog_fragment_item_add_sale.*
import kotlinx.android.synthetic.main.dialog_syncron_master_customer.*

class AddItemSaleDialogFragment(var product: Product): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_item_add_sale, container, false)
    }
    var listenerAdd: (Product) -> Unit = {}
    var listenerRemove: (Product) -> Unit = {}
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

        if (!product.isSelected){
            btn_remove_to_cart.gone()
        }

        btn_remove_to_cart.setOnClickListener {
            removeItemCart(product)
        }

        btn_add_to_cart.setOnClickListener {
            product.isSelected = true
            listenerAdd(product)
            this.dismiss()
        }
    }

    private fun removeItemCart(product: Product) {
        val dialog = context?.let { Dialog(it, R.style.MyCustomDialogFullScreen) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_syncron_master_customer)
        dialog?.tv_message?.text = Html.fromHtml(getString(R.string.txt_notif_remove_cart))

        dialog?.tv_cancel?.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.tv_sure?.setOnClickListener {
            product.sale_qty = 0
            product.isSelected = false
            dialog.dismiss()
            listenerRemove(product)
            this.dismiss()
        }
        dialog?.show()
    }


}