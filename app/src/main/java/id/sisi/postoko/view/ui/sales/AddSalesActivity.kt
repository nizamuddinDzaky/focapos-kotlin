package id.sisi.postoko.view.ui.sales

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Supplier
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.supplier.SupplierViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.adapter.ListProductAddSalesAdapter
import id.sisi.postoko.adapter.ListSearchCustomerAdapter
import id.sisi.postoko.adapter.OnClickListenerInterface
import id.sisi.postoko.model.Product
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.activity_add_product_sales.*
import kotlinx.android.synthetic.main.activity_add_sales.*
import kotlinx.android.synthetic.main.content_add_sales.*
import kotlinx.android.synthetic.main.fragment_search_customer.*
import java.text.NumberFormat
import java.util.*

class AddSalesActivity : AppCompatActivity(), OnClickListenerInterface {
    var customer : Customer? = null
    private lateinit var viewModelSupplier: SupplierViewModel
    private lateinit var viewModelGudang: WarehouseViewModel
    var listSupplier = ArrayList<String>()
    var listGudang= ArrayList<String>()
    var listSaleItems= ArrayList<SaleItem>()
    private lateinit var adapter: ListProductAddSalesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sales)
        setSupportActionBar(toolbar_add_sales)
        val actionBar = supportActionBar
        actionBar!!.title = ""
        actionBar.setDisplayHomeAsUpEnabled(true)


        et_tanggal.setOnClickListener { view ->
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                et_tanggal.setText("""$dayOfMonth - ${monthOfYear + 1} - $year""")
            }, year, month, day)
            dpd.show()
        }

        viewModelSupplier = ViewModelProvider(this).get(SupplierViewModel::class.java)
        viewModelSupplier.getListSuppliers().observe(this, Observer {
            if (it != null) {
                for (x in 0 until it.size)
                    listSupplier.add(it.get(x).name)
            }
        })
        val adapterSupplier = ArrayAdapter(this,android.R.layout.simple_spinner_item,listSupplier)
        sp_supplier.adapter = adapterSupplier

        viewModelGudang= ViewModelProvider(this).get(WarehouseViewModel::class.java)
        viewModelGudang.getListWarehouses().observe(this, Observer {
            if (it != null) {
                for (x in 0 until it.size)
                    listGudang.add(it.get(x).name)
            }
        })
        val adapterGudang= ArrayAdapter(this,android.R.layout.simple_spinner_item,listGudang)
        sp_gudang.adapter = adapterGudang

        et_customer.setOnClickListener { view ->
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
            if (prev != null)
            {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            dialogFragment.show(ft, "dialog")
        }

        btn_tambah_product.setOnClickListener {
            val intent = Intent(this, AddProductSalesActivity::class.java)
            startActivityForResult(intent , 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setDataFromAddProduct(data)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setupRecycleView(it : List<SaleItem>) {
        adapter = ListProductAddSalesAdapter()
        adapter.updateMasterData(it)
        adapter.listenerProduct = this
        rv_list_product_add_sale?.layoutManager = LinearLayoutManager(this)
        rv_list_product_add_sale?.setHasFixedSize(false)
        rv_list_product_add_sale?.adapter = adapter
        sumTotal()
    }

    fun sumTotal (){
        val localeID = Locale("in", "ID")
        var formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        var total : Double = 0.0
        for (x in 0 until listSaleItems.size){
            total += listSaleItems.get(x).subtotal?.toDouble()!!
        }
        tv_total_add_sale.text = formatRupiah.format(total).toString()
    }

    private fun setDataFromAddProduct(data: Intent?){
        val product = data!!.getParcelableExtra<Product>("product_result")
        var saleItem = SaleItem()
        saleItem?.product_id =product.id.toInt()
        saleItem?.product_code = product.code
        saleItem?.product_name = product.name
        saleItem?.net_unit_price = product.price.toDouble()
        saleItem?.unit_price =  product.price.toDouble()
        saleItem?.quantity = 1.0
        saleItem?.subtotal =saleItem?.quantity!! * saleItem?.unit_price!!
        if (saleItem != null) {
            var cek : Boolean = true

            for (x in 0 until listSaleItems.size){
                if(product.code == listSaleItems.get(x).product_code){
                    cek = false
                }
            }

            if(cek)
                listSaleItems.add(saleItem)
            else
                Toast.makeText(this, "Produk sama bro", Toast.LENGTH_SHORT).show()
            setupRecycleView(listSaleItems)
        }
    }

    override fun onClickPlus(){
        sumTotal()
    }

    override fun onClickMinus(){
        sumTotal()
    }
    override fun onClickEdit(saleItem: SaleItem, position: Int) {
        val intent = Intent(this, EditProductSalesActivity::class.java)
        intent.putExtra("sale_item", saleItem)
        startActivityForResult(intent , 2)
    }
}
