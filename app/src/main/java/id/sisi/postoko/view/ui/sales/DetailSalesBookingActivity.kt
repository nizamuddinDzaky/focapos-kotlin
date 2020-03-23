package id.sisi.postoko.view.ui.sales

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_DELIVERY_STATUS_SALE
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.KEY_TAG_SALES_ROOT_FRAGMENT
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.ui.delivery.DeliveryStatus
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailSalesBookingActivity : BaseActivity() {

    private var deliverStatusSale: String = ""
    var idSalesBooking: Int = 0
    var tempSale: Sales? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_sales_booking_activity)

        idSalesBooking = intent.getIntExtra(KEY_ID_SALES_BOOKING, 0)
        deliverStatusSale = intent.getStringExtra(KEY_DELIVERY_STATUS_SALE)
        supportActionBar?.elevation = 0.0F

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    DetailSalesBookingRootFragment.newInstance(),
                    KEY_TAG_SALES_ROOT_FRAGMENT
                )
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit_sale -> {
                val result = validationActionEditSale()
                if (!(result["type"] as Boolean)) {
                    AlertDialog.Builder(this@DetailSalesBookingActivity)
                        .setTitle("Alert")
                        .setMessage(result["message"] as String)
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                        }
                        .show()
                } else {
                    val intent = Intent(this, EditSaleActivity::class.java)
                    intent.putExtra("sale", tempSale)
                    intent.putParcelableArrayListExtra("sale_items",
                        tempSale?.saleItems?.let { ArrayList(it) })
                    startActivityForResult(intent, 1)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentByTag(KEY_TAG_SALES_ROOT_FRAGMENT)
            ?.onActivityResult(requestCode, resultCode, data)
    }

    private fun validationActionEditSale(): Map<String, Any> {
        var message = ""
        var cek = true
        if (tempSale?.sale_status == SaleStatus.values()[1].name.toLowerCase(Locale.getDefault())) {
            message += "- Status sale reseved\n"
            cek = false
        }
        if (deliverStatusSale.toLowerCase(Locale.ROOT) != DeliveryStatus.PENDING.toString()
                .toLowerCase(
                    Locale.ROOT
                )
        ) {
            message += "- Terdapat Delivery\n"
            cek = false
        }
        return mapOf("message" to message, "type" to cek)
    }
}