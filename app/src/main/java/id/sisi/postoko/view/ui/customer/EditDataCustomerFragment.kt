package id.sisi.postoko.view.ui.customer

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.extensions.setIfExist
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import kotlinx.android.synthetic.main.fragment_edit_data_customer.*
import kotlinx.android.synthetic.main.fragment_edit_data_customer.view.*
import java.util.ArrayList

class EditDataCustomerFragment : BaseFragment() {
    private lateinit var viewModelCustomer: CustomerViewModel
    private lateinit var mViewModelDaerah: DaerahViewModel

    private var listCustomerGroup: List<CustomerGroup> = ArrayList()
    private var listPriceGroup: List<PriceGroup> = ArrayList()
    private var idCustomerGroup: String? = null
    private var idPriceGroup: String? = null
    private val progressBar = CustomProgressBar()
    private var customer: Customer? = null
    private var provinceList: Array<String> = arrayOf()
    private var cityList: Array<String> = arrayOf()
    private var villageList: Array<String> = arrayOf()

    companion object {
        fun newInstance() = EditDataCustomerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override var tagName: String
        get() = "Pelanggan"
        set(_) {}

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        var view = inflater.inflate(R.layout.fragment_edit_data_customer, container, false)

        customer = (activity as EditCustomerActivity).customer

        view.et_company_name_edit_customer.setText(customer?.company)
        view.et_name_edit_customer.setText(customer?.company)

        view.et_email_edit_customer.setText(customer?.email)
        view.et_phone_edit_customer.setText(customer?.phone)
        view.et_address_edit_customer.setText(customer?.address)

        view.et_postal_code_edit_customer.setText(customer?.postal_code)
        view.et_npwp_edit_customer.setText(customer?.vat_no)
        view.et_cf1_edit_customer.setText(customer?.cf1)
        view.et_cf2_edit_customer.setText(customer?.cf2)
        view.et_cf3_edit_customer.setText(customer?.cf3)
        view.et_cf4_edit_customer.setText(customer?.cf4)
        view.et_cf5_edit_customer.setText(customer?.cf5)

        view.rg_status_edit_customer
        val adapterCustomerGroup = MySpinnerAdapter(activity!!.applicationContext, android.R.layout.simple_spinner_dropdown_item)

        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCustomer.getListCustomerGroup()
        viewModelCustomer.getListCustomerGroups().observe(viewLifecycleOwner, Observer{
            it?.let {
                adapterCustomerGroup.udpateView(it.map {cg->
                    return@map DataSpinner(cg.name, cg.id)
                }.toMutableList())
                /*view.sp_customer_group_edit_customer.setIfExist(customer?.customer_group_id.toString())*/
                listCustomerGroup = it
            }
        })

        view.sp_customer_group_edit_customer.adapter = adapterCustomerGroup
        view.sp_customer_group_edit_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        val adapterPriceGroup = MySpinnerAdapter(activity!!.applicationContext, android.R.layout.simple_spinner_dropdown_item)
        viewModelCustomer.getListPriceGroup()
        viewModelCustomer.getListPriceGroups().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterPriceGroup.udpateView(it.map {pg->
                    return@map DataSpinner(pg.name ?: "~", pg.id.toString())
                }.toMutableList())
                listPriceGroup = it
            }
        })

        view.sp_price_group_edit_customer.adapter = adapterPriceGroup
        view.sp_price_group_edit_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        view.sp_provinsi_group_edit_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getCity(provinceList[position])
            }
        }

        view.sp_city_group_edit_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getStates(cityList[position])
            }
        }

        mViewModelDaerah = ViewModelProvider(this).get(DaerahViewModel::class.java)

        mViewModelDaerah.getAllProvince().observe(viewLifecycleOwner, Observer {
            it?.let {listDataDaerah ->
                provinceList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.province_name ?: ""
                }.toTypedArray()
            }
            setUIProvince(view)
        })

        mViewModelDaerah.getAllCity().observe(viewLifecycleOwner, Observer {
            it?.let {listDataDaerah ->
                cityList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.kabupaten_name ?: ""
                }.toTypedArray()
            }
            setUICity(view)
        })

        mViewModelDaerah.getAllStates().observe(viewLifecycleOwner, Observer {
            it?.let {listDataDaerah ->
                villageList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.kecamatan_name ?: ""
                }.toTypedArray()
            }
            setUIStates(view)
        })

        mViewModelDaerah.getProvince()


        /*listProvinsi.add(DataSpinner("NANGGROE ACEH DARUSSALAM", "NANGGROE ACEH DARUSSALAM"))
        listProvinsi.add(DataSpinner("SUMATERA UTARA", "SUMATERA UTARA"))

        val adapterProvonsi = MySpinnerAdapter(activity!!.applicationContext, android.R.layout.simple_list_item_1, listProvinsi)
        view.sp_provinsi_group_edit_customer.adapter = adapterProvonsi
        //view.sp_provinsi_group_edit_customer.setIfExist(customer?.country ?: null.toString())

        listKabupaten.add(DataSpinner("ACEH SINGKIL", "ACEH SINGKIL"))
        listKabupaten.add(DataSpinner("PIDIE", "PIDIE"))

        val adapterKabupaten = MySpinnerAdapter(activity!!.applicationContext, android.R.layout.simple_list_item_1, listKabupaten)
        view.sp_city_group_edit_customer.adapter = adapterKabupaten
        //view.sp_city_group_edit_customer.setIfExist(customer?.city ?: null.toString())

        listKecamatan.add(DataSpinner("SINGKOHOR", "SINGKOHOR"))
        listKecamatan.add(DataSpinner("LAWE ALAS", "LAWE ALAS"))

        val adapterKecamatan = MySpinnerAdapter(activity!!.applicationContext, android.R.layout.simple_list_item_1, listKecamatan)
        view.sp_district_group_edit_customer.adapter = adapterKecamatan
        //view.sp_district_group_edit_customer.setIfExist(customer?.state ?: null.toString())*/

        for (i in 0 until (view.rg_status_edit_customer?.childCount ?: 0)) {
            (view.rg_status_edit_customer?.get(i) as? RadioButton)?.tag = i
        }

        view.rg_status_edit_customer?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            view.rg_status_edit_customer?.tag = radioButton.tag
        }
        view.rg_status_edit_customer?.check(view.rg_status_edit_customer?.get(1)?.id ?: 0)

        view.btn_confirmation_edit_customer.setOnClickListener {
            actionEditCustomer(view)
        }

        return view
    }

    private fun setUIProvince(view2: View){
        view2.sp_provinsi_group_edit_customer.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                provinceList
            )
        }
    }

    private fun setUICity(view2: View){
        view2.sp_city_group_edit_customer.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                cityList
            )
        }
    }

    private fun setUIStates(view2: View){
        view2.sp_district_group_edit_customer.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                villageList
            )
        }
    }

    private fun actionEditCustomer(view2: View) {
        val numbersMap =  validationFormEditCustomer()

        if (numbersMap["type"] as Boolean){
            progressBar.show(activity!!.applicationContext, "Silakan tunggu...")

            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (view2.et_name_edit_customer?.text?.toString() ?: ""),
                "email" to (view2.et_email_edit_customer?.text?.toString() ?: ""),
                "customer_group_id" to (idCustomerGroup ?: ""),
                "price_group_id" to (idPriceGroup ?: ""),
                "company" to (view2.et_company_name_edit_customer?.text?.toString() ?: ""),
                "address" to (view2.et_address_edit_customer?.text?.toString() ?: ""),
                "vat_no" to (view2.et_npwp_edit_customer?.text?.toString() ?: ""),
                "postal_code" to (view2.et_postal_code_edit_customer?.text?.toString() ?: ""),
                "phone" to (view2.et_phone_edit_customer?.text?.toString() ?: ""),
                "cf1" to (view2.et_cf1_edit_customer?.text?.toString() ?: ""),
                "cf2" to (view2.et_cf2_edit_customer?.text?.toString() ?: ""),
                "cf3" to (view2.et_cf3_edit_customer?.text?.toString() ?: ""),
                "cf4" to (view2.et_cf4_edit_customer?.text?.toString() ?: ""),
                "cf5" to (view2.et_cf5_edit_customer?.text?.toString() ?: ""),
                "provinsi" to (view2.sp_provinsi_group_edit_customer?.selectedItem?.toString() ?: ""),
                "kabupaten" to (view2.sp_city_group_edit_customer?.selectedItem?.toString() ?: ""),
                "kecamatan" to (view2.sp_district_group_edit_customer?.selectedItem?.toString() ?: "")
            )
            customer?.id?.let { viewModelCustomer.setIdCustomer(it) }
            viewModelCustomer.postEditSale(body){
                progressBar.dialog.dismiss()
                Toast.makeText(activity!!.applicationContext, ""+it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    /*val returnIntent = Intent()
                    activity!!.setResult(Activity.RESULT_OK, returnIntent)
                    activity!!.finish()*/
                }
            }
        }else{
            AlertDialog.Builder(activity!!.applicationContext)
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

        if (view!!.et_company_name_edit_customer?.text.toString() == ""){
            message += "- Nama Perusahaan/Toko Tidak Boleh Kosong\n"
            cek = false
        }

        if (view!!.et_name_edit_customer?.text.toString() == ""){
            message += "- Nama Pemilik Tidak Boleh Kosong\n"
            cek = false
        }

        if (idCustomerGroup == null){
            message += "- Customer Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (view!!.et_phone_edit_customer?.text.toString() == ""){
            message += "- No Telp Tidak Boleh Kosong\n"
            cek = false
        }

        if (view!!.et_address_edit_customer?.text.toString() == ""){
            message += "- Alamat Tidak Boleh Kosong\n"
            cek = false
        }

        if (view!!.sp_provinsi_group_edit_customer?.selectedItem.toString() == ""){
            message += "- Provinsi Tidak Boleh Kosong\n"
            cek = false
        }

        if (view!!.sp_city_group_edit_customer?.selectedItem.toString() == ""){
            message += "- Kabupaten/Kota Tidak Boleh Kosong\n"
            cek = false
        }

        if (view!!.sp_district_group_edit_customer?.selectedItem.toString() == ""){
            message += "- Kecamatan Tidak Boleh Kosong\n"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }
}
