package id.sisi.postoko.view.ui.purchase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.KEY_DEVICE_TYPE
import id.sisi.postoko.utils.KEY_SALE
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.ui.addsales.AddSaleActivity
import kotlinx.android.synthetic.main.payment_add_purchase_fragment.*
import kotlinx.android.synthetic.main.payment_add_purchase_fragment.btn_action_submit
import kotlinx.android.synthetic.main.payment_add_sale_fragment.*
import java.util.*


class PaymentAddPurchaseFragment: Fragment() {

    private var purchaseNote: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = null
        (activity as AddPurchaseActivity?)?.currentFragmentTag = TAG
        return inflater.inflate(R.layout.payment_add_purchase_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUI()

        et_discount_purchase.addTextChangedListener(NumberSeparator(et_discount_purchase))
        et_shipping_add_purchase.addTextChangedListener(NumberSeparator(et_shipping_add_purchase))
        rg_status_add_purchase?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_add_purchase.tag = radioButton.tag.toString()
        }

        rg_status_add_purchase?.check(rg_status_add_purchase?.get(0)?.id ?: 0)

        tv_edit_purchase_note.setOnClickListener {
            showPopUpNote(getString(R.string.txt_sale_note), purchaseNote ?: "")
        }

        tv_add_purchase_note.setOnClickListener {
            showPopUpNote(getString(R.string.txt_sale_note), purchaseNote ?: "")
        }
        btn_action_submit.setOnClickListener {
            actionSubmint()
        }
    }

    private fun actionSubmint() {
        val listSelected = ArrayList<Product>()
        (activity as AddPurchaseActivity).listProduct.forEach {
            if (it.isSelected)
                listSelected.add(it)
        }

        val purchaseItem =listSelected.map {
            return@map mutableMapOf(
                "product_id" to it.id,
                "price" to it.price.toString(),
                "quantity" to it.orderQty,
                "product_discount" to it.discount,
                "expiry" to it.expiry
            )
        }

        val body: MutableMap<String, Any> = mutableMapOf(
            "date" to ((activity as AddPurchaseActivity).date ?: ""),
            "warehouse_id" to ((activity as AddPurchaseActivity).idWarehouse ?: ""),
            "supplier_id" to ((activity as AddPurchaseActivity).idSupplier ?: ""),
            "status" to (rg_status_add_purchase?.tag?.toString() ?: ""),
            "shipping" to (et_shipping_add_purchase?.tag?.toString() ?: ""),
            "note" to ((activity as AddPurchaseActivity).purchaseNote ?: ""),
            "payment_term" to (et_top_add_purchase?.text.toString() ?: ""),
            "discount" to (et_discount_purchase?.tag?.toString() ?: ""),
            "acceptor" to (et_receiver_name_add_purchase?.text.toString() ?: ""),
            "device_type" to KEY_DEVICE_TYPE,
            "products" to purchaseItem,
            "no_si_spj" to (et_no_spj_add_purchase?.text.toString() ?: ""),
            "no_si_do" to (et_no_do_add_purchase?.text.toString() ?: ""),
            "no_si_billing" to (et_no_billing_add_purchase?.text.toString() ?: "")
        )

        (activity as AddPurchaseActivity).vmAddPurchase.postAddPurchase(body){
            finishAddPurchase()
        }
    }

    private fun finishAddPurchase() {
        val returnIntent = Intent()
        returnIntent.putExtra("purchase_status", rg_status_add_purchase.tag.toString())
        (activity as AddPurchaseActivity).setResult(Activity.RESULT_OK, returnIntent)
        (activity as AddPurchaseActivity).finish()
    }

    private fun setUpUI(){
        for (i in 0 until (rg_status_add_purchase?.childCount ?: 0)) {
            (rg_status_add_purchase?.get(i) as? RadioButton)?.tag =
                PurchaseStatus.values()[i].name.toLowerCase(
                    Locale.getDefault()
                )
        }
        setPurchaseNote()
    }

    private fun showPopUpNote(title: String, purchaseNote: String) {
        val dialog = MyDialog()
        dialog.note(title, purchaseNote, context)
        dialog.listenerPositifNote = {
            /*if (key == KEY_SALE){*/
                this.purchaseNote = it
                (activity as AddPurchaseActivity).purchaseNote = this.purchaseNote
                setPurchaseNote()
            /*}*/
        }
    }

    private fun setPurchaseNote() {
        if (TextUtils.isEmpty(purchaseNote)){
            tv_edit_purchase_note.gone()
            tv_add_purchase_note.visible()
            tv_purchase_note.text = getString(R.string.txt_not_set_note)
        }else{
            tv_purchase_note.text = purchaseNote
            tv_add_purchase_note.gone()
            tv_edit_purchase_note.visible()
        }
    }

    companion object {
        val TAG: String = PaymentAddPurchaseFragment::class.java.simpleName
        fun newInstance() =
            PaymentAddPurchaseFragment()
    }
}