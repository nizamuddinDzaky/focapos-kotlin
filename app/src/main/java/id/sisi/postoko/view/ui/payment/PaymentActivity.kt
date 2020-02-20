package id.sisi.postoko.view.ui.payment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R

class PaymentActivity : AppCompatActivity(){
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