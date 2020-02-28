package id.sisi.postoko.view.ui.sales

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListProductAddSalesAdapter
import id.sisi.postoko.adapter.OnClickListenerInterface
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Product
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.supplier.SupplierViewModel
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.activity_add_sales.*
import kotlinx.android.synthetic.main.content_add_sales.*
import java.text.NumberFormat
import java.util.*

class AddSalesActivity : AppCompatActivity(), OnClickListenerInterface {
    var customer: Customer? = null
    private lateinit var viewModelSupplier: SupplierViewModel
    private lateinit var viewModelWarehouse: WarehouseViewModel
    private var listSupplier = ArrayList<String>()
    private var listWarehouse = ArrayList<String>()
    private var listSaleItems = ArrayList<SaleItem>()
    private lateinit var adapter: ListProductAddSalesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sales)
        setSupportActionBar(toolbar_add_sales)
        val actionBar = supportActionBar
        actionBar!!.title = ""
        actionBar.setDisplayHomeAsUpEnabled(true)


        et_tanggal.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, _, monthOfYear, dayOfMonth ->
                    val selectedDate = """$dayOfMonth - ${monthOfYear + 1} - $year"""
                    et_tanggal.setText(selectedDate)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        viewModelSupplier = ViewModelProvider(this).get(SupplierViewModel::class.java)
        viewModelSupplier.getListSuppliers().observe(this, Observer {
            if (it != null) for (x in it.indices)
                listSupplier.add(it[x].name)
        })
        val adapterSupplier = ArrayAdapter(this, android.R.layout.simple_spinner_item, listSupplier)
        sp_supplier.adapter = adapterSupplier

        viewModelWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        viewModelWarehouse.getListWarehouses().observe(this, Observer {
            it?.let {
                for (x in it.indices)
                    listWarehouse.add(it[x].name)
            }
        })
        val adapterGudang = ArrayAdapter(this, android.R.layout.simple_spinner_item, listWarehouse)
        sp_gudang.adapter = adapterGudang

        et_customer.setOnClickListener { _ ->
            val dialogFragment = FragmentSearchCustomer()
            dialogFragment.listener = {
                logE("cek $it")
                customer = it
                et_customer.setText(it.company)
            }

            val bundle = Bundle()
            bundle.putBoolean("notAlertDialog", true)
            dialogFragment.arguments = bundle
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            dialogFragment.show(ft, "dialog")
        }

        btn_tambah_product.setOnClickListener {
            val intent = Intent(this, AddProductSalesActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setDataFromAddProduct(data as Intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setupRecycleView(it: List<SaleItem>) {
        adapter = ListProductAddSalesAdapter()
        adapter.updateMasterData(it)
        adapter.listenerProduct = this
        rv_list_product_add_sale?.layoutManager = LinearLayoutManager(this)
        rv_list_product_add_sale?.setHasFixedSize(false)
        rv_list_product_add_sale?.adapter = adapter
        sumTotal()
    }

    private fun sumTotal() {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        var total = 0.0
        for (x in 0 until listSaleItems.size) {
            total += listSaleItems[x].subtotal!!
        }
        tv_total_add_sale.text = formatRupiah.format(total).toString()
    }

    private fun setDataFromAddProduct(data: Intent) {
        val product = data.getParcelableExtra<Product>("product_result") ?: return
        val saleItem = SaleItem()
        saleItem.product_id = product.id.toInt()
        saleItem.product_code = product.code
        saleItem.product_name = product.name
        saleItem.net_unit_price = product.price.toDouble()
        saleItem.unit_price = product.price.toDouble()
        saleItem.quantity = 1.0
        saleItem.subtotal = saleItem.quantity!! * saleItem.unit_price!!
        var cek = true

        for (x in 0 until listSaleItems.size) {
            if (product.code == listSaleItems[x].product_code) {
                cek = false
            }
        }

        if (cek)
            listSaleItems.add(saleItem)
        else
            Toast.makeText(this, "Produk sama bro", Toast.LENGTH_SHORT).show()
        setupRecycleView(listSaleItems)
    }

    override fun onClickPlus() {
        sumTotal()
    }

    override fun onClickMinus() {
        sumTotal()
    }

    override fun onClickEdit(saleItem: SaleItem, position: Int) {
        val intent = Intent(this, EditProductSalesActivity::class.java)
        intent.putExtra("sale_item", saleItem)
        startActivityForResult(intent, 2)
    }
}
