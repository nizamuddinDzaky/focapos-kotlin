package id.sisi.postoko.view.ui.pricegroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.extensions.setIfExist
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_price_group.*


class BottomSheetEditPriceGroupFragment : BottomSheetDialogFragment() {
    private lateinit var mViewModel: PriceGroupViewModel
    private lateinit var vmWarehouse: WarehouseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this).get(PriceGroupViewModel::class.java)
        return inflater.inflate(R.layout.fragment_bottom_sheet_edit_price_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
        val priceGroup = arguments?.getParcelable<PriceGroup>(KEY_PRICE_GROUP)?.also { priceGroup ->
            et_price_group_name?.setText(priceGroup.name)
        }
        val adapterWarehouse =
            MySpinnerAdapter(view.context, android.R.layout.simple_spinner_dropdown_item)
        adapterWarehouse.udpateView(mutableListOf(DataSpinner(getString(R.string.txt_no_data), "")))
        sp_price_group_warehouse?.adapter = adapterWarehouse
        vmWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        vmWarehouse.getListWarehouses().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterWarehouse.udpateView(it.map { pg ->
                    return@map DataSpinner(pg.name, pg.id)
                }.toMutableList(), hasHeader = getString(R.string.txt_choose_warehouse))
                sp_price_group_warehouse?.setIfExist(priceGroup?.warehouse_id?.toString())
            }
        })
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            priceGroup: PriceGroup
        ) {
            val bottomSheetFragment = BottomSheetEditPriceGroupFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_PRICE_GROUP, priceGroup)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}