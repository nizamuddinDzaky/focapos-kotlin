package id.sisi.postoko.view.ui.addsales


import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemAddSaleAdapter
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.MyAlert
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.utils.helper.AddSaleFragment
import id.sisi.postoko.utils.helper.createFragment
import id.sisi.postoko.utils.helper.findSaleFragmentByTag
import id.sisi.postoko.utils.helper.getTag
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.product.ProductViewModel
import id.sisi.postoko.view.ui.sales.AddSalesViewModel
import kotlinx.android.synthetic.main.activity_add_sale.*


class AddSaleActivity : BaseActivity() {

    var idWarehouse: String? = null
    var warehouseName: String? = null
    var idCustomer: String? = null
    var customerName: String? = null
    var currentFragmentTag: String? = null
    var saleNote: String? = null
    var employeeNote: String? = null
    var status: String? = null
    var discount: String? = null
    var shipmentPrice: String? = null
    var paymentTerm: String? = null
    var date: String? = null
    private val progressBar = CustomProgressBar()
    lateinit var adapter: ListItemAddSaleAdapter
    private lateinit var vmProduct: ProductViewModel
    var listProduct: List<Product> = arrayListOf()
    lateinit var vmAddSale: AddSalesViewModel
    private var alert = MyAlert()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sale)
        supportActionBar?.title = null
        setSupportActionBar(toolbar)
        initFragment(savedInstanceState)

        adapter = ListItemAddSaleAdapter()

        vmProduct = ViewModelProvider(this).get(ProductViewModel::class.java)
        vmProduct.getListProducts().observe(this, Observer {
            if (it != null) {
                listProduct=it
            }
        })

        vmAddSale = ViewModelProvider(
            this
        ).get(AddSalesViewModel::class.java)
        vmAddSale.getIsExecute().observe(this, Observer {
            if (it) {
                progressBar.show(this, getString(R.string.txt_please_wait))
            } else {
                progressBar.dialog.dismiss()
            }
        })

        vmAddSale.getMessage().observe(this, Observer {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState ?: switchFragment(AddSaleFragment.SELECT_CUSTOMER)
    }

    fun switchFragment(fragment: AddSaleFragment): Boolean{
        return findFragment(fragment).let {
            if (it.isAdded) return false
            supportFragmentManager.detach() // Extension function
            supportFragmentManager.attach(it, fragment.getTag()) // Extension function
            supportFragmentManager.executePendingTransactions()
        }
    }

    private fun findFragment(fragment: AddSaleFragment): Fragment {
        return supportFragmentManager.findFragmentByTag(fragment.getTag())
            ?: fragment.createFragment()
    }

    fun setUpBadge(){
        val totalSelected = countItemSelected()
        findViewById<TextView>(R.id.cart_badge).text = totalSelected.toString()
    }

    fun getTotal(): Double{
        var total = 0.0
        listProduct.forEach { product ->
            if (product.isSelected)
                total += (product.sale_qty * product.price)
        }
        return total
    }

    private fun setUpTotal(){
        if (findViewById<TextView>(R.id.tv_total_add_sale) != null)
            findViewById<TextView>(R.id.tv_total_add_sale).text = getTotal().toCurrencyID()
    }

    fun countItemSelected(): Int{
        var totalSelected = 0
        listProduct.forEach { product ->
            if (product.isSelected)
                totalSelected ++
        }
        return totalSelected
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_cart, menu)
        val menuItem = menu?.findItem(R.id.action_cart)

        val actionView = menuItem?.actionView
        actionView?.rootView?.findViewById<TextView>(R.id.cart_badge)?.text = countItemSelected().toString()
        actionView?.setOnClickListener {
            val bottomSheetCartAddSaleFragment = BottomSheetCartAddSaleFragment
            bottomSheetCartAddSaleFragment.show(supportFragmentManager)
            bottomSheetCartAddSaleFragment.listener={
                setUpBadge()
                setUpTotal()
                adapter.notifyDataSetChanged()
            }
        }
        return true
    }

    override fun onBackPressed() {
        when(currentFragmentTag){
            PaymentAddSaleFragment.TAG -> switchFragment(findSaleFragmentByTag(AddItemAddSaleFragment.TAG))
            AddItemAddSaleFragment.TAG -> switchFragment(findSaleFragmentByTag(SelectCustomerFragment.TAG))
            SelectCustomerFragment.TAG -> {
                alert.confirmation(getString(R.string.txt_are_you_sure), this)
                alert.listenerPositif ={
                    super.onBackPressed()
                }
            }
        }
    }
}
