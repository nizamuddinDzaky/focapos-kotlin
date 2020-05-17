package id.sisi.postoko.view.ui.addsales

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListCartAddSaleAdapter
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setupFullHeight
import kotlinx.android.synthetic.main.dialog_syncron_master_customer.*
import kotlinx.android.synthetic.main.fragment_bottom_cart_add_sale.*

class BottomSheetCartAddSaleFragment: BottomSheetDialogFragment(), ListCartAddSaleAdapter.OnClickListenerInterface {
    var position: Int? = null
    private lateinit var adapterCart: ListCartAddSaleAdapter
    private var listProduct: List<Product> = arrayListOf()
    var listener: () -> Unit = {}

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
        (activity as AddSaleActivity?)?.listProdcut.let {
            if (it != null) {
                listProduct = it
                setupData()
            }
        }
        logE("wahaiii")

        btn_close.setOnClickListener {
            this.dismiss()
        }
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
        adapterCart.notifyDataSetChanged()
    }

    override fun onClickMinus(product: Product?) {
        val index = listProduct.indexOf(product)
        val quantity = listProduct[index].sale_qty.minus(1)
        if (quantity > 0){
            listProduct[index].sale_qty = listProduct[index].sale_qty.minus(1)
            adapterCart.notifyDataSetChanged()
        }else{
            removeItemCart(product)
        }
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

    private fun removeItemCart(product: Product?) {
        val dialog = context?.let { Dialog(it, R.style.MyCustomDialogFullScreen) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_syncron_master_customer)
        dialog?.tv_message?.text = Html.fromHtml(getString(R.string.txt_notif_remove_cart))

        dialog?.tv_cancel?.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.tv_sure?.setOnClickListener {
            product?.sale_qty = 0
            product?.isSelected = false
            product?.let { prod -> adapterCart.removeData(prod) }
            dialog.dismiss()
        }
        dialog?.show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        /*(activity as AddSaleActivity?)?.setUpBadge()*/
        listener()
//        (parentFragment as AddItemAddSaleFragment).updateRecycleView()
    }

    companion object {
        var listener: () -> Unit = {}
        fun show(
            fragmentManager: FragmentManager
        ) {

            val bottomSheetFragment = BottomSheetCartAddSaleFragment()
            bottomSheetFragment.listener={
                listener()
            }
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}