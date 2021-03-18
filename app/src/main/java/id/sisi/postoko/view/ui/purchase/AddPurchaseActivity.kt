package id.sisi.postoko.view.ui.purchase

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemAddSaleAdapter
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.add
import id.sisi.postoko.utils.extensions.remove
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.helper.AddPurchaseFragment
import id.sisi.postoko.utils.helper.createFragment
import id.sisi.postoko.utils.helper.findPurchaseFragmentByTag
import id.sisi.postoko.utils.helper.getTag
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.product.ProductViewModel
import kotlinx.android.synthetic.main.activity_add_purchase.*

class AddPurchaseActivity : BaseActivity() {

    private var myDialog = MyDialog()
    var date: String? = null
    var idWarehouse: String? = null
    var warehouseName: String? = null
    var idSupplier: String? = null
    var supplierName: String? = null
    lateinit var vmProduct: ProductViewModel
    lateinit var vmAddPurchase: DetailPurchaseViewModel
    var listProduct: List<Product> = arrayListOf()
    var currentFragmentTag: String? = null
    lateinit var adapter: ListItemAddSaleAdapter
    var purchaseNote: String? = null
    private val progressBar = CustomProgressBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_purchase)
        supportActionBar?.title = null
        setSupportActionBar(toolbar)
        initFragment(savedInstanceState)

        adapter = ListItemAddSaleAdapter()
        vmProduct = ViewModelProvider(this).get(ProductViewModel::class.java)
        vmAddPurchase = ViewModelProvider(this).get(DetailPurchaseViewModel::class.java)
        vmAddPurchase.getIsExecute().observe(this, Observer {
            if (it) {
                progressBar.show(this, getString(R.string.txt_please_wait))
            } else {
                progressBar.dialog.dismiss()
            }
        })

        vmAddPurchase.getMessage().observe(this, Observer {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState ?: switchFragment(AddPurchaseFragment.SELECT_SUPPLIER)
    }

    fun switchFragment(fragment: AddPurchaseFragment): Boolean{
        return findFragment(fragment).let {
            if (it.isAdded) return false
            supportFragmentManager.remove() // Extension function
            supportFragmentManager.add(it, fragment.getTag()) // Extension function
            supportFragmentManager.executePendingTransactions()
        }
    }

    private fun findFragment(fragment: AddPurchaseFragment): Fragment {
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
            /*logE("selectedTotal : ${product.price.toDouble()}")*/
            if (product.isSelected){
                total += (product.orderQty * (product.price?.toDouble()?.toInt() ?: 0))
            }
        }
        return total
    }

    fun getDiscount(): Double{
        var disc = 0.0
        listProduct.forEach { product ->
            if (product.isSelected)
                disc += (product.orderQty * product.discount)
        }
        return disc
    }

    fun countItemSelected(): Int{
        var totalSelected = 0
        listProduct.forEach { product ->
            if (product.isSelected)
                totalSelected ++
        }
        return totalSelected
    }

    private fun setUpTotal(){
        if (findViewById<TextView>(R.id.tv_total_add_sale) != null)
            findViewById<TextView>(R.id.tv_total_add_sale).text = getTotal().toCurrencyID()
    }

    fun showCart(){
        val bottomSheetCartAddPurchaseFragment = BottomSheetCartAddPurchaseFragment
        bottomSheetCartAddPurchaseFragment.show(supportFragmentManager)
        bottomSheetCartAddPurchaseFragment.listener={
            setUpBadge()
            setUpTotal()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        when(currentFragmentTag){
            PaymentAddPurchaseFragment.TAG -> switchFragment(
                findPurchaseFragmentByTag(
                    AddItemPurchaseFragment.TAG)
            )
            AddItemPurchaseFragment.TAG -> switchFragment(
                findPurchaseFragmentByTag(
                    SelectSupplierFragment.TAG)
            )
            SelectSupplierFragment.TAG -> {
                myDialog.confirmation(getString(R.string.txt_are_you_sure), this)
                myDialog.listenerPositif ={
                    super.onBackPressed()
                }
            }
        }
    }
}