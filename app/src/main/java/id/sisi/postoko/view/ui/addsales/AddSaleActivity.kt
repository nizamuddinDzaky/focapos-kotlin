package id.sisi.postoko.view.ui.addsales


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.add
import id.sisi.postoko.utils.extensions.attach
import id.sisi.postoko.utils.extensions.detach
import id.sisi.postoko.utils.extensions.remove
import id.sisi.postoko.utils.helper.AddSaleFragment
import id.sisi.postoko.utils.helper.createFragment
import id.sisi.postoko.utils.helper.findSaleFragmentByTag
import id.sisi.postoko.utils.helper.getTag
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.product.ProductViewModel


class AddSaleActivity : AppCompatActivity() {

    var idWarehouse: String? = null
    var warehouseName: String? = null
    var idCustomer: String? = null
    var customerName: String? = null
    var currentFragmentTag: String? = null
    private lateinit var vmProduct: ProductViewModel
    var listProdcut: List<Product> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sale)
        supportActionBar?.title = null

        initFragment(savedInstanceState)

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

    override fun onBackPressed() {
        when(currentFragmentTag){
            AddItemAddSaleFragment.TAG -> switchFragment(findSaleFragmentByTag(SelectCustomerFragment.TAG))
            SelectCustomerFragment.TAG -> super.onBackPressed()
        }
    }
}
