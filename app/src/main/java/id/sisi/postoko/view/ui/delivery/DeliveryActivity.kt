package id.sisi.postoko.view.ui.delivery

import android.os.Bundle
import id.sisi.postoko.R
import id.sisi.postoko.view.BaseActivity

class DeliveryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pengiriman_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    DeliveryFragment.newInstance()
                )
                .commitNow()
        }
    }
}