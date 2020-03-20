package id.sisi.postoko.view.ui.customer

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tiper.MaterialSpinner
import id.sisi.postoko.R
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.logE

import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.content_add_customer.*
import java.util.ArrayList

class AddCustomerActivity : AppCompatActivity() {
    private lateinit var viewModelCustomer: CustomerViewModel
    private var listCustomerGroupName = ArrayList<String>()
    private var listCustomerGroup: List<CustomerGroup> = ArrayList()
    private var listPriceGroupName = ArrayList<String>()
    private var listPriceGroup: List<PriceGroup> = ArrayList()
    private var idCustomerGroup: String? = null
    private var idPriceGroup: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        setSupportActionBar(toolbar_add_customer)
        supportActionBar?.title = null

        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCustomer.getListCustomerGroup()
        viewModelCustomer.getListCustomerGroups().observe(this, Observer {
            it?.let {
                for (x in it.indices)
                    listCustomerGroupName.add(it[x].name)
                listCustomerGroup = it
            }
        })
        val adapterCustomerGroup = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listCustomerGroupName)
        sp_customer_group_add_customer.adapter = adapterCustomerGroup
        sp_customer_group_add_customer.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                idCustomerGroup = listCustomerGroup[position].id
            }
            override fun onNothingSelected(parent: MaterialSpinner): Unit = Unit
        }


        viewModelCustomer.getListPriceGroup()
        viewModelCustomer.getListPriceGroups().observe(this, Observer {
            it?.let {
                logE("price goup $it")
                for (x in it.indices)
                    listPriceGroupName.add(it[x].name)
                listPriceGroup = it
            }
        })

        val adapterPriceGroup = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listPriceGroupName)
        sp_price_group_add_customer.adapter = adapterPriceGroup
        sp_price_group_add_customer.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
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
            sp_provinsi_group_add_customer.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.array_kabupaten,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_city_group_add_customer.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.array_kecamatan,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_district_group_add_customer.adapter = adapter
        }

        btn_confirmation_add_customer.setOnClickListener {
            actionAddCustomer()
        }
    }

    private fun actionAddCustomer() {
        val numbersMap =  validationFormAddCustomer()
    }

    private fun validationFormAddCustomer(): Map<String, Any?> {
        var message = ""
        var cek = true

        return mapOf("message" to message, "type" to cek)
    }
}
