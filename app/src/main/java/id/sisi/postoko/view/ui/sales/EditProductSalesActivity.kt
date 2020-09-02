package id.sisi.postoko.view.ui.sales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import id.sisi.postoko.R
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.utils.KEY_MESSAGE
import id.sisi.postoko.utils.KEY_SALE_ITEM
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toCurrencyID

import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_product_sales.*
import kotlinx.android.synthetic.main.content_edit_product_sales.*
import kotlinx.android.synthetic.main.payment_add_sale_fragment.*
import java.util.*

class EditProductSalesActivity : BaseActivity() {
    var unitPrice = 0.0
    private var myDialog = MyDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product_sales)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val saleItem = intent?.getParcelableExtra<SaleItem>(KEY_SALE_ITEM)
        val position: Int? = intent?.getIntExtra("position", 0)

        unitPrice = saleItem?.unit_price ?: 0.0
        toolbar_title.text = saleItem?.product_name?.toUpperCase(Locale.getDefault())
        et_qty_produk_edit_produk_sale.addTextChangedListener(NumberSeparator(et_qty_produk_edit_produk_sale))
        et_real_unit_price_edit_produk_add_sale.addTextChangedListener(NumberSeparator(et_real_unit_price_edit_produk_add_sale))
        et_discount_edit_produk_add_sale.addTextChangedListener(NumberSeparator(et_discount_edit_produk_add_sale))

        et_qty_produk_edit_produk_sale.setText(saleItem?.quantity?.toInt().toString())
        et_real_unit_price_edit_produk_add_sale.setText(saleItem?.real_unit_price?.toInt().toString())
        et_real_unit_price_edit_produk_add_sale.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                setUpResult()
            }
        })
        et_discount_edit_produk_add_sale.setText(saleItem?.discount?.toString())
        et_discount_edit_produk_add_sale.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                setUpResult()
            }
        })


        tv_tax_edit_produk_add_sale.text = saleItem?.tax?.toCurrencyID()
        tv_unit_price_edit_produk_add_sale.text = unitPrice.toCurrencyID()
        btn_edit_product_add_sale.setOnClickListener {

            if (unitPrice <= 0){
                myDialog.alert("Satuan Harga Tidak Boleh 0 Atau Kosong" as String, this)
            }else{
                val newSaleItems = setNewSaleItems()

                val returnIntent = Intent()
                returnIntent.putExtra(KEY_SALE_ITEM, newSaleItems)
                returnIntent.putExtra("position", position)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }

        }
    }

    private fun setUpResult() {
        val strDisc = et_discount_edit_produk_add_sale.text.toString().replace(",".toRegex(), "")
        val strRealUnitPrice = et_real_unit_price_edit_produk_add_sale.text.toString().replace(",".toRegex(), "")
        var disc = 0.0
        if (!TextUtils.isEmpty(strDisc)){
            disc = strDisc.toDouble()
        }
        if (!TextUtils.isEmpty(strRealUnitPrice)){
            unitPrice = strRealUnitPrice.toDouble().minus(disc)
        }

        tv_unit_price_edit_produk_add_sale.text = unitPrice.toCurrencyID()
    }

    private fun setNewSaleItems(): SaleItem {
        val newSaleItems = SaleItem()
        if (et_discount_edit_produk_add_sale?.text?.toString() != "") {
            newSaleItems.discount = et_discount_edit_produk_add_sale?.tag?.toString()?.toInt()
        }
        newSaleItems.unit_price = unitPrice
        newSaleItems.real_unit_price = et_real_unit_price_edit_produk_add_sale?.tag?.toString()?.toDouble()
        newSaleItems.quantity = et_qty_produk_edit_produk_sale?.tag?.toString()?.toDouble()
        return newSaleItems
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
