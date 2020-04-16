package id.sisi.postoko.view.ui.customergroup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_CUSTOMER_GROUP
import id.sisi.postoko.utils.MySearchView
import id.sisi.postoko.utils.RC_ADD_CUSTOMER_TO_CG
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.activity_customer_customer_group.*
import kotlinx.android.synthetic.main.failed_load_data.*

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
                tv_swipe_down.gone()
                logE("size= ${it?.size}")
                if (it?.size ?: 0 == 0) {
                    val status = when(it?.size) {
                        0 -> "Belum ada Data"
                        else -> "Gagal Memuat Data"
                    }
                    tv_status_progress?.text = status
                    layout_status_progress?.visible()
                    rv_list_customer_cart.gone()
                    rv_list_customer.gone()
                } else {
                    layout_status_progress?.gone()
                    rv_list_customer_cart.visible()
                    rv_list_customer.visible()
                }

                adapterCustomer.updateMasterData(firstListCustomer)
                setupDataCart()
            })
            vmCustomerGroup.getListCustomerCustomerGroup(customerGroup.id)
        }else{
            adapterCustomer.updateMasterData(firstListCustomer)
        }
    }


    private fun setupDataCart() {
        for(index in firstListCustomer.indices){
            if (firstListCustomer[index].customer_group_id == customerGroup.id){
                firstListCustomer[index].isSelected = !firstListCustomer[index].isSelected
                validation(firstListCustomer[index])
            }
        }
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
        menuInflater.inflate(R.menu.menu_search, menu)
        menu?.findItem(R.id.menu_action_search)?.let {
            search_view?.typeView = 0
            search_view?.setMenuItem(it)
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun validation(customer: Customer) {
        if (customer.isSelected) {
            logE("asdsadsad")
            listCustomerCart.add(customer)
            adapterCart.updateMasterData(listCustomerCart)

            rv_list_customer_cart?.smoothScrollToPosition(adapterCart.itemCount)
        } else {
            logE("qwewqe")
            listCustomerCart.remove(customer)
            val index = listCustomerCart.indexOf(customer)

            adapterCart.notifyItemRemoved(index)
            adapterCart.updateMasterData(listCustomerCart)
        }
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