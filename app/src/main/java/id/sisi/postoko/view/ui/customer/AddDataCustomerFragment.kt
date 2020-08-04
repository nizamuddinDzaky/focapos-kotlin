package id.sisi.postoko.view.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.customergroup.CustomerGroupViewModel
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import id.sisi.postoko.view.ui.pricegroup.PriceGroupViewModel
import kotlinx.android.synthetic.main.fragment_add_data_customer.view.*
import java.util.*

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

    override var tagName: String
        get() = "Pelanggan"
        set(_) {}

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_add_data_customer, container, false)

//        setup cutomer group
        val adapterCustomerGroup = context?.let {
            MySpinnerAdapter(
                it,
                R.layout.list_spinner
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
                R.layout.list_spinner
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

                if((position-1) >= 0){
                    (activity as AddCustomerActivity).provinsiSelected = provinceList[position-1]
                    mViewModelDaerah.getCity(provinceList[position-1]).toString()
                }else{
                    (activity as AddCustomerActivity).provinsiSelected = null
                }
            }
        }

        view.sp_district_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if((position-1) >= 0){
                    (activity as AddCustomerActivity).districtSelected = cityList[position-1]
                    mViewModelDaerah.getStates(cityList[position-1])
                }else{
                    (activity as AddCustomerActivity).districtSelected = null
                }
            }
        }

        view.sp_city_group_add_customer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if((position-1) >= 0){
                    (activity as AddCustomerActivity).stateSelected = villageList[position-1]
                }else{
                    (activity as AddCustomerActivity).stateSelected = null
                }
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
        setUICity(view)
        setUIStates(view)
        return view
    }

    private fun setUIProvince(view2: View){
        val adapter = context?.let {
            MySpinnerAdapter(
                it,
                R.layout.list_spinner
            )
        }
        provinceList.let {
            adapter?.udpateView(it.map { prov->
                return@map DataSpinner(prov , prov)
            }.toMutableList(), hasHeader = getString(R.string.txt_choose_province))
        }
        view2.sp_provinsi_group_add_customer.adapter = adapter
    }

    private fun setUICity(view2: View){
        val adapter = context?.let {
            MySpinnerAdapter(
                it,
                R.layout.list_spinner
            )
        }

        cityList.let {
            adapter?.udpateView(it.map { city->
                return@map DataSpinner(city , city)
            }.toMutableList(), hasHeader = getString(R.string.txt_choose_district))
        }
        view2.sp_district_group_add_customer.adapter = adapter
    }

    private fun setUIStates(view2: View){
        val adapter = context?.let {
            MySpinnerAdapter(
                it,
                R.layout.list_spinner
            )
        }

        villageList.let {
            adapter?.udpateView(it.map { state->
                return@map DataSpinner(state , state)
            }.toMutableList(), hasHeader = getString(R.string.txt_choose_state))
        }
        view2.sp_city_group_add_customer.adapter = adapter
    }


}
