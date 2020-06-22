package id.sisi.postoko.view.ui.customer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.customergroup.ListCartToWarehouseAdapter
import id.sisi.postoko.view.ui.customergroup.ListWarehouseToCartAdapter
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.activity_customer_add_warehouse.*

class AddCustomerWarehouseActivity : BaseActivity() {

    private var firstListWarehouse: List<Warehouse>? = arrayListOf()

    private lateinit var adapterWarehouse: ListWarehouseToCartAdapter<Warehouse>
    private lateinit var adapterCart: ListCartToWarehouseAdapter<Warehouse>
    private var listWarehouseCart = ArrayList<Warehouse>()
    private lateinit var vmWarehouseGroup: WarehouseViewModel
    private val progressBar = CustomProgressBar()
    /*private var strFilter: String? = null
    private var strCountry: String? = null
    private var strCity: String? = null*/

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_add_warehouse)
        setSupportActionBar(toolbar)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = ""

        initView()
        setupData()
    }

    private fun initView() {
        adapterWarehouse = ListWarehouseToCartAdapter(fragmentActivity = this)
        adapterCart = ListCartToWarehouseAdapter(fragmentActivity = this)
        vmWarehouseGroup = ViewModelProvider(this).get(WarehouseViewModel::class.java)
    }

    private fun setupData() {
        rv_list_warehouse?.adapter = adapterWarehouse
        rv_list_warehouse_cart?.adapter = adapterCart
        rv_list_warehouse?.addVerticalDivider()

        setupDataCustomer(/*true*/)
    }

    private fun setupDataCustomer(/*loadNew: Boolean = false*/) {
        /*if (loadNew) {*/
        progressBar.show(this, "Silakan tunggu...")
        vmWarehouseGroup.getListWarehouses().observe(this, Observer {
            logE("setupDataCustomer", it.toString())
            firstListWarehouse = it ?: listOf()
            adapterWarehouse.updateMasterData(firstListWarehouse)
            //setupDataCart()
            progressBar.dialog.dismiss()
        })
        vmWarehouseGroup.getListWarehouse()
        /*}else{
            adapterCustomer.updateMasterData(firstListCustomer)
        }*/
    }

    @SuppressLint("SetTextI18n")
    fun validation(warehouse: Warehouse) {
        logE(warehouse.isSelected.toString(), "isSelected")
        if (warehouse.isSelected) {
            adapterCart.insertData(warehouse)
            logE(warehouse.toString(), "Line 78")
            //listCustomerCart.add(warehouse)
            adapterWarehouse.notifyDataSetChanged()
            rv_list_warehouse_cart?.smoothScrollToPosition(0)
        } else {
            adapterCart.removeData(warehouse)

            //listCustomerCart.remove(warehouse)
            adapterWarehouse.notifyDataSetChanged()
        }
        //tv_total_selected.text = "Pelanggan yang terpilih (${listWarehouseCart.size})"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_blue_ic, menu)
        menu?.findItem(R.id.menu_action_search)
        return super.onCreateOptionsMenu(menu)
    }
}