package id.sisi.postoko.view.ui.sales

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiper.MaterialSpinner
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListProductAddSalesAdapter
import id.sisi.postoko.model.*
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.view.ui.supplier.SupplierViewModel
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.activity_add_sales.*
import kotlinx.android.synthetic.main.content_add_sales.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class AddSalesActivity : AppCompatActivity(), ListProductAddSalesAdapter.OnClickListenerInterface {
    var customer: Customer? = null
    private lateinit var viewModelSupplier: SupplierViewModel
    private lateinit var viewModelWarehouse: WarehouseViewModel
    private var listSupplierName = ArrayList<String>()
    private var listSupplier:List<Supplier>  = ArrayList()
    private var listWarehouseName = ArrayList<String>()
    private var listWarehouse: List<Warehouse> = ArrayList()
    private var listSaleItems = ArrayList<SaleItem>()
    private lateinit var adapter: ListProductAddSalesAdapter
    private lateinit var viewModel: AddSalesViewModel

    var listener: () -> Unit = {}

    private var idCustomer: String? = null
    private var idWarehouse: String? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sales)
        setSupportActionBar(toolbar_add_sales)
        val actionBar = supportActionBar
        actionBar!!.title = ""
        actionBar.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(
            this
        ).get(AddSalesViewModel::class.java)
        viewModel.getIsExecute().observe(this, Observer {
//            viewLifecycleOwner
            if (it) {
                logE("progress")
            } else {
                logE("done")
            }
        })

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        et_date_add_sale?.setText(currentDate.toDisplayDate())
        et_date_add_sale?.hint = currentDate.toDisplayDate()
        et_date_add_sale?.tag = currentDate
        et_date_add_sale.setOnClickListener {

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val date = if (et_date_add_sale.tag == null){
                inputDateFormat.format(Date())
            }else{
                et_date_add_sale.tag.toString() + " 00:00:00"
            }
            val resultDate = inputDateFormat.parse(date)
            val calendar: Calendar = GregorianCalendar()
            resultDate?.let {
                calendar.time = resultDate
            }
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, _, monthOfYear, dayOfMonth ->
                    val parseDate = inputDateFormat.parse("$year-${monthOfYear + 1}-$dayOfMonth 00:00:00")
                    parseDate?.let {
                        val selectedDate = inputDateFormat.format(parseDate)
                        et_date_add_sale.setText(selectedDate.toDisplayDate())
                        et_date_add_sale?.tag = selectedDate

                    }
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        viewModelSupplier = ViewModelProvider(this).get(SupplierViewModel::class.java)
        viewModelSupplier.getListSuppliers().observe(this, Observer {
            if (it != null){
                for (x in it.indices) {
                    listSupplierName.add(it[x].name)
                }
                listSupplier = it
            }
        })
        val adapterSupplier = ArrayAdapter(this, android.R.layout.simple_spinner_item, listSupplierName)
        sp_supplier.adapter = adapterSupplier

        viewModelWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        viewModelWarehouse.getListWarehouses().observe(this, Observer {
            it?.let {
                for (x in it.indices)
                    listWarehouseName.add(it[x].name)
                listWarehouse = it
            }
        })
        val adapterGudang = ArrayAdapter(this, android.R.layout.simple_spinner_item, listWarehouseName)
        sp_warehouse_add_sale.adapter = adapterGudang
        sp_warehouse_add_sale.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner,view: View?,position: Int,id: Long) {
                idWarehouse = listWarehouse[position].id
            }
            override fun onNothingSelected(parent: MaterialSpinner): Unit = Unit
        }

        et_customer.setOnClickListener { _ ->
            val dialogFragment = FragmentSearchCustomer()
            dialogFragment.listener = {
                logE("cek $it")
                customer = it
                idCustomer = it.id
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

        btn_add_product.setOnClickListener {
            val intent = Intent(this, AddProductSalesActivity::class.java)
            startActivityForResult(intent, 1)
        }

        btn_confirmation_add_sale.setOnClickListener {
            actionAddSale()
        }

        for (i in 0 until (rg_status_add_sale?.childCount ?: 0)) {
            (rg_status_add_sale?.get(i) as? RadioButton)?.tag =
                SaleStatus.values()[i].name.toLowerCase(
                    Locale.getDefault()
                )
        }
        rg_status_add_sale?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_add_sale?.tag = radioButton.tag
        }
        rg_status_add_sale?.check(rg_status_add_sale?.get(0)?.id ?: 0)

        for (i in 0 until (rg_status_add_sale?.childCount ?: 0)) {
            (rg_status_add_sale?.get(i) as? RadioButton)?.tag =
                SaleStatus.values()[i].name.toLowerCase(
                    Locale.getDefault()
                )
        }
        rg_status_add_sale?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_add_sale?.tag = radioButton.tag
        }
        rg_status_add_sale?.check(rg_status_add_sale?.get(0)?.id ?: 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setDataFromAddProduct(data as Intent)
            }
        }else if(requestCode == 2){
            val position = data?.getIntExtra("position", 0)
            if (position != null) {
                setDataFromUpdateProduct(data, position)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setDataFromUpdateProduct(data: Intent, position: Int) {
        val saleItem = data.getParcelableExtra<SaleItem>("new_sale_item")
        saleItem?.let {
            listSaleItems[position].quantity = saleItem.quantity
            listSaleItems[position].discount = saleItem.discount
            listSaleItems[position].unit_price = saleItem.unit_price
            listSaleItems[position].subtotal = saleItem.unit_price?.times(saleItem.quantity!!)
        }
        setupRecycleView(listSaleItems)
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

    override fun onClickPlus(qty: Double, position: Int) {
        listSaleItems[position].quantity = listSaleItems[position].quantity?.plus(1.0)
        listSaleItems[position].subtotal = listSaleItems[position].quantity?.times(listSaleItems[position].unit_price!!)
        adapter.notifyDataSetChanged()
        sumTotal()
    }

    override fun onClickMinus(qty: Double, position: Int) {
        val quantity = listSaleItems[position].quantity?.minus(1.0)
        if (quantity != null) {
            if (quantity < 1){
                AlertDialog.Builder(this@AddSalesActivity)
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah yakin ?")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        listSaleItems[position].quantity = quantity
                        listSaleItems.removeAt(position)
                        adapter.notifyDataSetChanged()
                        sumTotal()
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ ->
                        adapter.notifyDataSetChanged()
                        sumTotal()
                    }
                    .show()
            }else{
                listSaleItems[position].quantity = quantity
                listSaleItems[position].subtotal = listSaleItems[position].quantity?.times(listSaleItems[position].unit_price!!)
                adapter.notifyDataSetChanged()
            }
        }

        sumTotal()
    }

    override fun onClickEdit(saleItem: SaleItem, position: Int) {
        val intent = Intent(this, EditProductSalesActivity::class.java)
        intent.putExtra("sale_item", saleItem)
        intent.putExtra("position", position)
        startActivityForResult(intent, 2)
    }

    private fun actionAddSale(){
        val numbersMap =  validationFormAddSale()
        if (numbersMap["type"] as Boolean){
            val saleItems = listSaleItems.map {
                return@map mutableMapOf(
                    "product_id" to it.product_id.toString(),
                    "price" to it.unit_price.toString(),
                    "quantity" to it.quantity
                )
            }

            val body: MutableMap<String, Any> = mutableMapOf(
                "date" to (et_date_add_sale?.tag?.toString() ?: ""),
                "warehouse" to (idWarehouse ?: ""),
                "customer" to (idCustomer ?: ""),
                "order_discount" to (et_discount_add_sale?.text?.toString() ?: ""),
                "shipping" to (et_shipping_add_sale?.text?.toString() ?: ""),
                "sale_status" to (rg_status_add_sale?.tag?.toString() ?: ""),
                "payment_term" to (et_payment_term_add_sale?.text?.toString() ?: ""),
                "staff_note" to (et_staff_note_add_sale?.text?.toString() ?: ""),
                "note" to (et_note_add_sale?.text?.toString() ?: ""),
                "products" to saleItems
            )
            viewModel.postAddSales(body){
                setResult(Activity.RESULT_OK)
                finish()
            }
        }else{
            AlertDialog.Builder(this@AddSalesActivity)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    private fun validationFormAddSale(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (listSaleItems.size < 1){
            message += "- Product Item Tidak Boleh Kosong\n"
            cek = false
        }
        if (idCustomer == null ){
            message += "- Customer Tidak Boleh Kosong\n"
            cek = false
        }
        if (idWarehouse == null ){
            message += "- Warehouse Tidak Boleh Kosong\n"
            cek = false
        }
        if (rg_status_add_sale?.tag?.toString() == ""){
            message += "- Sale Status Tidak Boleh Kosong\n"
            cek = false
        }
        if (et_date_add_sale?.text?.toString() == ""){
            message += "- Date Tidak Boleh Kosong"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }

    override fun onBackPressed()
    {
        AlertDialog.Builder(this@AddSalesActivity)
            .setTitle("Konfirmasi")
            .setMessage("Apakah yakin ?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->

            }
            .show()
    }
}
