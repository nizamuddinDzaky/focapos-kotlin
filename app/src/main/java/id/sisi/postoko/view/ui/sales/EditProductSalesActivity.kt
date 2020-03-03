package id.sisi.postoko.view.ui.sales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R
import id.sisi.postoko.model.SaleItem
import kotlinx.android.synthetic.main.activity_edit_product_sales.*
import kotlinx.android.synthetic.main.content_edit_product_sales.*
import java.util.*

class EditProductSalesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product_sales)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val saleItem = intent?.getParcelableExtra<SaleItem>("sale_item")
        val position : Int? = intent?.getIntExtra("position", 0)

        toolbar_title.text = saleItem?.product_name?.toUpperCase(Locale.getDefault())
        et_qty_produk_edit_produk_sale.setText(saleItem?.quantity?.toInt().toString())
        et_unit_price_edit_produk_add_sale.setText(saleItem?.unit_price?.toInt().toString())
        et_discount_edit_produk_add_sale.setText(saleItem?.discount.toString())

        btn_edit_product_add_sale.setOnClickListener {
            val newSaleItems = setNewSaleItems()

            val returnIntent = Intent()
            returnIntent.putExtra("new_sale_item", newSaleItems)
            returnIntent.putExtra("position", position)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun setNewSaleItems(): SaleItem {
        val newSaleItems = SaleItem()
        newSaleItems.discount = et_discount_edit_produk_add_sale?.text?.toString()?.toInt()
        newSaleItems.unit_price = et_unit_price_edit_produk_add_sale?.text?.toString()?.toDouble()
        newSaleItems.quantity = et_qty_produk_edit_produk_sale?.text?.toString()?.toDouble()
        return newSaleItems
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
