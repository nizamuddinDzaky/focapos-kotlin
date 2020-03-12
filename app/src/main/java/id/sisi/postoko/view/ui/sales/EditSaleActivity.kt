package id.sisi.postoko.view.ui.sales

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListProductAddSalesAdapter
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Product
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.activity_edit_sale.*
import kotlinx.android.synthetic.main.content_add_sales.*
import kotlinx.android.synthetic.main.content_edit_sale.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NAME_SHADOWING",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class EditSaleActivity : AppCompatActivity(), ListProductAddSalesAdapter.OnClickListenerInterface {
    var customer: Customer? = null
    private var idCustomer: String? = null
    private lateinit var adapter: ListProductAddSalesAdapter
    private var sale: Sales? =null
    private var saleItem: ArrayList<SaleItem>? = null
    private lateinit var viewModel: AddSalesViewModel
    private val progressBar = CustomProgressBar()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_sale)
        setSupportActionBar(toolbar_edit_sales)

        supportActionBar?.title=null
        val bundle = intent.extras
        sale = bundle?.getParcelable("sale")!!
        saleItem = bundle.getParcelableArrayList("sale_items")

        viewModel = ViewModelProvider(
            this
        ).get(AddSalesViewModel::class.java)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.parse(sale?.date)
        val strCurentDate = sdf.format(currentDate)
        var idRadioGroupStatusSale =0

        if(sale?.order_discount != 0.0 )
            et_discount_edit_sale.setText(sale?.order_discount.toString())
        if(sale?.shipping != 0.0)
            et_shipping_edit_sale.setText(sale?.shipping.toString())
        if(sale?.payment_term != 0 )
            et_payment_term_edit_sale.setText(sale?.payment_term.toString())

        et_note_edit_sale.setText(sale?.note.toString())
        et_staff_note_edit_sale.setText(sale?.staff_note.toString())

        et_date_edit_sale?.setText(strCurentDate.toDisplayDate())
        et_date_edit_sale?.hint = strCurentDate.toDisplayDate()
        et_date_edit_sale?.tag = strCurentDate
        et_date_edit_sale.setOnClickListener {

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val date = if (et_date_edit_sale.tag == null){
                inputDateFormat.format(Date())
            }else{
                et_date_edit_sale.tag.toString() + " 00:00:00"
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
                        et_date_edit_sale.setText(selectedDate.toDisplayDate())
                        et_date_edit_sale?.tag = selectedDate
                    }
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        et_customer_edit_sale.setText(sale?.customer)
        idCustomer= sale?.customer_id.toString()

        et_customer_edit_sale.setOnClickListener { _ ->
            val dialogFragment = FragmentSearchCustomer()
            dialogFragment.listener = {
                customer = it
                idCustomer = it.id
                et_customer_edit_sale.setText(it.company)
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

        btn_add_product_edit_sale.setOnClickListener {
            val intent = Intent(this, AddProductSalesActivity::class.java)
            startActivityForResult(intent, 1)
        }

        for (i in 0 until (rg_status_edit_sale?.childCount ?: 0)) {
            (rg_status_edit_sale?.get(i) as? RadioButton)?.tag =
                SaleStatus.values()[i].name.toLowerCase(
                    Locale.getDefault()
                )
            if (SaleStatus.values()[i].name.toLowerCase(Locale.getDefault()) == sale?.sale_status){
                idRadioGroupStatusSale = i
            }
        }
        rg_status_edit_sale?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_edit_sale?.tag = radioButton.tag
        }
        rg_status_edit_sale?.check(rg_status_edit_sale?.get(idRadioGroupStatusSale)?.id ?: 0)
        saleItem?.let { setupRecycleView(it) }

        btn_confirmation_edit_sale.setOnClickListener {
            actionEditSale()
        }
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
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onClickPlus(qty: Double, position: Int) {
        saleItem?.get(position)?.quantity = saleItem?.get(position)?.quantity?.plus(1.0)
        saleItem?.get(position)?.subtotal = saleItem?.get(position)?.quantity?.times(saleItem?.get(position)?.unit_price!!)
        adapter.notifyDataSetChanged()
        sumTotal()
    }

    override fun onClickMinus(qty: Double, position: Int) {
        val quantity = saleItem?.get(position)?.quantity?.minus(1.0)
        if (quantity != null) {
            if (quantity < 1){
                AlertDialog.Builder(this@EditSaleActivity)
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah yakin ?")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        saleItem?.get(position)?.quantity = quantity
                        saleItem?.removeAt(position)
                        adapter.notifyDataSetChanged()
                        sumTotal()
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ ->
                        adapter.notifyDataSetChanged()
                        sumTotal()
                    }
                    .show()
            }else{
                saleItem?.get(position)?.quantity = quantity
                saleItem?.get(position)?.subtotal = saleItem?.get(position)?.quantity?.times(saleItem?.get(position)?.unit_price!!)

                adapter.notifyDataSetChanged()
                sumTotal()
            }
        }
    }

    override fun onClickEdit(saleItem: SaleItem, position: Int) {
        val intent = Intent(this, EditProductSalesActivity::class.java)
        intent.putExtra("sale_item", saleItem)
        intent.putExtra("position", position)
        startActivityForResult(intent, 2)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this@EditSaleActivity)
            .setTitle("Konfirmasi")
            .setMessage("Apakah yakin ?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->

            }
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            AlertDialog.Builder(this@EditSaleActivity)
                .setTitle("Konfirmasi")
                .setMessage("Apakah yakin ?")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->

                }
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecycleView(it: List<SaleItem>) {
        adapter = ListProductAddSalesAdapter()
        adapter.updateMasterData(it)
        adapter.listenerProduct = this
        rv_list_product_edit_sale?.layoutManager = LinearLayoutManager(this)
        rv_list_product_edit_sale?.setHasFixedSize(false)
        rv_list_product_edit_sale?.adapter = adapter
        sumTotal()
    }

    private fun sumTotal() {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        var total = 0.0
        for (x in 0 until (saleItem?.size ?: 0)) {
            total += saleItem?.get(x)?.subtotal!!
        }
        tv_total_edit_sale.text = formatRupiah.format(total).toString()
    }

    private fun setDataFromAddProduct(data: Intent) {
        val product = data.getParcelableExtra<Product>("product_result") ?: return
        val si = SaleItem()
        si.product_id = product.id.toInt()
        si.product_code = product.code
        si.product_name = product.name
        si.net_unit_price = product.price.toDouble()
        si.unit_price = product.price.toDouble()
        si.quantity = 1.0
        si.subtotal = si.quantity!! * si.unit_price!!
        var cek = true
        for (x in 0 until saleItem?.size!!) {
            if (product.code == saleItem?.get(x)?.product_code) {
                cek = false
            }
        }
        if (cek)
            saleItem!!.add(si)
        else
            Toast.makeText(this, "Produk sama bro", Toast.LENGTH_SHORT).show()
        setupRecycleView(saleItem!!)
    }

    private fun setDataFromUpdateProduct(data: Intent, position: Int) {
        val si = data.getParcelableExtra<SaleItem>("new_sale_item")
        saleItem?.let {
            saleItem?.get(position)?.quantity = si.quantity
            saleItem?.get(position)?.discount = si.discount
            saleItem?.get(position)?.unit_price = si.unit_price
            saleItem?.get(position)?.subtotal = si.unit_price?.times(si.quantity!!)
        }
        saleItem?.let { setupRecycleView(it) }
    }

    private fun actionEditSale() {
        val numbersMap =  validationFormAddSale()
        if (numbersMap["type"] as Boolean){
            progressBar.show(this, "Silakan tunggu...")
            val saleItems = saleItem?.map {
                return@map mutableMapOf(
                    "product_id" to it.product_id.toString(),
                    "price" to it.unit_price.toString(),
                    "quantity" to it.quantity
                )
            }

            val body: MutableMap<String, Any?> = mutableMapOf(
                "date" to (et_date_edit_sale?.tag?.toString() ?: ""),
                "customer" to (idCustomer ?: ""),
                "order_discount" to (et_discount_edit_sale?.text?.toString() ?: ""),
                "shipping" to (et_shipping_edit_sale?.text?.toString() ?: ""),
                "sale_status" to (rg_status_edit_sale?.tag?.toString() ?: ""),
                "payment_term" to (et_payment_term_edit_sale?.text?.toString() ?: ""),
                "staff_note" to (et_staff_note_edit_sale?.text?.toString() ?: ""),
                "note" to (et_note_edit_sale?.text?.toString() ?: ""),
                "products" to saleItems
            )
            sale?.id?.let { viewModel.setIdSalesBooking(it) }
            viewModel.postEditSale(body){
                progressBar.dialog.dismiss()
                Toast.makeText(this, ""+it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    val returnIntent = Intent()
                    returnIntent.putExtra("sale_status", rg_status_add_sale.tag.toString())
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }else{
            AlertDialog.Builder(this@EditSaleActivity)
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
        if (saleItem?.size!! < 1){
            message += "- Product Item Tidak Boleh Kosong\n"
            cek = false
        }
        if (idCustomer == null ){
            message += "- Customer Tidak Boleh Kosong\n"
            cek = false
        }

        if (rg_status_edit_sale?.tag?.toString() == ""){
            message += "- Sale Status Tidak Boleh Kosong\n"
            cek = false
        }
        if (et_date_edit_sale?.text?.toString() == ""){
            message += "- Date Tidak Boleh Kosong"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }
}