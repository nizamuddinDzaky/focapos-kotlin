package id.sisi.postoko.view.ui.addsales

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.KEY_INDEX_ITEM_ADD_SALE
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.utils.extensions.toNumberID
import kotlinx.android.synthetic.main.fragment_bottom_sheet_update_item_add_sale.*

class BottomSheetUpdateItemAddSaleFragment: BottomSheetDialogFragment() {

    private var index: Int = 0
    private var listProduct: List<Product> = arrayListOf()

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
        return inflater.inflate(R.layout.fragment_bottom_sheet_update_item_add_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        index = arguments?.getInt(KEY_INDEX_ITEM_ADD_SALE) ?: 0

        listProduct = (activity as AddSaleActivity).listProduct
        val product = listProduct[index]
        setupUI(product)

        btn_action_submit.setOnClickListener {
            setUpNewProduct()
        }

        btn_close.setOnClickListener {
            this.dismiss()
        }
    }

    private fun setUpNewProduct() {
        listProduct[index].price = et_unit_price.text.toString().toInt()
        listProduct[index].orderQty = et_qty_produk.text.toString().toInt()
        listProduct[index].discount = et_discount.text.toString().toInt()
        listProduct[index].code = et_product_serial_number.text.toString()
        this.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as BottomSheetCartAddSaleFragment).setupData()
    }

    private fun setupUI(product: Product?) {
        tv_title_bottom_sheet.text = product?.name
        et_product_serial_number.setText(product?.code)
        et_qty_produk.setText(product?.orderQty?.toNumberID())
        et_discount.setText(product?.discount.toString())
        et_unit_price.setText(product?.price.toString())
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            index: Int
        ) {
            val bottomSheetFragment = BottomSheetUpdateItemAddSaleFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_INDEX_ITEM_ADD_SALE, index)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}