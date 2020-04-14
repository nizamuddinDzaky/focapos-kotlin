package id.sisi.postoko.view.ui.payment

import android.os.Bundle
import id.sisi.postoko.R
import id.sisi.postoko.view.BaseActivity

class PaymentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pembayaran_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    PaymentFragment.newInstance()
                )
                .commitNow()
        }
    }
}