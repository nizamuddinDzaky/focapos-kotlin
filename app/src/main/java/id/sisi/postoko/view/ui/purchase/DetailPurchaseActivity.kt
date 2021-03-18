package id.sisi.postoko.view.ui.purchase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Purchases
import id.sisi.postoko.utils.*
import id.sisi.postoko.view.ui.sales.DetailSalesBookingRootFragment
import id.sisi.postoko.view.ui.sales.SaleBookingViewModel
import kotlinx.android.synthetic.main.detail_good_received_activity.*

class DetailPurchaseActivity : AppCompatActivity() {

    var purchaseStatus: String? = null
    var idPurchase: Int = 0
    var tempPurchase: Purchases? =null
    lateinit var vmPurchase: DetailPurchaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_purchase)
        setSupportActionBar(toolbar)
        idPurchase = intent.getIntExtra(KEY_ID_PURCHASE, 0)
        purchaseStatus = intent.getStringExtra(KEY_PURCHASE_STATUS)

        vmPurchase = ViewModelProvider(this).get(DetailPurchaseViewModel::class.java)
        supportActionBar?.elevation = 0.0F

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    DetailPurchaseRootFragment.newInstance(),
                    KEY_TAG_PURCHASE_ROOT_FRAGMENT
                )
                .commitNow()
        }
    }
}