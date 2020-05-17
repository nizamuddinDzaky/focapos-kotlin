package id.sisi.postoko.view.ui.addsales


import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemAddSaleAdapter
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.add
import id.sisi.postoko.utils.extensions.remove
import id.sisi.postoko.utils.helper.AddSaleFragment
import id.sisi.postoko.utils.helper.createFragment
import id.sisi.postoko.utils.helper.findSaleFragmentByTag
import id.sisi.postoko.utils.helper.getTag
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.ui.product.ProductViewModel
import kotlinx.android.synthetic.main.activity_add_sale.*
import kotlinx.android.synthetic.main.custom_action_item_layout.*


class AddSaleActivity : BaseActivity() {

    var idWarehouse: String? = null
    var warehouseName: String? = null
    var idCustomer: String? = null
    var customerName: String? = null
    var currentFragmentTag: String? = null
    lateinit var adapter: ListItemAddSaleAdapter
    private lateinit var vmProduct: ProductViewModel
    var listProdcut: List<Product> = arrayListOf()


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
                listProdcut=it
            }
        })
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState ?: switchFragment(AddSaleFragment.SELECT_CUSTOMER)
    }

    fun switchFragment(fragment: AddSaleFragment): Boolean{
        return findFragment(fragment).let {
            if (it.isAdded) return false
            supportFragmentManager.remove() // Extension function
            supportFragmentManager.add(it, fragment.getTag()) // Extension function
            supportFragmentManager.executePendingTransactions()
        }
    }

    private fun findFragment(fragment: AddSaleFragment): Fragment {
        return supportFragmentManager.findFragmentByTag(fragment.getTag())
            ?: fragment.createFragment()
    }

    fun setUpBadge(){
        val totalSelected = countItemSelected()
        cart_badge.text = totalSelected.toString()
    }

    private fun countItemSelected(): Int{
        var totalSelected = 0
        listProdcut.forEach { product ->
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
        actionView?.setOnClickListener {
            val bottomSheetCartAddSaleFragment = BottomSheetCartAddSaleFragment
            bottomSheetCartAddSaleFragment.show(supportFragmentManager)
            bottomSheetCartAddSaleFragment.listener={
                setUpBadge()
                adapter.notifyDataSetChanged()
            }
        }
        return true
    }

    override fun onBackPressed() {
        when(currentFragmentTag){
            PaymentAddSaleFragment.TAG -> switchFragment(findSaleFragmentByTag(AddItemAddSaleFragment.TAG))
            AddItemAddSaleFragment.TAG -> switchFragment(findSaleFragmentByTag(SelectCustomerFragment.TAG))
            SelectCustomerFragment.TAG -> super.onBackPressed()
        }
    }
}
