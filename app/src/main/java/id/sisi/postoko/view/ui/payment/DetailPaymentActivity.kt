package id.sisi.postoko.view.ui.payment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING

class DetailPaymentActivity : AppCompatActivity(){

    private var idSalesBooking: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_payment_activity)

        idSalesBooking = intent.getIntExtra(KEY_ID_SALES_BOOKING, 0)

        supportActionBar?.elevation = 0.0F

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    DetailPaymentFragment.newInstance()
                )
                .commitNow()
        }
    }
}