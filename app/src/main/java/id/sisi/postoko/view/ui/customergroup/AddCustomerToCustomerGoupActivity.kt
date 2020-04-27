package id.sisi.postoko.view.ui.customergroup

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
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_CUSTOMER_GROUP
import id.sisi.postoko.utils.RC_ADD_CUSTOMER_TO_CG
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.pricegroup.BSFilterMemberPGandCG
import kotlinx.android.synthetic.main.activity_customer_customer_group.*

class AddCustomerToCustomerGoupActivity : BaseActivity() {
    private var firstListCustomer = listOf(
        Customer(name = "masih"),
        Customer(name = "dalam"),
        Customer(name = "uji"),
        Customer(name = "coba")
    )

    private var customerGroup: CustomerGroup = CustomerGroup(id = "0", name = "ForcaPoS")
    private lateinit var adapterCustomer: ListCustomerCGToCartAdapter<Customer>
    private lateinit var adapterCart: ListCartCGToCustomerAdapter<Customer>
    private var listCustomerCart = ArrayList<Customer>()
    private lateinit var vmCustomerGroup: CustomerGroupViewModel
    private val progressBar = CustomProgressBar()
    private var strFilter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_customer_group)
        setSupportActionBar(toolbar)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = null
        intent?.getParcelableExtra<CustomerGroup>(KEY_CUSTOMER_GROUP)?.let {
            customerGroup = it
            toolbar_subtitle.text = it.name
        }

        initView()
        setupData()
        setupAction()
    }

    private fun initView() {
        adapterCustomer = ListCustomerCGToCartAdapter(fragmentActivity = this)
        adapterCart = ListCartCGToCustomerAdapter(fragmentActivity = this)
        vmCustomerGroup = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)
    }

    private fun setupData() {
        rv_list_customer?.adapter = adapterCustomer
        rv_list_customer_cart?.adapter = adapterCart
        rv_list_customer?.addVerticalDivider()

        setupDataCustomer(true)
    }

    private fun setupDataCustomer(loadNew: Boolean = false) {
        if (loadNew) {
            vmCustomerGroup.getListCustomers().observe(this, Observer {
                firstListCustomer = it ?: listOf()
                adapterCustomer.updateMasterData(firstListCustomer)
                setupDataCart()
            })
            vmCustomerGroup.getListCustomerCustomerGroup(customerGroup.id)
        }else{
            adapterCustomer.updateMasterData(firstListCustomer)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDataCart() {
        for(index in firstListCustomer.indices){
            if (firstListCustomer[index].customer_group_id == customerGroup.id){
                firstListCustomer[index].isSelected = !firstListCustomer[index].isSelected
                listCustomerCart.add(firstListCustomer[index])
            }
        }
        tv_total_selected.text = "Pelanggan yang terpilih (${listCustomerCart.size})"
        adapterCart.updateMasterData(listCustomerCart)
    }

    private fun setupAction() {
        tv_select_all.setOnClickListener { selectUnselectAll(false) }
        tv_unselect_all.setOnClickListener { selectUnselectAll(true) }
        btn_action_submit?.setOnClickListener { actionSave() }
//        setupSearch()
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
        vmCustomerGroup.postAddCustomerToCustoemrGroup(body, customerGroup.id){
            progressBar.dialog.dismiss()
            Toast.makeText(this, "" + it["message"], Toast.LENGTH_SHORT).show()
            if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_blue_ic, menu)
        menu?.findItem(R.id.menu_action_search)/*?.let {
            search_view?.typeView = 0
            search_view?.setMenuItem(it)
        }*/
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

    @SuppressLint("SetTextI18n")
    fun validation(customer: Customer) {
        if (customer.isSelected) {
            adapterCart.insertData(customer)
            rv_list_customer_cart?.smoothScrollToPosition(0)
        } else {
            adapterCart.removeData(customer)
        }
        tv_total_selected.text = "Pelanggan yang terpilih (${listCustomerCart.size})"
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity,
            customerGroup: CustomerGroup
        ) {
            val page = Intent(fragmentActivity, AddCustomerToCustomerGoupActivity::class.java)
            page.putExtra(KEY_CUSTOMER_GROUP, customerGroup)
            fragmentActivity.startActivityForResult(page, RC_ADD_CUSTOMER_TO_CG)
        }
    }
}