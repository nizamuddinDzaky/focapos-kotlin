package id.sisi.postoko.view.ui.pricegroup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.activity_customer_price_group.*

class AddCustomerPriceGroupActivity : BaseActivity() {
    private val mCustomer = CustomerPriceGroupFragment.newInstance()
    private val mCartCustomer = CartCustomerToPGFragment.newInstance()
    var firstListCustomer = listOf(Customer(), Customer(), Customer(), Customer())
    var priceGroup: PriceGroup? = PriceGroup(name = "ForcaPoS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_price_group)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = getString(R.string.txt_title_price_group)
        supportActionBar?.subtitle = getString(R.string.txt_add_remove_customer)

        main_container?.gone()
        main_view_pager?.let {
            it.adapter = CustomerPriceGroupPagerAdapter(
                supportFragmentManager, mutableListOf(
                    mCustomer,
                    mCartCustomer
                )
            )
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }

    fun countInc(value: Customer) {
        mCartCustomer.addToAdapter(value)
        updateTab()
    }

    fun countDec(value: Customer) {
        mCustomer.addToAdapter(value)
        updateTab()
    }

    fun updateTab() {
        tabs_main_pagers?.getTabAt(1)?.text = mCartCustomer.tagName
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity
        ) {
            val page = Intent(fragmentActivity, AddCustomerPriceGroupActivity::class.java)
            fragmentActivity.startActivity(page)
        }
    }
}