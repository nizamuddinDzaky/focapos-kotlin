package id.sisi.postoko.view.ui.customergroup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.customer.CustomerViewModel
import id.sisi.postoko.view.ui.pricegroup.ListCustomerCGToCartAdapter
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
    private lateinit var vmCustomer: CustomerViewModel
    private val progressBar = CustomProgressBar()

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
        vmCustomerGroup = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)
        vmCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
    }

    private fun setupDataCustomer(loadNew: Boolean = false) {
        if (loadNew) {
            vmCustomer.getListCustomers().observe(this, Observer {
                firstListCustomer = it ?: listOf()
                adapterCustomer.updateMasterData(firstListCustomer)
                setupDataCart()
            })

            vmCustomer.getListCustomer()
        }
    }

    private fun setupData() {
        rv_list_customer?.adapter = adapterCustomer
        rv_list_customer_cart?.adapter = adapterCart
        rv_list_customer?.addVerticalDivider()

        setupDataCustomer(true)
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
        if (listCustomerCart.isNotEmpty()){
            progressBar.show(this, "Silakan tunggu...")
            val listIdSelected: ArrayList<String> = arrayListOf()
            for (index in 0 until listCustomerCart.size){
                listIdSelected.add(listCustomerCart[index].id ?: "")
            }
            val body: MutableMap<String, Any> = mutableMapOf(
                "id_customer" to listIdSelected
            )
            vmCustomerGroup.postAddCustomerToCustoemrGroup(body, customerGroup.id){
                progressBar.dialog.dismiss()
                Toast.makeText(this, "" + it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    finish()
                }
            }
        }else{
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Silahkan Pilih Pelanggan Terlebih Dahulu")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    fun validation(customer: Customer) {
        if (customer.isSelected) {
            listCustomerCart.add(customer)
            adapterCart.updateMasterData(listCustomerCart)

            rv_list_customer_cart?.smoothScrollToPosition(adapterCart.itemCount)
        } else {
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
            fragmentActivity.startActivity(page)
        }
    }
}