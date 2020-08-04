package id.sisi.postoko.view.ui.sales

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListProductAddSalesAdapter
import id.sisi.postoko.model.*
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setIfExist
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.validation
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.activity_edit_sale.*
import kotlinx.android.synthetic.main.content_edit_sale.*
import kotlinx.android.synthetic.main.payment_add_sale_fragment.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NAME_SHADOWING",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
class EditSaleActivity : BaseActivity(), ListProductAddSalesAdapter.OnClickListenerInterface {
    var customer: Customer? = null
    private var idCustomer: String? = null
    private var idWarehouse: String? = null
    private lateinit var viewModelWarehouse: WarehouseViewModel
    private lateinit var adapter: ListProductAddSalesAdapter
    private var sale: Sales? = null
    private var saleItem: ArrayList<SaleItem>? = null
    private var listWarehouse: List<Warehouse> = ArrayList()
    private lateinit var viewModel: AddSalesViewModel
    private val progressBar = CustomProgressBar()
    private var myDialog = MyDialog()
    private var listTOP: Array<DataTermOfPayment> = arrayOf()
    private var termOfPayment: String? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_sale)
        setSupportActionBar(toolbar_edit_sales)

        supportActionBar?.title = null
        val bundle = intent.extras
        sale = bundle?.getParcelable(KEY_SALE)
        saleItem = bundle?.getParcelableArrayList(KEY_SALE_ITEM)

        viewModel = ViewModelProvider(
            this
        ).get(AddSalesViewModel::class.java)

        viewModel.getIsExecute().observe(this, Observer {
            if (it) {
                progressBar.show(this, getString(R.string.txt_please_wait))
            } else {
                progressBar.dialog.dismiss()
            }
        })

        viewModel.getMessage().observe(this, Observer {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getTermOfPayment {
            it?.let {listTOP ->
                this.listTOP = listTOP.toTypedArray()
            }
            setUpTOP()
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.parse(sale?.date)
        val strCurrentDate = sdf.format(currentDate)
        var idRadioGroupStatusSale = 0
        et_discount_edit_sale.addTextChangedListener(NumberSeparator(et_discount_edit_sale))
        et_shipping_edit_sale.addTextChangedListener(NumberSeparator(et_shipping_edit_sale))

        if (sale?.order_discount != 0.0)
            et_discount_edit_sale.setText(sale?.order_discount?.toInt().toString())
        if (sale?.shipping != 0.0 && sale?.shipping.toString() != "null")
            et_shipping_edit_sale.setText(sale?.shipping?.toInt().toString())
        /*if (sale?.payment_term != 0 && sale?.payment_term.toString() != "null")
            et_payment_term_edit_sale.setText(sale?.payment_term.toString())*/
        idWarehouse = sale?.warehouse_id.toString()

        et_note_edit_sale.setText(sale?.note.toString())
        et_staff_note_edit_sale.setText(sale?.staff_note.toString())
        et_reason_edit_sale.setText(sale?.reason)
        et_date_edit_sale?.setText(strCurrentDate.toDisplayDate())
        et_date_edit_sale?.hint = strCurrentDate.toDisplayDate()
        et_date_edit_sale?.tag = strCurrentDate
        et_date_edit_sale.setOnClickListener {

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val date = if (et_date_edit_sale.tag == null) {
                inputDateFormat.format(Date())
            } else {
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
                    val parseDate =
                        inputDateFormat.parse("$year-${monthOfYear + 1}-$dayOfMonth 00:00:00")
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

        sp_payment_term_edit_sale.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                termOfPayment = listTOP[position].duration

            }
        }

        val adapterWarehouse = MySpinnerAdapter(this, R.layout.list_spinner)
        viewModelWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        viewModelWarehouse.getListWarehouses().observe(this, Observer {
            it?.let {
                adapterWarehouse.udpateView(it.map {wh->
                    return@map DataSpinner(wh.name, wh.id)
                }.toMutableList())
                sp_warehouse_edit_sale.setIfExist(idWarehouse.toString())
                listWarehouse=it
            }
        })

        sp_warehouse_edit_sale.adapter = adapterWarehouse
        sp_warehouse_edit_sale.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                idWarehouse = listWarehouse[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        et_customer_edit_sale.setText(sale?.customer)
        idCustomer = sale?.customer_id.toString()

        et_customer_edit_sale.setOnClickListener { _ ->
            val dialogFragment = FragmentSearchCustomer()
            val ft = supportFragmentManager.beginTransaction()
            dialogFragment.listener = {
                customer = it
                idCustomer = it.id
                et_customer_edit_sale.setText(it.company)
            }
            dialogFragment.show(ft, FragmentSearchCustomer().tag)
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
            if (SaleStatus.values()[i].name.toLowerCase(Locale.getDefault()) == sale?.sale_status) {
                idRadioGroupStatusSale = i
            }
        }
        rg_status_edit_sale?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_edit_sale?.tag = radioButton.tag
        }
        rg_status_edit_sale?.check(rg_status_edit_sale?.get(idRadioGroupStatusSale)?.id ?: 0)
        saleItem?.let { setupRecycleView(it) }

        val mandatory = listOf<EditText>(
            et_date_edit_sale
        )

        btn_confirmation_edit_sale.setOnClickListener {
            if (!mandatory.validation()) {
                return@setOnClickListener
            }
            actionEditSale()
        }
    }

    private fun setUpTOP() {
        val adapter = this.let {
            MySpinnerAdapter(
                it,
                R.layout.list_spinner
            )
        }

        listTOP.let {
            adapter.udpateView(it.map { top->
                return@map DataSpinner(top.description ?: "" , top.duration ?: "")
            }.toMutableList())
        }
        sp_payment_term_edit_sale.adapter = adapter
        sp_payment_term_edit_sale.setIfExist(sale?.payment_term.toString())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setDataFromAddProduct(data as Intent)
            }
        } else if (requestCode == 2) {
            val position = data?.getIntExtra("position", 0)
            if (position != null) {
                setDataFromUpdateProduct(data, position)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onClickPlus(qty: Double, position: Int) {
        saleItem?.get(position)?.quantity = saleItem?.get(position)?.quantity?.plus(1.0)
        saleItem?.get(position)?.subtotal =
            saleItem?.get(position)?.quantity?.times((saleItem?.get(position)?.unit_price ?: 0.0))
        adapter.notifyDataSetChanged()
        sumTotal()
    }

    override fun onClickMinus(qty: Double, position: Int) {
        val quantity = saleItem?.get(position)?.quantity?.minus(1.0)
        if (quantity != null) {
            if (quantity < 1) {
                myDialog.confirmation(getString(R.string.txt_are_you_sure), this)
                myDialog.listenerPositif = {
                    saleItem?.get(position)?.quantity = quantity
                    saleItem?.removeAt(position)
                    adapter.notifyDataSetChanged()
                    sumTotal()
                }
                myDialog.listenerNegatif = {
                    adapter.notifyDataSetChanged()
                    sumTotal()
                }
            } else {
                saleItem?.get(position)?.quantity = quantity
                saleItem?.get(position)?.subtotal =
                    saleItem?.get(position)?.quantity?.times((saleItem?.get(position)?.unit_price ?: 0.0))

                adapter.notifyDataSetChanged()
                sumTotal()
            }
        }
    }

    override fun onClickEdit(saleItem: SaleItem, position: Int) {
        val intent = Intent(this, EditProductSalesActivity::class.java)
        intent.putExtra(KEY_SALE_ITEM, saleItem)
        intent.putExtra("position", position)
        startActivityForResult(intent, 2)
    }

    override fun onChange(position: Int) {
        val newSaleItem = saleItem?.get(position)
        myDialog.qty(
            newSaleItem?.product_name ?: "",
            getString(R.string.txt_sale_quantity),
            newSaleItem?.quantity?.toInt() ?: 0,
            this,
            newSaleItem?.product_unit_code )
        myDialog.listenerPositifNote = { qty ->

            if (TextUtils.isEmpty(qty)){
                myDialog.alert(getString(R.string.txt_alert_must_more_than_one), this)
            }else{
                val newQty = qty.toDouble()
                if (newQty < 1) {
                    myDialog.alert(getString(R.string.txt_alert_must_more_than_one), this)
                }else{
                    saleItem?.get(position)?.quantity = newQty
                    saleItem?.get(position)?.subtotal =
                        newQty.times((saleItem?.get(position)?.unit_price ?: 0.0))
                    adapter.notifyDataSetChanged()
                    sumTotal()
                }
            }
        }
    }

    override fun onBackPressed() {
        myDialog.confirmation(getString(R.string.txt_are_you_sure), this)
        myDialog.listenerPositif = {
            super.onBackPressed()
        }
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
        val product = data.getParcelableExtra<Product>(KEY_SALE_ITEM) ?: return
        val si = SaleItem()
        si.product_id = product.id.toInt()
        si.product_code = product.code
        si.product_name = product.name
        si.net_unit_price = product.price?.toDouble()
        si.unit_price = product.price?.toDouble()
        si.quantity = 1.0
        si.subtotal = (si.quantity ?: 0.0) * (si.unit_price ?: 0.0)
        var cek = true
        for (x in 0 until saleItem?.size!!) {
            if (product.code == saleItem?.get(x)?.product_code) {
                cek = false
            }
        }
        if (cek)
            saleItem!!.add(si)
        else
            Toast.makeText(this, getString(R.string.txt_alert_duplcate_item), Toast.LENGTH_SHORT).show()
        setupRecycleView(saleItem!!)
    }

    private fun setDataFromUpdateProduct(data: Intent, position: Int) {
        val si = data.getParcelableExtra<SaleItem>(KEY_SALE_ITEM)
        saleItem?.let {
            saleItem?.get(position)?.quantity = si?.quantity
            saleItem?.get(position)?.discount = si?.discount
            saleItem?.get(position)?.unit_price = si?.unit_price
            saleItem?.get(position)?.subtotal = si?.unit_price?.times(si.quantity ?: 1.0)
        }
        saleItem?.let { setupRecycleView(it) }
    }

    private fun actionEditSale() {
        val validation = validationFormAddSale()
        if (validation[KEY_VALIDATION_REST] as Boolean) {
            val saleItems = saleItem?.map {
                return@map mutableMapOf(
                    "product_id" to it.product_id.toString(),
                    "price" to it.real_unit_price.toString(),
                    "quantity" to it.quantity,
                    "discount" to it.discount
                )
            }

            val body: MutableMap<String, Any?> = mutableMapOf(
                "date" to (et_date_edit_sale?.tag?.toString() ?: ""),
                "customer" to (idCustomer ?: ""),
                "warehouse" to (idWarehouse ?: ""),
                "order_discount" to (et_discount_edit_sale?.tag?.toString() ?: ""),
                "shipping" to (et_shipping_edit_sale?.tag?.toString() ?: ""),
                "sale_status" to (rg_status_edit_sale?.tag?.toString() ?: ""),
                "payment_term" to (termOfPayment ?: ""),
                "staff_note" to (et_staff_note_edit_sale?.text?.toString() ?: ""),
                "note" to (et_note_edit_sale?.text?.toString() ?: ""),
                "reason" to (et_reason_edit_sale?.text?.toString() ?: ""),
                "products" to saleItems
            )
            sale?.id?.let { viewModel.setIdSalesBooking(it) }
            viewModel.postEditSale(body) {
                progressBar.dialog.dismiss()
                val returnIntent = Intent()
                returnIntent.putExtra("sale_status", rg_status_edit_sale.tag.toString())
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        } else {
            myDialog.alert(validation[KEY_MESSAGE] as String, this)
        }
    }

    private fun validationFormAddSale(): Map<String, Any?> {
        var message = ""
        var cek = true
        if ((saleItem?.size ?: 0) < 1) {
            message += "- ${getString(R.string.txt_alert_item_sale)}\n"
            cek = false
        }
        if (idCustomer == null) {
            message += "- ${getString(R.string.txt_alert_id_customer)}\n"
            cek = false
        }

        return mapOf(KEY_MESSAGE to message, KEY_VALIDATION_REST to cek)
    }
}