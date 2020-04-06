package id.sisi.postoko.view.ui.pricegroup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import id.sisi.postoko.R
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.DataCustomer
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.utils.helper.fromJson
import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.activity_customer_price_group.*
import kotlinx.android.synthetic.main.activity_customer_price_group2.*

class AddCustomerPriceGroupActivity : BaseActivity() {
    var firstListCustomer = listOf(
        Customer(name = "masih"),
        Customer(name = "dalam"),
        Customer(name = "uji"),
        Customer(name = "coba")
    )
    var listCustomerCart = mutableListOf<Customer>()
    var priceGroup: PriceGroup? = PriceGroup(name = "ForcaPoS")
    private lateinit var adapterCustomer: ListCustomerToCartAdapter<Customer>
    private lateinit var adapterCart: ListCartToCustomerAdapter<Customer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_price_group)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = getString(R.string.txt_title_price_group)
        supportActionBar?.subtitle = getString(R.string.txt_add_remove_customer)
        intent?.getParcelableExtra<PriceGroup>(KEY_PRICE_GROUP)?.let {
            priceGroup = it
        }

        initData()
        setupData()
        setupAction()
    }

    private fun initData() {
        adapterCustomer = ListCustomerToCartAdapter(fragmentActivity = this)
        adapterCart = ListCartToCustomerAdapter(fragmentActivity = this)
    }

    private fun setupData() {
        rv_list_customer?.adapter = adapterCustomer
        rv_list_customer_cart?.adapter = adapterCart
        rv_list_customer?.addVerticalDivider()

        val jsonHelper =
            Gson().fromJson<BaseResponse<DataCustomer>>(this, "DummyListCustomer.json")
        firstListCustomer = (jsonHelper.data?.list_customers) ?: listOf()
        adapterCustomer.updateMasterData(firstListCustomer)
//        adapterCart.updateMasterData(firstListCustomer)
        adapterCart.updateMasterData(listCustomerCart)
    }

    private fun setupAction() {
        btn_action_submit?.setOnClickListener { }
    }

    private fun toggle(count: Int) {
        if ((count > 1) or (count == 1 && rv_list_customer_cart.visibility == View.VISIBLE)) return
//        val animate = ScaleAnimation(1F, 1F, if (count == 0) 1F else 0F, if (count == 0) 0F else 1F)
//        animate.duration = 500
//        animate.fillAfter = false
//        rv_list_customer_cart?.startAnimation(animate)
//        if (count == 0) rv_list_customer_cart?.gone() else rv_list_customer_cart?.visible()
        val animation: Animation =
            AnimationUtils.loadAnimation(this, R.anim.slide_up)
        rv_list_customer_cart.startAnimation(animation)
    }

    fun validation(customer: Customer) {
        if (customer.isSelected) {
            adapterCart.addData(customer)
            rv_list_customer_cart?.smoothScrollToPosition(adapterCart.itemCount)
        } else {
            adapterCart.removeCustomerFromCart(customer)
            adapterCustomer.notifyDataSetChanged()
        }
        toggle(adapterCart.itemCount)
    }

    fun updateTab() {}
    fun countDec(customer: Customer) {}
    fun countInc(customer: Customer) {}

    companion object {
        fun show(
            fragmentActivity: FragmentActivity,
            priceGroup: PriceGroup
        ) {
            val page = Intent(fragmentActivity, AddCustomerPriceGroupActivity::class.java)
            page.putExtra(KEY_PRICE_GROUP, priceGroup)
            fragmentActivity.startActivity(page)
        }
    }
}

class AddCustomerPriceGroupActivity2 : BaseActivity() {
    private val mCustomer = CustomerPriceGroupFragment.newInstance()
    private val mCartCustomer = CartCustomerToPGFragment.newInstance()
    var firstListCustomer = listOf(
        Customer(name = "masih"),
        Customer(name = "dalam"),
        Customer(name = "uji"),
        Customer(name = "coba")
    )
    var priceGroup: PriceGroup? = PriceGroup(name = "ForcaPoS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_price_group)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = getString(R.string.txt_title_price_group)
        supportActionBar?.subtitle = getString(R.string.txt_add_remove_customer)
        intent?.getParcelableExtra<PriceGroup>(KEY_PRICE_GROUP)?.let {
            priceGroup = it
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
            priceGroup: PriceGroup
        ) {
            val page = Intent(fragmentActivity, AddCustomerPriceGroupActivity::class.java)
            page.putExtra(KEY_PRICE_GROUP, priceGroup)
            fragmentActivity.startActivity(page)
        }
    }
}