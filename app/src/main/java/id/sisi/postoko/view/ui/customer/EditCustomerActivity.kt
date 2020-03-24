package id.sisi.postoko.view.ui.customer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tiper.MaterialSpinner
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setIfExist
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.activity_edit_customer.*
import kotlinx.android.synthetic.main.content_edit_customer.*
//import sun.awt.windows.ThemeReader.getPosition
import java.util.*


class EditCustomerActivity : AppCompatActivity() {
    private lateinit var viewModelCustomer: CustomerViewModel
    private var listCustomerGroup: List<CustomerGroup> = ArrayList()
    private var listPriceGroup: List<PriceGroup> = ArrayList()
    private var idCustomerGroup: String? = null
    private var idPriceGroup: String? = null
    private val progressBar = CustomProgressBar()
    private var customer: Customer? = null
    private val listProvinsi = mutableListOf<DataSpinner>()
    private val listKabupaten = mutableListOf<DataSpinner>()
    private val listKecamatan = mutableListOf<DataSpinner>()

    @SuppressLint("ResourceType")
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

        et_postal_code_edit_customer.setText(customer?.postal_code)
        et_npwp_edit_customer.setText(customer?.vat_no)
        et_cf1_edit_customer.setText(customer?.cf1)
        et_cf2_edit_customer.setText(customer?.cf2)
        et_cf3_edit_customer.setText(customer?.cf3)
        et_cf4_edit_customer.setText(customer?.cf4)
        et_cf5_edit_customer.setText(customer?.cf5)

        rg_status_edit_customer
        val adapterCustomerGroup = MySpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item)

        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCustomer.getListCustomerGroup()
        viewModelCustomer.getListCustomerGroups().observe(this, Observer{
            it?.let {
                adapterCustomerGroup.udpateView(it.map {cg->
                    return@map DataSpinner(cg.name, cg.id)
                }.toMutableList())
                listCustomerGroup = it
            }
        })


        sp_customer_group_edit_customer.adapter = adapterCustomerGroup
        sp_customer_group_edit_customer.setIfExist(customer?.customer_group_id.toString())
        sp_customer_group_edit_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                idCustomerGroup = listCustomerGroup[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        val adapterPriceGroup = MySpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        viewModelCustomer.getListPriceGroup()
        viewModelCustomer.getListPriceGroups().observe(this, Observer {
            it?.let {
                adapterPriceGroup.udpateView(it.map {pg->
                    return@map DataSpinner(pg.name, pg.id.toString())
                }.toMutableList())
                listPriceGroup = it
            }
        })

        sp_price_group_edit_customer.adapter = adapterPriceGroup
        sp_price_group_edit_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                idPriceGroup = listPriceGroup[position].id.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        listProvinsi.add(DataSpinner("NANGGROE ACEH DARUSSALAM", "NANGGROE ACEH DARUSSALAM"))
        listProvinsi.add(DataSpinner("SUMATERA UTARA", "SUMATERA UTARA"))

        val adapterProvonsi = MySpinnerAdapter(this, android.R.layout.simple_list_item_1, listProvinsi)
        sp_provinsi_group_edit_customer.adapter = adapterProvonsi
        sp_provinsi_group_edit_customer.setIfExist(customer?.country ?: null.toString())

        listKabupaten.add(DataSpinner("ACEH SINGKIL", "ACEH SINGKIL"))
        listKabupaten.add(DataSpinner("PIDIE", "PIDIE"))

        val adapterKabupaten = MySpinnerAdapter(this, android.R.layout.simple_list_item_1, listKabupaten)
        sp_city_group_edit_customer.adapter = adapterKabupaten
        sp_city_group_edit_customer.setIfExist(customer?.city ?: null.toString())

        listKecamatan.add(DataSpinner("SINGKOHOR", "SINGKOHOR"))
        listKecamatan.add(DataSpinner("LAWE ALAS", "LAWE ALAS"))

        val adapterKecamatan = MySpinnerAdapter(this, android.R.layout.simple_list_item_1, listKecamatan)
        sp_district_group_edit_customer.adapter = adapterKecamatan
        sp_district_group_edit_customer.setIfExist(customer?.state ?: null.toString())

        for (i in 0 until (rg_status_edit_customer?.childCount ?: 0)) {
            (rg_status_edit_customer?.get(i) as? RadioButton)?.tag = i
        }

        rg_status_edit_customer?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_edit_customer?.tag = radioButton.tag
        }
        rg_status_edit_customer?.check(rg_status_edit_customer?.get(1)?.id ?: 0)

        btn_confirmation_edit_customer.setOnClickListener { 
            actionEditCustomer()
        }
    }

    private fun actionEditCustomer() {
        val numbersMap =  validationFormEditCustomer()

        if (numbersMap["type"] as Boolean){
            progressBar.show(this, "Silakan tunggu...")

            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_name_edit_customer?.text?.toString() ?: ""),
                "email" to (et_email_edit_customer?.text?.toString() ?: ""),
                "customer_group_id" to (idCustomerGroup ?: ""),
                "price_group_id" to (idPriceGroup ?: ""),
                "company" to (et_company_name_edit_customer?.text?.toString() ?: ""),
                "address" to (et_address_edit_customer?.text?.toString() ?: ""),
                "vat_no" to (et_npwp_edit_customer?.text?.toString() ?: ""),
                "postal_code" to (et_postal_code_edit_customer?.text?.toString() ?: ""),
                "phone" to (et_phone_edit_customer?.text?.toString() ?: ""),
                "cf1" to (et_cf1_edit_customer?.text?.toString() ?: ""),
                "cf2" to (et_cf2_edit_customer?.text?.toString() ?: ""),
                "cf3" to (et_cf3_edit_customer?.text?.toString() ?: ""),
                "cf4" to (et_cf4_edit_customer?.text?.toString() ?: ""),
                "cf5" to (et_cf5_edit_customer?.text?.toString() ?: ""),
                "provinsi" to (sp_provinsi_group_edit_customer?.selectedItem?.toString() ?: ""),
                "kabupaten" to (sp_district_group_edit_customer?.selectedItem?.toString() ?: ""),
                "kecamatan" to (sp_city_group_edit_customer?.selectedItem?.toString() ?: "")
            )
            customer?.id?.let { viewModelCustomer.setIdCustomer(it) }
            viewModelCustomer.postEditSale(body){
                progressBar.dialog.dismiss()
                Toast.makeText(this, ""+it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    val returnIntent = Intent()
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }else{
            AlertDialog.Builder(this@EditCustomerActivity)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    private fun validationFormEditCustomer(): Map<String, Any?> {
        var message = ""
        var cek = true

        if (et_company_name_edit_customer?.text.toString() == ""){
            message += "- Nama Perusahaan/Toko Tidak Boleh Kosong\n"
            cek = false
        }

        if (et_name_edit_customer?.text.toString() == ""){
            message += "- Nama Pemilik Tidak Boleh Kosong\n"
            cek = false
        }

        if (idCustomerGroup == null){
            message += "- Customer Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (et_phone_edit_customer?.text.toString() == ""){
            message += "- No Telp Tidak Boleh Kosong\n"
            cek = false
        }

        if (et_address_edit_customer?.text.toString() == ""){
            message += "- Alamat Tidak Boleh Kosong\n"
            cek = false
        }

        if (sp_provinsi_group_edit_customer?.selectedItem.toString() == ""){
            message += "- Provinsi Tidak Boleh Kosong\n"
            cek = false
        }

        if (sp_district_group_edit_customer?.selectedItem.toString() == ""){
            message += "- Kabupaten/Kota Tidak Boleh Kosong\n"
            cek = false
        }

        if (sp_city_group_edit_customer?.selectedItem.toString() == ""){
            message += "- Kecamatan Tidak Boleh Kosong\n"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }
}
