package id.sisi.postoko.view.ui.customer

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.tiper.MaterialSpinner

import id.sisi.postoko.R
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import kotlinx.android.synthetic.main.fragment_add_data_customer.view.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.ArrayList

class AddDataCustomerFragment : BaseFragment() {
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

    private var imageUri: Uri? = null
    private var requestBody: RequestBody? = null
    private var requestPart: MultipartBody.Part? = null

    companion object {
        fun newInstance() = AddDataCustomerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override var tagName: String
        get() = "Pelanggan"
        set(_) {}

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        var view = inflater.inflate(R.layout.fragment_add_data_customer, container, false)

        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCustomer.getListCustomerGroup()
        viewModelCustomer.getListCustomerGroups().observe(viewLifecycleOwner, Observer {
            it?.let {
                for (x in it.indices)
                    listCustomerGroupName.add(it[x].name)
                listCustomerGroup = it
            }
        })

        viewModelCustomer.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity!!.applicationContext, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModelCustomer.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {

                progressBar.show(activity!!.applicationContext, getString(R.string.txt_please_wait))
            } else {
                progressBar.dialog.dismiss()
            }
        })

        val adapterCustomerGroup = ArrayAdapter(activity!!.applicationContext, android.R.layout.simple_spinner_dropdown_item, listCustomerGroupName)
        view.sp_customer_group_add_customer.adapter = adapterCustomerGroup
        view.sp_customer_group_add_customer.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                idCustomerGroup = listCustomerGroup[position].id
            }
            override fun onNothingSelected(parent: MaterialSpinner): Unit = Unit
        }


        viewModelCustomer.getListPriceGroup()
        viewModelCustomer.getListPriceGroups().observe(viewLifecycleOwner, Observer {
            it?.let {
                logE("price goup $it")
                for (x in it.indices)
                    listPriceGroupName.add(it[x].name ?: "~")
                listPriceGroup = it
            }
        })

        val adapterPriceGroup = ArrayAdapter(activity!!.applicationContext, android.R.layout.simple_spinner_dropdown_item, listPriceGroupName)
        view.sp_price_group_add_customer.adapter = adapterPriceGroup
        view.sp_price_group_add_customer.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                idPriceGroup = listPriceGroup[position].id.toString()
            }
            override fun onNothingSelected(parent: MaterialSpinner): Unit = Unit
        }

        view.sp_provinsi_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getCity(provinceList[position])
            }
        }

        view.sp_district_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getStates(cityList[position])
            }
        }

        view.btn_confirmation_add_customer.setOnClickListener {
            actionAddCustomer(view)
        }

        for (i in 0 until (view.rg_status_add_customer?.childCount ?: 0)) {
            (view.rg_status_add_customer?.get(i) as? RadioButton)?.tag = i
        }

        view.rg_status_add_customer?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            view.rg_status_add_customer?.tag = radioButton.tag
        }
        view.rg_status_add_customer?.check(view.rg_status_add_customer?.get(1)?.id ?: 0)

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

        return view
    }

    private fun setUIProvince(view2: View){
        view2.sp_provinsi_group_add_customer.adapter = ArrayAdapter(
            activity!!.applicationContext,
            R.layout.support_simple_spinner_dropdown_item,
            provinceList
        )
    }

    private fun setUICity(view2: View){
        view2.sp_district_group_add_customer.adapter = ArrayAdapter(
            activity!!.applicationContext,
            R.layout.support_simple_spinner_dropdown_item,
            cityList
        )
    }

    private fun setUIStates(view2: View){
        view2.sp_city_group_add_customer.adapter = ArrayAdapter(
            activity!!.applicationContext,
            R.layout.support_simple_spinner_dropdown_item,
            villageList
        )
    }

    private fun actionAddCustomer(view2: View) {
        val numbersMap =  validationFormAddCustomer(view2)

        if (numbersMap["type"] as Boolean){
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (view2.et_name_add_customer?.text?.toString() ?: ""),
                "email" to (view2.et_email_add_customer?.text?.toString() ?: ""),
                "customer_group_id" to (idCustomerGroup ?: ""),
                "price_group_id" to (idPriceGroup ?: ""),
                "company" to (view2.et_company_name_add_customer?.text?.toString() ?: ""),
                "address" to (view2.et_address_add_customer?.text?.toString() ?: ""),
                "vat_no" to (view2.et_npwp_add_customer?.text?.toString() ?: ""),
                "postal_code" to (view2.et_postal_code_add_customer?.text?.toString() ?: ""),
                "phone" to (view2.et_phone_add_customer?.text?.toString() ?: ""),
                "cf1" to (view2.et_cf1_add_customer?.text?.toString() ?: ""),
                "cf2" to (view2.et_cf2_add_customer?.text?.toString() ?: ""),
                "cf3" to (view2.et_cf3_add_customer?.text?.toString() ?: ""),
                "cf4" to (view2.et_cf4_add_customer?.text?.toString() ?: ""),
                "cf5" to (view2.et_cf5_add_customer?.text?.toString() ?: ""),
                "province" to (view2.sp_provinsi_group_add_customer?.selectedItem?.toString() ?: ""),
                "city" to (view2.sp_district_group_add_customer?.selectedItem?.toString() ?: ""),
                "state" to (view2.sp_city_group_add_customer?.selectedItem?.toString() ?: "")

            )
            viewModelCustomer.postAddCustomer(body, requestPart){
                val returnIntent = Intent()
                activity!!.setResult(Activity.RESULT_OK, returnIntent)
                activity!!.finish()
            }
        }else{
            androidx.appcompat.app.AlertDialog.Builder(activity!!.applicationContext)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    private fun validationFormAddCustomer(view2: View): Map<String, Any?> {
        var message = ""
        var cek = true

        if (view2.et_company_name_add_customer?.text.toString() == ""){
            message += "- Nama Perusahaan/Toko Tidak Boleh Kosong\n"
            cek = false
        }

        if (view2.et_name_add_customer?.text.toString() == ""){
            message += "- Nama Pemilik Tidak Boleh Kosong\n"
            cek = false
        }

        if (idCustomerGroup == null){
            message += "- Customer Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (view2.et_phone_add_customer?.text.toString() == ""){
            message += "- No Telp Tidak Boleh Kosong\n"
            cek = false
        }

        if (view2.et_address_add_customer?.text.toString() == ""){
            message += "- Alamat Tidak Boleh Kosong\n"
            cek = false
        }

        if (view2.sp_provinsi_group_add_customer?.selectedItem.toString() == ""){
            message += "- Provinsi Tidak Boleh Kosong\n"
            cek = false
        }

        if (view2.sp_district_group_add_customer?.selectedItem.toString() == ""){
            message += "- Kabupaten/Kota Tidak Boleh Kosong\n"
            cek = false
        }

        if (view2.sp_city_group_add_customer?.selectedItem.toString() == ""){
            message += "- Kecamatan Tidak Boleh Kosong\n"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }
}
