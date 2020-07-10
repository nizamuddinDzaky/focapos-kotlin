package id.sisi.postoko.view.ui.addsales

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListCartAddSaleAdapter
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.helper.findSaleFragmentByTag
import kotlinx.android.synthetic.main.fragment_bottom_cart_add_sale.*

class BottomSheetCartAddSaleFragment: BottomSheetDialogFragment(), ListCartAddSaleAdapter.OnClickListenerInterface {
    var position: Int? = null
    private lateinit var adapterCart: ListCartAddSaleAdapter
    private var listProduct: List<Product> = arrayListOf()
    private var btnClicked: Boolean = false
    var listener: (btnClicked: Boolean) -> Unit = {}
    private val myDialog = MyDialog()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_cart_add_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AddSaleActivity?)?.listProduct.let {
            if (it != null) {
                listProduct = it
                setupData()
            }
        }
        setUpTotal()

        btn_close.setOnClickListener {
            this.dismiss()
        }

        btn_action_submit.setOnClickListener {
            this.dismiss()
            (activity as AddSaleActivity?)?.switchFragment(findSaleFragmentByTag(PaymentAddSaleFragment.TAG))
        }
    }

    private fun setUpTotal(){
        tv_total_add_sale.text = (activity as AddSaleActivity).getTotal().toCurrencyID()
    }

    fun setupData() {
        setupDataCart()
        rv_cart_sale?.layoutManager = LinearLayoutManager(this.context)
        rv_cart_sale?.setHasFixedSize(false)
        rv_cart_sale?.adapter = adapterCart
    }

    private fun setupDataCart() {
        adapterCart = ListCartAddSaleAdapter()
        adapterCart.listenerProduct = this
        listProduct.forEach {product ->
            if(product.isSelected){
                adapterCart.insertData(product)
            }
        }
    }

    override fun onClickPlus(product: Product?) {
        val index = listProduct.indexOf(product)
        listProduct[index].sale_qty = listProduct[index].sale_qty.plus(1)
        setUpTotal()
        adapterCart.notifyDataSetChanged()
    }

    override fun onClickMinus(product: Product?) {
        val index = listProduct.indexOf(product)
        val quantity = listProduct[index].sale_qty.minus(1)
        if (quantity > 0){
            listProduct[index].sale_qty = quantity
            adapterCart.notifyDataSetChanged()
        }else{
            removeItemCart(product)
        }
        setUpTotal()
    }

    override fun onClickDelete(product: Product?) {
        removeItemCart(product)
    }

    override fun onClickEdit(product: Product?) {
        position = listProduct.indexOf(product)

        BottomSheetUpdateItemAddSaleFragment.show(
            childFragmentManager,
            position ?: 0
        )
    }

    override fun onChange(product: Product?) {
        myDialog.qty(product?.name ?: "",getString(R.string.txt_sale_quantity), product?.sale_qty ?: 0, context, product?.unit_name)
        myDialog.listenerPositifNote={ qty ->
            val index = listProduct.indexOf(product)
            if (TextUtils.isEmpty(qty)){
                myDialog.alert(getString(R.string.txt_alert_must_more_than_one), context)
            }else{
                listProduct[index].sale_qty = qty.toInt()
                adapterCart.notifyDataSetChanged()
            }
        }
    }

    private fun removeItemCart(product: Product?) {
        myDialog.confirmation(getString(R.string.txt_notif_remove_cart),context)
        myDialog.listenerPositif={
            product?.sale_qty = 0
            product?.isSelected = false
            product?.let { prod -> adapterCart.removeData(prod) }
            setUpTotal()
        }
        myDialog.listenerNegatif={
            adapterCart.notifyDataSetChanged()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener(btnClicked)
    }

    companion object {
        var listener: (btnClicked: Boolean) -> Unit = {}
        fun show(
            fragmentManager: FragmentManager
        ) {

            val bottomSheetFragment = BottomSheetCartAddSaleFragment()
            bottomSheetFragment.listener={
                listener(it)
            }
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}