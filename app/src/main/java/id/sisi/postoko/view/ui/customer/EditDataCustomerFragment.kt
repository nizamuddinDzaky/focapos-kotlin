package id.sisi.postoko.view.ui.customer

import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_edit_data_customer.view.*
import java.util.ArrayList

class EditDataCustomerFragment : BaseFragment() {
    private lateinit var viewModelCustomer: CustomerViewModel
    private lateinit var mViewModelDaerah: DaerahViewModel

    private var listCustomerGroup: List<CustomerGroup> = ArrayList()
    private var listPriceGroup: List<PriceGroup> = ArrayList()

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
        val view = inflater.inflate(R.layout.fragment_edit_data_customer, container, false)

        customer = (activity as EditCustomerActivity).customer

        view.et_company_name_edit_customer.setText(customer?.company)
        view.et_name_edit_customer.setText(customer?.company)

        view.et_email_edit_customer.setText(customer?.email)
        view.et_phone_edit_customer.setText(customer?.phone)
        view.et_address_edit_customer.setText(customer?.address)

        view.et_postal_code_edit_customer.setText(customer?.postal_code)
        view.et_npwp_edit_customer.setText(customer?.vat_no)


        view.rg_status_edit_customer
        val adapterCustomerGroup = MySpinnerAdapter(activity!!.applicationContext, R.layout.list_spinner)

        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCustomer.getListCustomerGroup()
        viewModelCustomer.getListCustomerGroups().observe(viewLifecycleOwner, Observer{
            it?.let {
                adapterCustomerGroup.udpateView(it.map {cg->
                    return@map DataSpinner(cg.name, cg.id)
                }.toMutableList())
                view.sp_customer_group_edit_customer.setIfExist(customer?.customer_group_id.toString())
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
                (activity as EditCustomerActivity).idCustomerGroup = listCustomerGroup[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        val adapterPriceGroup = MySpinnerAdapter(activity!!.applicationContext, R.layout.list_spinner)
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
                (activity as EditCustomerActivity).idPriceGroup = listPriceGroup[position].id.toString()
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

        for (i in 0 until (view.rg_status_edit_customer?.childCount ?: 0)) {
            (view.rg_status_edit_customer?.get(i) as? RadioButton)?.tag = i
        }

        view.rg_status_edit_customer?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            view.rg_status_edit_customer?.tag = radioButton.tag
        }
        view.rg_status_edit_customer?.check(view.rg_status_edit_customer?.get(1)?.id ?: 0)

        /*view.btn_confirmation_edit_customer.setOnClickListener {
            actionEditCustomer(view)
        }*/

        return view
    }

    private fun setUIProvince(view2: View){
        view2.sp_provinsi_group_edit_customer.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.list_spinner,
                provinceList
            )
        }
    }

    private fun setUICity(view2: View){
        view2.sp_city_group_edit_customer.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.list_spinner,
                cityList
            )
        }
    }

    private fun setUIStates(view2: View){
        view2.sp_district_group_edit_customer.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.list_spinner,
                villageList
            )
        }
    }
}
