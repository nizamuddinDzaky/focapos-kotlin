package id.sisi.postoko.view.ui.pricegroup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.RC_ADD_CUSTOMER_TO_PG
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.activity_customer_price_group.*
import kotlinx.android.synthetic.main.activity_customer_price_group.btn_action_submit
import kotlinx.android.synthetic.main.activity_customer_price_group.rv_list_customer
import kotlinx.android.synthetic.main.activity_customer_price_group.rv_list_customer_cart
import kotlinx.android.synthetic.main.activity_customer_price_group.toolbar
import kotlinx.android.synthetic.main.activity_customer_price_group.toolbar_subtitle

class AddCustomerPriceGroupActivity : BaseActivity() {
    private var strFilter: String? = null
    private var firstListCustomer = listOf(
        Customer(name = "masih"),
        Customer(name = "dalam"),
        Customer(name = "uji"),
        Customer(name = "coba")
    )
    private var listCustomerCart = mutableListOf<Customer>()
    private var priceGroup: PriceGroup? = PriceGroup(name = "ForcaPoS")
    private lateinit var vmPriceGroup: PriceGroupViewModel
    private lateinit var adapterCustomer: ListCustomerToCartAdapter<Customer>
    private lateinit var adapterCart: ListCartToCustomerAdapter<Customer>

    private val progressBar = CustomProgressBar()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_price_group)
        setSupportActionBar(toolbar)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = null
//        supportActionBar?.subtitle = getString(R.string.txt_add_remove_customer)
        intent?.getParcelableExtra<PriceGroup>(KEY_PRICE_GROUP)?.let {
            priceGroup = it
        }

        toolbar_subtitle.text = priceGroup?.name
        tv_total_selected.text = "Pelanggan yang terpilih (${listCustomerCart.size})"
        initView()
        setupData()
        setupAction()
    }

    private fun initView() {
        adapterCustomer = ListCustomerToCartAdapter(fragmentActivity = this)
        adapterCart = ListCartToCustomerAdapter(fragmentActivity = this)
        vmPriceGroup = ViewModelProvider(this).get(PriceGroupViewModel::class.java)
    }

    private fun setupData() {
        rv_list_customer?.adapter = adapterCustomer
        rv_list_customer_cart?.adapter = adapterCart
        rv_list_customer?.addVerticalDivider()
        setupDataCustomer(true)
    }

    private fun setupDataCustomer(loadNew: Boolean = false) {
        if (loadNew) {
            vmPriceGroup.getListCustomers().observe(this, Observer {
                firstListCustomer = it ?: listOf()
                adapterCustomer.updateMasterData(firstListCustomer)
                setupDataCart()
            })
            vmPriceGroup.getListCustomerPriceGroup(priceGroup?.id.toString())
        }else{
            adapterCustomer.updateMasterData(firstListCustomer)
        }
    }

    private fun setupDataCart() {
        for(index in firstListCustomer.indices){
            if (firstListCustomer[index].price_group_id == priceGroup?.id){
                firstListCustomer[index].isSelected = !firstListCustomer[index].isSelected
                validation(firstListCustomer[index])
            }
        }
        adapterCart.updateMasterData(listCustomerCart)
    }

    private fun setupAction() {
        tv_select_all.setOnClickListener { selectUnselectAll(false) }
        tv_unselect_all.setOnClickListener { selectUnselectAll(true) }
        btn_action_submit?.setOnClickListener { actionSave() }
        /*setupSearch()*/
    }

    private fun selectUnselectAll(flag: Boolean){
        for(index in firstListCustomer.indices){
            if (firstListCustomer[index].isSelected == flag){
                firstListCustomer[index].isSelected = !firstListCustomer[index].isSelected
                validation(firstListCustomer[index])
            }
        }
        adapterCustomer.updateMasterData(firstListCustomer)
    }

    /*private fun setupSearch() {
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
                logE("closed Search")
                actoionShowSearch(false)
            }

            override fun onFilter() {
                submitOnFilter()
            }
        })
    }*/

    /*private fun actoionShowSearch(isShow: Boolean) {
        if (!isShow) {
            setupDataCustomer()
        }
    }

    private fun submitOnFilter() {
        logE("testing")
    }*/

    private fun submitQuerySearch(newText: String) {
        val resultSearch = firstListCustomer.filter {
            val name = "${it.customer_company} (${it.customer_name})"
            return@filter name.contains(newText, true)
        }
        adapterCustomer.updateMasterData(resultSearch)
    }

    private fun actionSave() {
        progressBar.show(this, "Silakan tunggu...")
        val listIdSelected: ArrayList<String> = arrayListOf()
        for (index in 0 until listCustomerCart.size){
            listIdSelected.add(listCustomerCart[index].customer_id ?: "")
        }
        val body: MutableMap<String, Any> = mutableMapOf(
            "id_customer" to listIdSelected
        )
        vmPriceGroup.postAddCustomerToPriceGroup(body, priceGroup?.id.toString()){
            progressBar.dialog.dismiss()
            Toast.makeText(this, "" + it["message"], Toast.LENGTH_SHORT).show()
            if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
    
    @SuppressLint("SetTextI18n")
    fun validation(customer: Customer) {
        if (customer.isSelected) {
            listCustomerCart.add(customer)
            adapterCart.updateMasterData(listCustomerCart)
            rv_list_customer_cart?.smoothScrollToPosition(adapterCart.itemCount)
        } else {
            listCustomerCart.remove(customer)
            adapterCart.updateMasterData(listCustomerCart)

            adapterCustomer.notifyDataSetChanged()
        }
        tv_total_selected.text = "Pelanggan yang terpilih (${listCustomerCart.size})"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_blue_ic, menu)
        menu?.findItem(R.id.menu_action_search)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_action_search) {
            BSFilterMemberPGandCG.show(
                supportFragmentManager,
                strFilter
            )
            BSFilterMemberPGandCG.listener = {
                submitQuerySearch(it["filter"].toString())
                strFilter = it["filter"].toString()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity,
            priceGroup: PriceGroup
        ) {
            val page = Intent(fragmentActivity, AddCustomerPriceGroupActivity::class.java)
            page.putExtra(KEY_PRICE_GROUP, priceGroup)
            fragmentActivity.startActivityForResult(page, RC_ADD_CUSTOMER_TO_PG)
        }
    }
}