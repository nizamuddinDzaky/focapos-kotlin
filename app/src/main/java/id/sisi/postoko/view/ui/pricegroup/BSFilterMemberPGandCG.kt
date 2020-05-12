package id.sisi.postoko.view.ui.pricegroup

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter_price_group.*

class BSFilterMemberPGandCG: BottomSheetDialogFragment() {

    private var expanded: Boolean = false
    var listener: (Map<String, Any?>) -> Unit= {}
    private lateinit var mViewModelDaerah: DaerahViewModel
    private var provinceList: Array<String> = arrayOf()
    private var cityList: Array<String> = arrayOf()

    var countrySelected: String? = null
    var citySelected: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = R.layout.fragment_bottom_sheet_filter_price_group
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("strFilter").let {
            if (it != "null")
                et_filter_customer_name.setText(it)
            else
                et_filter_customer_name.setText("")
        }
        arguments?.getString("filter_country").let {
            countrySelected = it
            if (it != "null" && it != null){
                expanded = true
            }
            setUpExpand()
        }
        arguments?.getString("filter_city").let {
            citySelected = it
            if (it != "null" && it != null){
                expanded = true
            }
            setUpExpand()
        }


        btn_close.setOnClickListener {
            this.dismiss()
        }
        tv_reset.setOnClickListener {
            et_filter_customer_name.text = null
            listener(
                mapOf("filter" to (et_filter_customer_name.text.toString()))
            )
            this.dismiss()
        }

        setUpSpinner()

        btn_action_apply.setOnClickListener {
            if (!expanded){
                listener(
                    mapOf(
                        "filter_str" to (et_filter_customer_name.text.toString())
                    )
                )
            }else{
                listener(
                    mapOf(
                        "filter_str" to (et_filter_customer_name.text.toString()),
                        "filter_country" to (countrySelected),
                        "filter_city" to (citySelected)
                    )
                )
            }
            this.dismiss()
        }
    }

    private fun setUpExpand() {
        if (!expanded){
            expandable_layout.collapse(true)
        }else{
            expandable_layout.expand(true)
        }

        layout_filter_region.setOnClickListener {
            expanded = !expanded

            if (!expanded){
                iv_arrow_up.gone()
                iv_arrow_down.visible()
                expandable_layout.collapse(true)
            }else{
                iv_arrow_up.visible()
                iv_arrow_down.gone()
                expandable_layout.expand(true)
            }
        }
    }

    private fun setUpSpinner() {
        mViewModelDaerah = ViewModelProvider(this).get(DaerahViewModel::class.java)
        mViewModelDaerah.getAllProvince().observe(requireActivity(), Observer {
            it?.let {listDataDaerah ->
                provinceList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.province_name ?: ""
                }.toTypedArray()
            }
            setUIProvince()
        })

        mViewModelDaerah.getAllCity().observe(requireActivity(), Observer {
            it?.let {listDataDaerah ->
                cityList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.kabupaten_name ?: ""
                }.toTypedArray()
            }
            setUICity()
        })

        spinner_filter_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getCity(provinceList[position])
                countrySelected = provinceList[position]
            }
        }

        spinner_filter_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getStates(cityList[position])
                citySelected = cityList[position]
            }
        }
        mViewModelDaerah.getProvince()
    }

    private fun setUICity() {
        spinner_filter_city.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            cityList
        )
        var index = 0
        if (cityList.indexOf(citySelected) != -1){
            index = cityList.indexOf(citySelected)
        }
        spinner_filter_city.setSelection(index)
    }

    private fun setUIProvince() {
        spinner_filter_province.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            provinceList
        )

        spinner_filter_province.setSelection(provinceList.indexOf(countrySelected))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            /*setupFullHeight(bottomSheetDialog)*/
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }

    companion object {
        var listener: (Map<String, Any?>) -> Unit= {}
        fun show(
            fragmentManager: FragmentManager,
            strFilter: String?,
            countrySelected: String?,
            citySelected: String?
        ) {

            val bottomSheetFragment = BSFilterMemberPGandCG()
            bottomSheetFragment.listener = {
                listener(it)
            }
//            bottomSheetFragment.profileType = profileType
            val bundle = Bundle()
            bundle.putString("strFilter", strFilter)
            bundle.putString("filter_country", countrySelected)
            bundle.putString("filter_city", citySelected)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}