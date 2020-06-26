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
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setIfExist
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.customergroup.CustomerGroupViewModel
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import id.sisi.postoko.view.ui.pricegroup.PriceGroupViewModel
import kotlinx.android.synthetic.main.fragment_add_data_customer.view.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.ArrayList

class AddDataCustomerFragment : BaseFragment() {

    private lateinit var mViewModelDaerah: DaerahViewModel
    private lateinit var mViewModelCustomerGroup: CustomerGroupViewModel
    private lateinit var mViewModelPriceGroup: PriceGroupViewModel

    private var listCustomerGroup: List<CustomerGroup> = ArrayList()
    private var listPriceGroup: List<PriceGroup> = ArrayList()

    private var provinceList: Array<String> = arrayOf()
    private var cityList: Array<String> = arrayOf()
    private var villageList: Array<String> = arrayOf()

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
        val view = inflater.inflate(R.layout.fragment_add_data_customer, container, false)

//        setup cutomer group
        val adapterCustomerGroup = context?.let {
            MySpinnerAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item
            )
        }
        mViewModelCustomerGroup = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)
        mViewModelCustomerGroup.getListCustomerGroup()
        mViewModelCustomerGroup.getListCustomerGroups().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterCustomerGroup?.udpateView(it.map {wh->
                    return@map DataSpinner(wh.name, wh.id)
                }.toMutableList())
                listCustomerGroup=it
            }
        })
        view.sp_customer_group_add_customer.adapter = adapterCustomerGroup
        view.sp_customer_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (activity as AddCustomerActivity).idCustomerGroup = listCustomerGroup[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        val adapterPriceGroup = context?.let {
            MySpinnerAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item
            )
        }
        mViewModelPriceGroup = ViewModelProvider(this).get(PriceGroupViewModel::class.java)
        mViewModelPriceGroup.getListPriceGroup()
        mViewModelPriceGroup.getListPriceGroups().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterPriceGroup?.udpateView(it.map {pg->
                    return@map DataSpinner(pg.name?: "", pg.id.toString())
                }.toMutableList())
                listPriceGroup=it
            }
        })

        view.sp_price_group_add_customer.adapter = adapterPriceGroup
        view.sp_price_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (activity as AddCustomerActivity).idPriceGroup = listPriceGroup[position].id.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
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

        /*view.btn_confirmation_add_customer.setOnClickListener {
            actionAddCustomer(view)
        }*/

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
            android.R.layout.simple_spinner_dropdown_item,
            provinceList
        )
    }

    private fun setUICity(view2: View){
        view2.sp_district_group_add_customer.adapter = ArrayAdapter(
            activity!!.applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            cityList
        )
    }

    private fun setUIStates(view2: View){
        view2.sp_city_group_add_customer.adapter = ArrayAdapter(
            activity!!.applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            villageList
        )
    }


}
