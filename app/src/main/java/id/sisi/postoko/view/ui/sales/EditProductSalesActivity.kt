package id.sisi.postoko.view.ui.sales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import id.sisi.postoko.R
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.utils.KEY_SALE_ITEM
import id.sisi.postoko.utils.NumberSeparator

import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_product_sales.*
import kotlinx.android.synthetic.main.content_edit_product_sales.*
import java.util.*

class EditProductSalesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product_sales)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val saleItem = intent?.getParcelableExtra<SaleItem>(KEY_SALE_ITEM)
        val position: Int? = intent?.getIntExtra("position", 0)

        toolbar_title.text = saleItem?.product_name?.toUpperCase(Locale.getDefault())
        et_qty_produk_edit_produk_sale.addTextChangedListener(NumberSeparator(et_qty_produk_edit_produk_sale))
        et_unit_price_edit_produk_add_sale.addTextChangedListener(NumberSeparator(et_unit_price_edit_produk_add_sale))
        et_discount_edit_produk_add_sale.addTextChangedListener(NumberSeparator(et_discount_edit_produk_add_sale))

        et_qty_produk_edit_produk_sale.setText(saleItem?.quantity?.toInt().toString())
        et_unit_price_edit_produk_add_sale.setText(saleItem?.unit_price?.toInt().toString())
        et_discount_edit_produk_add_sale.setText(saleItem?.discount?.toString())

        btn_edit_product_add_sale.setOnClickListener {
            val newSaleItems = setNewSaleItems()

            val returnIntent = Intent()
            returnIntent.putExtra(KEY_SALE_ITEM, newSaleItems)
            returnIntent.putExtra("position", position)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun setNewSaleItems(): SaleItem {
        val newSaleItems = SaleItem()
        if (et_discount_edit_produk_add_sale?.text?.toString() != "") {
            newSaleItems.discount = et_discount_edit_produk_add_sale?.tag?.toString()?.toInt()
        }
        newSaleItems.unit_price = et_unit_price_edit_produk_add_sale?.tag?.toString()?.toDouble()
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
