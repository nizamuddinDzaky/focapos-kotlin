package id.sisi.postoko.view.ui.sales

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.logE
import java.util.*
import kotlin.collections.ArrayList

class DetailSalesBookingActivity : AppCompatActivity(){

    var idSalesBooking: Int = 0
    var tempSale: Sales? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_sales_booking_activity)

        idSalesBooking = intent.getIntExtra(KEY_ID_SALES_BOOKING, 0)

        supportActionBar?.elevation = 0.0F

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    DetailSalesBookingRootFragment.newInstance()
                )
                .commitNow()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit_sale -> {
                if (tempSale?.sale_status == SaleStatus.values()[1].name.toLowerCase(Locale.getDefault())){
                    AlertDialog.Builder(this@DetailSalesBookingActivity)
                        .setTitle("Alert")
                        .setMessage("Status sale reseved")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                        }
                        .show()
                }else{
                    val intent = Intent(this, EditSaleActivity::class.java)
                    intent.putExtra("sale", tempSale)
                    intent.putParcelableArrayListExtra("sale_items",
                        tempSale?.saleItems?.let { ArrayList(it) })
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}