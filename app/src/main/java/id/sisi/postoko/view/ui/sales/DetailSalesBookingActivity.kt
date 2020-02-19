package id.sisi.postoko.view.ui.sales

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import kotlinx.android.synthetic.main.detail_sales_booking_activity.*

class DetailSalesBookingActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_sales_booking_activity)
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