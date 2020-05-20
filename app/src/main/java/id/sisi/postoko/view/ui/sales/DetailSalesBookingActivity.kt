package id.sisi.postoko.view.ui.sales

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.*
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.ui.delivery.DeliveryStatus
import kotlinx.android.synthetic.main.detail_good_received_activity.*
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailSalesBookingActivity : BaseActivity() {

    private var deliverStatusSale: String = ""
    var idSalesBooking: Int = 0
    var tempSale: Sales? = null
    var tempCustomer: Customer? = null
    private var alert = MyAlert()
    lateinit var vmSale: SaleBookingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_sales_booking_activity)
        setSupportActionBar(toolbar)
        idSalesBooking = intent.getIntExtra(KEY_ID_SALES_BOOKING, 0)
        deliverStatusSale = intent.getStringExtra(KEY_DELIVERY_STATUS_SALE)

        vmSale = ViewModelProvider(
            this,
            SaleBookingFactory(idSalesBooking)
        ).get(SaleBookingViewModel::class.java)

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
                if (!(result[KEY_VALIDATION_REST] as Boolean)) {
                    alert.alert(result[KEY_MESSAGE] as String, this)
                } else {
                    val intent = Intent(this, EditSaleActivity::class.java)
                    intent.putExtra(KEY_SALE, tempSale)
                    intent.putParcelableArrayListExtra(KEY_SALE_ITEM,
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
            message += "- ${getString(R.string.txt_sale_reserved)}\n"
            cek = false
        }
        if (deliverStatusSale.toLowerCase(Locale.ROOT) != DeliveryStatus.PENDING.toString()
                .toLowerCase(
                    Locale.ROOT
                )
        ) {
            message += "- ${getString(R.string.txt_alert_sale_has_delivery)}\n"
            cek = false
        }
        return mapOf(KEY_MESSAGE to message, KEY_VALIDATION_REST to cek)
    }
}