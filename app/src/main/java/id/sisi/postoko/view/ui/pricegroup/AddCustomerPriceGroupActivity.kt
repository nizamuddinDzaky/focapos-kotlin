package id.sisi.postoko.view.ui.pricegroup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.MySearchView
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.customer.CustomerViewModel
import kotlinx.android.synthetic.main.activity_customer_price_group.*

class AddCustomerPriceGroupActivity : BaseActivity() {
    var firstListCustomer = listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_price_group)
        setSupportActionBar(toolbar)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = getString(R.string.txt_title_price_group)
        supportActionBar?.subtitle = getString(R.string.txt_add_remove_customer)
        intent?.getParcelableExtra<PriceGroup>(KEY_PRICE_GROUP)?.let {
            priceGroup = it
        }

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
                logE("data: $it")
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
                logE("closed Search")
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
        logE("testing")
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
            vmPriceGroup.postAddCustomerToPriceGroup(body, priceGroup?.id.toString()){
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

//    private fun toggle(count: Int) {
//        if ((count > 1) or (count == 1 && rv_list_customer_cart.visibility == View.VISIBLE)) return
//        val animation: Animation =
//            AnimationUtils.loadAnimation(this, R.anim.slide_up)
//        rv_list_customer_cart.startAnimation(animation)
//    }

    fun validation(customer: Customer) {
        if (customer.isSelected) {
//            adapterCart.addData(customer)
            listCustomerCart.add(customer)
            adapterCart.updateMasterData(listCustomerCart)
            rv_list_customer_cart?.smoothScrollToPosition(adapterCart.itemCount)
        } else {
            listCustomerCart.remove(customer)
            adapterCart.updateMasterData(listCustomerCart)

            adapterCustomer.notifyDataSetChanged()
        }
        //toggle(adapterCart.itemCount)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        menu?.findItem(R.id.menu_action_search)?.let {
            search_view?.typeView = 0
            search_view?.setMenuItem(it)
        }
        return super.onCreateOptionsMenu(menu)
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