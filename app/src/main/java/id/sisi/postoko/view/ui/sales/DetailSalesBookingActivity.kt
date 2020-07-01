package id.sisi.postoko.view.ui.sales

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_DELIVERY_STATUS_SALE
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.KEY_SALE_STATUS
import id.sisi.postoko.utils.KEY_TAG_SALES_ROOT_FRAGMENT
import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.detail_good_received_activity.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailSalesBookingActivity : BaseActivity() {

    var deliverStatusSale: String = ""
    var idSalesBooking: Int = 0
    var tempSale: Sales? = null
    var tempCustomer: Customer? = null
    var saleStatus: String? = null

    lateinit var vmSale: SaleBookingViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_sales_booking_activity)
        setSupportActionBar(toolbar)
        idSalesBooking = intent.getIntExtra(KEY_ID_SALES_BOOKING, 0)
        saleStatus = intent.getStringExtra(KEY_SALE_STATUS)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentByTag(KEY_TAG_SALES_ROOT_FRAGMENT)
            ?.onActivityResult(requestCode, resultCode, data)
    }


}