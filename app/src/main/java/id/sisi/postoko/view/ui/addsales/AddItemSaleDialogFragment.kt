package id.sisi.postoko.view.ui.addsales

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.*
import kotlinx.android.synthetic.main.dialog_fragment_item_add_sale.*

class AddItemSaleDialogFragment(var product: Product): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        return inflater.inflate(R.layout.dialog_fragment_item_add_sale, container, false)
    }
    var listenerAdd: (Product) -> Unit = {}
    var listenerRemove: (Product) -> Unit = {}
    private val myDialog = MyDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_alias_product.text = product.name.toAlias()
        tv_product_name.text = product.name
        tv_product_price.text = product.price?.toCurrencyID()

        et_sale_item_qty.onChange {
            val strQty = et_sale_item_qty.text.toString()
            if (!TextUtils.isEmpty(strQty))
                product.orderQty  = strQty.toInt()
        }

        et_sale_item_qty.setText(product.orderQty.toString())

        iv_plus.setOnClickListener {
            product.orderQty += 1
            et_sale_item_qty.setText(product.orderQty.toString())
        }

        iv_minus.setOnClickListener {
            val qty = product.orderQty - 1
            if (qty < 1){
                myDialog.alert(getString(R.string.txt_alert_must_more_than_one), context)
            }else{
                product.orderQty = qty
                et_sale_item_qty.setText(product.orderQty.toString())
            }
        }

        if (!product.isSelected){
            btn_remove_to_cart.gone()
        }

        btn_remove_to_cart.setOnClickListener {
            removeItemCart(product)
        }


        btn_add_to_cart.setOnClickListener {
            if (product.orderQty < 1){
                myDialog.alert(getString(R.string.txt_alert_must_more_than_one), context)
            }else{
                product.isSelected = true
                listenerAdd(product)
                this.dismiss()
            }
        }
    }

    private fun removeItemCart(product: Product) {
        myDialog.confirmation(getString(R.string.txt_notif_remove_cart),context)
        myDialog.listenerPositif={
            product.orderQty = 0
            product.isSelected = false
            listenerRemove(product)
            this.dismiss()
        }
    }
}