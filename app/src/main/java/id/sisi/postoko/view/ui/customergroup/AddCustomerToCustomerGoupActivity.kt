package id.sisi.postoko.view.ui.customergroup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import id.sisi.postoko.R
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.DataCustomer
import id.sisi.postoko.utils.KEY_CUSTOMER_GROUP
import id.sisi.postoko.utils.MySearchView
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.helper.fromJson
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.ui.pricegroup.CustomerPriceGroupPagerAdapter
import id.sisi.postoko.view.ui.pricegroup.ListCustomerCGToCartAdapter
import kotlinx.android.synthetic.main.activity_customer_customer_group.*
import kotlinx.android.synthetic.main.activity_customer_customer_group.rv_list_customer
import kotlinx.android.synthetic.main.activity_customer_customer_group.rv_list_customer_cart
import kotlinx.android.synthetic.main.activity_customer_price_group.*

class AddCustomerToCustomerGoupActivity : BaseActivity() {
    var firstListCustomer = listOf(
        Customer(name = "masih"),
        Customer(name = "dalam"),
        Customer(name = "uji"),
        Customer(name = "coba")
    )


    var customerGroup: CustomerGroup = CustomerGroup(id = "0", name = "ForcaPoS")
    private lateinit var adapterCustomer: ListCustomerCGToCartAdapter<Customer>
    private lateinit var adapterCart: ListCartCGToCustomerAdapter<Customer>
    var listCustomerCart = mutableListOf<Customer>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_customer_group)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = getString(R.string.txt_title_customer_group)
        supportActionBar?.subtitle = getString(R.string.txt_add_remove_customer)
        intent?.getParcelableExtra<CustomerGroup>(KEY_CUSTOMER_GROUP)?.let {
            customerGroup = it
        }

        initView()
        setupData()
        setupAction()
    }

    private fun initView() {
        adapterCustomer = ListCustomerCGToCartAdapter(fragmentActivity = this)
        adapterCart = ListCartCGToCustomerAdapter(fragmentActivity = this)
    }

    private fun setupDataCustomer(loadNew: Boolean = false) {
        if (loadNew) {
            val jsonHelper =
                Gson().fromJson<BaseResponse<DataCustomer>>(this, "DummyListCustomer.json")
            firstListCustomer = (jsonHelper.data?.list_customers) ?: listOf()
        }
        adapterCustomer.updateMasterData(firstListCustomer)
    }

    private fun setupData() {
        rv_list_customer?.adapter = adapterCustomer
        rv_list_customer_cart?.adapter = adapterCart
        rv_list_customer?.addVerticalDivider()

        setupDataCart()
        setupDataCustomer(true)
    }

    private fun setupDataCart() {
        adapterCart.updateMasterData(listCustomerCart)
    }

    private fun setupAction() {
        btn_action_submit?.setOnClickListener { actionSave() }
        setupSearch()
    }

    private fun setupSearch() {
        search_view?.setOnQueryTextListener(object : MySearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    submitQuerySearch(newText)
                }
                return false
            }
        })
        search_view?.setOnSearchViewListener(object : MySearchView.SearchViewListener {
            override fun onSearchViewShown() {
                actoionShowSearch(true)
            }

            override fun onSearchViewClosed() {
                actoionShowSearch(false)
            }

            override fun onFilter() {
                submitOnFilter()
            }
        })
    }

    private fun actoionShowSearch(isShow: Boolean) {
        if (!isShow) {
            setupDataCustomer()
        }
    }

    private fun submitOnFilter() {

    }

    private fun submitQuerySearch(newText: String) {
        val resultSearch = firstListCustomer.filter {
            val name = "${it.company} (${it.name})"
            return@filter name.contains(newText, true)
        }
        adapterCustomer.updateMasterData(resultSearch)
    }

    private fun actionSave() {

    }

    fun validation(customer: Customer) {
        if (customer.isSelected) {
            adapterCart.addData(customer)
            rv_list_customer_cart?.smoothScrollToPosition(adapterCart.itemCount)
        } else {
            adapterCart.removeCustomerFromCart(customer)
            adapterCustomer.notifyDataSetChanged()
        }
        //toggle(adapterCart.itemCount)
    }

    fun updateTab() {}

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