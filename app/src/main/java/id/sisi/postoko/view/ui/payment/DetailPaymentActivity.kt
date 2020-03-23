package id.sisi.postoko.view.ui.payment

import android.os.Bundle
import id.sisi.postoko.R
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.view.BaseActivity

class DetailPaymentActivity : BaseActivity() {

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