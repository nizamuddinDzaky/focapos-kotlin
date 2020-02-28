package id.sisi.postoko.view.ui.sales

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING


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
}