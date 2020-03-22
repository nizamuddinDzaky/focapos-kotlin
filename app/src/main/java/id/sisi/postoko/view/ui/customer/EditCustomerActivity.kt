package id.sisi.postoko.view.ui.customer

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tiper.MaterialSpinner
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.activity_edit_customer.*
import kotlinx.android.synthetic.main.content_edit_customer.*
//import sun.awt.windows.ThemeReader.getPosition
import java.util.*


class EditCustomerActivity : AppCompatActivity() {
    private lateinit var viewModelCustomer: CustomerViewModel
    private var listCustomerGroupName = ArrayList<String>()
    private var listCustomerGroup: List<CustomerGroup> = ArrayList()
    private var listPriceGroupName = ArrayList<String>()
    private var listPriceGroup: List<PriceGroup> = ArrayList()
    private var idCustomerGroup: String? = null
    private var idPriceGroup: String? = null
    private val progressBar = CustomProgressBar()
    private var customer: Customer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_customer)
        setSupportActionBar(toolbar_edit_customer)
        supportActionBar?.title = null

        customer = intent.extras?.getParcelable("customer")!!

        et_company_name_edit_customer.setText(customer?.company)
        et_name_edit_customer.setText(customer?.name)

        et_email_edit_customer.setText(customer?.email)
        et_phone_edit_customer.setText(customer?.phone)
        et_address_edit_customer.setText(customer?.address)
//        sp_provinsi_group_edit_customer
//        sp_district_group_edit_customer
//        sp_city_group_edit_customer
        et_postal_code_edit_customer.setText(customer?.postal_code)
        et_npwp_edit_customer.setText(customer?.vat_no)
        et_cf1_edit_customer.setText(customer?.cf1)
        et_cf2_edit_customer.setText(customer?.cf2)
        et_cf3_edit_customer.setText(customer?.cf3)
        et_cf4_edit_customer.setText(customer?.cf4)
        et_cf5_edit_customer.setText(customer?.cf5)

        rg_status_edit_customer
        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCustomer.getListCustomerGroup()
        viewModelCustomer.getListCustomerGroups().observe(this, Observer{
            it?.let {
                for (x in it.indices)
                    listCustomerGroupName.add(it[x].name)
                listCustomerGroup = it
            }
        })

        val adapterCustomerGroup = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listCustomerGroupName)

        sp_customer_group_edit_customer.adapter = adapterCustomerGroup
        sp_customer_group_edit_customer.selection=1

//        if (customer?.customer_group_name != null) {
//            val spinnerPosition: Int = adapterCustomerGroup.getPosition(customer?.customer_group_name)
//            logE("position : ${adapterCustomerGroup.toString()}")
//            idCustomerGroup = customer?.customer_group_id
//        }

        sp_customer_group_edit_customer.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                idCustomerGroup = listCustomerGroup[position].id
            }
            override fun onNothingSelected(parent: MaterialSpinner): Unit = Unit
        }

        viewModelCustomer.getListPriceGroup()
        viewModelCustomer.getListPriceGroups().observe(this, Observer {
            it?.let {
                for (x in it.indices)
                    listPriceGroupName.add(it[x].name)
                listPriceGroup = it
            }
        })

        val adapterPriceGroup = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listPriceGroupName)

        sp_price_group_edit_customer.adapter = adapterPriceGroup
        sp_price_group_edit_customer.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                idPriceGroup = listPriceGroup[position].id.toString()
            }
            override fun onNothingSelected(parent: MaterialSpinner): Unit = Unit
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.array_provinsi,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_provinsi_group_edit_customer.adapter = adapter
        }
        if (customer?.country != null) {
            val spinnerPosition: Int = adapterCustomerGroup.getPosition(customer?.country)
            logE("spinner $spinnerPosition ${customer?.country}")
            sp_provinsi_group_edit_customer.selection = spinnerPosition
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.array_kabupaten,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_city_group_edit_customer.adapter = adapter
        }
        if (customer?.city != null) {
            val spinnerPosition: Int = adapterCustomerGroup.getPosition(customer?.city)
            sp_city_group_edit_customer.selection = spinnerPosition
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.array_kecamatan,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_district_group_edit_customer.adapter = adapter
        }
        if (customer?.state != null) {
            val spinnerPosition: Int = adapterCustomerGroup.getPosition(customer?.state)
            sp_district_group_edit_customer.selection = spinnerPosition
        }

        for (i in 0 until (rg_status_edit_customer?.childCount ?: 0)) {
            (rg_status_edit_customer?.get(i) as? RadioButton)?.tag = i
        }

        rg_status_edit_customer?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_edit_customer?.tag = radioButton.tag
        }
        rg_status_edit_customer?.check(rg_status_edit_customer?.get(1)?.id ?: 0)
    }

}
