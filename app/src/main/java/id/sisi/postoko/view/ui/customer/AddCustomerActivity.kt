package id.sisi.postoko.view.ui.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tiper.MaterialSpinner
import id.sisi.postoko.R
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.model.User
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.content_add_customer.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_address.*
import java.util.*

class AddCustomerActivity : AppCompatActivity() {
    private lateinit var viewModelCustomer: CustomerViewModel
    private lateinit var mViewModelDaerah: DaerahViewModel

    private var listCustomerGroupName = ArrayList<String>()
    private var listCustomerGroup: List<CustomerGroup> = ArrayList()
    private var listPriceGroupName = ArrayList<String>()
    private var listPriceGroup: List<PriceGroup> = ArrayList()
    private var idCustomerGroup: String? = null
    private var idPriceGroup: String? = null
    private val progressBar = CustomProgressBar()

    private var provinceList: Array<String> = arrayOf()
    private var cityList: Array<String> = arrayOf()
    private var villageList: Array<String> = arrayOf()

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
                    listPriceGroupName.add(it[x].name ?: "~")
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

        sp_provinsi_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getCity(provinceList[position])
            }
        }

        sp_district_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getStates(cityList[position])
            }
        }

        btn_confirmation_add_customer.setOnClickListener {
            actionAddCustomer()
        }

        for (i in 0 until (rg_status_add_customer?.childCount ?: 0)) {
            (rg_status_add_customer?.get(i) as? RadioButton)?.tag = i
        }

        rg_status_add_customer?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_status_add_customer?.tag = radioButton.tag
        }
        rg_status_add_customer?.check(rg_status_add_customer?.get(1)?.id ?: 0)

        mViewModelDaerah = ViewModelProvider(this).get(DaerahViewModel::class.java)

        mViewModelDaerah.getAllProvince().observe(this, Observer {
            it?.let {listDataDaerah ->
                provinceList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.province_name ?: ""
                }.toTypedArray()
            }
            setUIProvince()
        })

        mViewModelDaerah.getAllCity().observe(this, Observer {
            it?.let {listDataDaerah ->
                cityList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.kabupaten_name ?: ""
                }.toTypedArray()
            }
            setUICity()
        })

        mViewModelDaerah.getAllStates().observe(this, Observer {
            it?.let {listDataDaerah ->
                villageList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.kecamatan_name ?: ""
                }.toTypedArray()
            }
            setUIStates()
        })

        mViewModelDaerah.getProvince()
    }

    private fun setUIProvince(){
        sp_provinsi_group_add_customer.adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            provinceList
        )
    }

    private fun setUICity(){
        sp_district_group_add_customer.adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            cityList
        )
    }

    private fun setUIStates(){
        sp_city_group_add_customer.adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            villageList
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            android.app.AlertDialog.Builder(this@AddCustomerActivity)
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

    private fun actionAddCustomer() {
        val numbersMap =  validationFormAddCustomer()

        if (numbersMap["type"] as Boolean){
            progressBar.show(this, "Silakan tunggu...")

            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_name_add_customer?.text?.toString() ?: ""),
                "email" to (et_email_add_customer?.text?.toString() ?: ""),
                "customer_group_id" to (idCustomerGroup ?: ""),
                "price_group_id" to (idPriceGroup ?: ""),
                "company" to (et_company_name_add_customer?.text?.toString() ?: ""),
                "address" to (et_address_add_customer?.text?.toString() ?: ""),
                "vat_no" to (et_npwp_add_customer?.text?.toString() ?: ""),
                "postal_code" to (et_postal_code_add_customer?.text?.toString() ?: ""),
                "phone" to (et_phone_add_customer?.text?.toString() ?: ""),
                "cf1" to (et_cf1_add_customer?.text?.toString() ?: ""),
                "cf2" to (et_cf2_add_customer?.text?.toString() ?: ""),
                "cf3" to (et_cf3_add_customer?.text?.toString() ?: ""),
                "cf4" to (et_cf4_add_customer?.text?.toString() ?: ""),
                "cf5" to (et_cf5_add_customer?.text?.toString() ?: ""),
                "province" to (sp_provinsi_group_add_customer?.selectedItem?.toString() ?: ""),
                "city" to (sp_district_group_add_customer?.selectedItem?.toString() ?: ""),
                "state" to (sp_city_group_add_customer?.selectedItem?.toString() ?: "")

            )
            viewModelCustomer.postAddCustomer(body){
                progressBar.dialog.dismiss()
                Toast.makeText(this, ""+it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    val returnIntent = Intent()
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }else{
            AlertDialog.Builder(this@AddCustomerActivity)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    private fun validationFormAddCustomer(): Map<String, Any?> {
        var message = ""
        var cek = true

        if (et_company_name_add_customer?.text.toString() == ""){
            message += "- Nama Perusahaan/Toko Tidak Boleh Kosong\n"
            cek = false
        }

        if (et_name_add_customer?.text.toString() == ""){
            message += "- Nama Pemilik Tidak Boleh Kosong\n"
            cek = false
        }

        if (idCustomerGroup == null){
            message += "- Customer Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (et_phone_add_customer?.text.toString() == ""){
            message += "- No Telp Tidak Boleh Kosong\n"
            cek = false
        }

        if (et_address_add_customer?.text.toString() == ""){
            message += "- Alamat Tidak Boleh Kosong\n"
            cek = false
        }

        if (sp_provinsi_group_add_customer?.selectedItem.toString() == ""){
            message += "- Provinsi Tidak Boleh Kosong\n"
            cek = false
        }

        if (sp_district_group_add_customer?.selectedItem.toString() == ""){
            message += "- Kabupaten/Kota Tidak Boleh Kosong\n"
            cek = false
        }

        if (sp_city_group_add_customer?.selectedItem.toString() == ""){
            message += "- Kecamatan Tidak Boleh Kosong\n"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }
}
