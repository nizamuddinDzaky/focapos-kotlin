package id.sisi.postoko.view.ui.customergroup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.utils.KEY_CUSTOMER_GROUP
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.ui.pricegroup.CustomerPriceGroupPagerAdapter
import kotlinx.android.synthetic.main.activity_customer_customer_group.*

class AddCustomerToCustomerGoupActivity : BaseActivity() {
    private val mCustomer = CustomersCustomerGroupFragment.newInstance()
    private val mCartCustomer = CartCustomerToCGFragment.newInstance()
    var firstListCustomer = listOf(
        Customer(name = "masih"),
        Customer(name = "dalam"),
        Customer(name = "uji"),
        Customer(name = "coba")
    )

    var customerGroup: CustomerGroup = CustomerGroup(id = "0", name = "ForcaPoS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_customer_group)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = getString(R.string.txt_title_price_group)
        supportActionBar?.subtitle = getString(R.string.txt_add_remove_customer)
        intent?.getParcelableExtra<CustomerGroup>(KEY_CUSTOMER_GROUP)?.let {
            customerGroup = it
        }

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
            fragmentActivity: FragmentActivity,
            customerGroup: CustomerGroup
        ) {
            val page = Intent(fragmentActivity, AddCustomerToCustomerGoupActivity::class.java)
            page.putExtra(KEY_CUSTOMER_GROUP, customerGroup)
            fragmentActivity.startActivity(page)
        }
    }
}