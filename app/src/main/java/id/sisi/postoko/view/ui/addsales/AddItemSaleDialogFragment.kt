package id.sisi.postoko.view.ui.addsales

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.DialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.toAlias
import id.sisi.postoko.utils.extensions.toCurrencyID
import kotlinx.android.synthetic.main.dialog_fragment_item_add_sale.*

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
    private val alert = MyDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_alias_product.text = product.name.toAlias()
        tv_product_name.text = product.name
        tv_product_price.text = product.price.toCurrencyID()

        /*et_sale_item_qty.onChange {
            val strQty = et_sale_item_qty.text.toString()
            if (!TextUtils.isEmpty(strQty))
                product.sale_qty  = strQty.toInt()
        }*/

        et_sale_item_qty.setOnEditorActionListener(
            OnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null && event.action === KeyEvent.ACTION_DOWN && event.keyCode === KeyEvent.KEYCODE_ENTER
                ) {
                    if (event == null || !event.isShiftPressed) { // the user is done typing.
                        val imm =
                            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(getView()!!.windowToken, 0)
                        return@OnEditorActionListener true // consume.
                    }
                }
                false // pass on to other listeners.
            }
        )

        et_sale_item_qty.setText(product.sale_qty.toString())

        iv_plus.setOnClickListener {
            product.sale_qty += 1
            et_sale_item_qty.setText(product.sale_qty.toString())
        }

        iv_minus.setOnClickListener {
            product.sale_qty -= 1
            et_sale_item_qty.setText(product.sale_qty.toString())
        }

        if (!product.isSelected){
            btn_remove_to_cart.gone()
        }

        btn_remove_to_cart.setOnClickListener {
            removeItemCart(product)
        }


        btn_add_to_cart.setOnClickListener {
            if (product.sale_qty < 1){
                alert.alert(getString(R.string.txt_alert_must_more_than_one), context)
            }else{
                product.isSelected = true
                listenerAdd(product)
                this.dismiss()
            }
        }
    }

    private fun removeItemCart(product: Product) {
        alert.confirmation(getString(R.string.txt_notif_remove_cart),context)
        alert.listenerPositif={
            product.sale_qty = 0
            product.isSelected = false
            listenerRemove(product)
            this.dismiss()
        }
    }
}