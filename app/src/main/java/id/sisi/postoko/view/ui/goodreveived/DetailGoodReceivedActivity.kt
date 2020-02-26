package id.sisi.postoko.view.ui.goodreveived

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R

class DetailGoodReceivedActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_good_received_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,
                    DetailGoodReceivedFragment.newInstance()
                )
                .commitNow()
        }
    }
}