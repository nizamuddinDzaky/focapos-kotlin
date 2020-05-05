package id.sisi.postoko.view.ui.gr

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.utils.KEY_GOOD_RECEIVED
import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.detail_good_received_activity.*

class DetailGoodReceivedActivity : AppCompatActivity() {
    var mGoodReceived: GoodReceived? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_good_received_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            mGoodReceived = intent.getParcelableExtra(KEY_GOOD_RECEIVED)
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    DetailGoodReceivedFragment.newInstance()
                )
                .commitNow()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}