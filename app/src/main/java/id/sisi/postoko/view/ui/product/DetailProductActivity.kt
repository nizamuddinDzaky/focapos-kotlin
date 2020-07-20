package id.sisi.postoko.view.ui.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R
import id.sisi.postoko.view.ui.customer.CustomerPagerAdapter
import kotlinx.android.synthetic.main.activity_detail_product.*

class DetailProductActivity: AppCompatActivity() {

    private val pages = listOf(
        DetailProductFragment(),
        QuantityProductFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_product)
        setSupportActionBar(toolbar_add_customer)
        supportActionBar?.title = null

        main_view_pager?.let {
            it.adapter = CustomerPagerAdapter(supportFragmentManager, pages)
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }
}