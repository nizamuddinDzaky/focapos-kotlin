package id.sisi.postoko.view.ui.delivery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R

class DeliveryActivity : AppCompatActivity (){
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