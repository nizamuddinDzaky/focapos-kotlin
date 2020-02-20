package id.sisi.postoko.view.ui.sales

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R

class DetailSalesBookingActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_sales_booking_activity)

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