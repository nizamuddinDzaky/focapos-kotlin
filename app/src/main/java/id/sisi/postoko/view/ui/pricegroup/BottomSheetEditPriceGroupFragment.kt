package id.sisi.postoko.view.ui.pricegroup

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.extensions.setIfExist
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_price_group.*
import java.util.*


class BottomSheetEditPriceGroupFragment : BottomSheetDialogFragment() {
    private lateinit var vmPriceGroup: PriceGroupViewModel
    private lateinit var vmWarehouse: WarehouseViewModel
    private var idWarehouse: String? = null
    var listener: (Boolean) -> Unit = {}
    private var listWarehouse: List<Warehouse> = ArrayList()
    private var priceGroup: PriceGroup? = null
    private val progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_edit_price_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }

        vmPriceGroup = ViewModelProvider(this).get(PriceGroupViewModel::class.java)
        priceGroup = arguments?.getParcelable<PriceGroup>(KEY_PRICE_GROUP)?.also { priceGroup ->
            et_price_group_name?.setText(priceGroup.name)
        }

        idWarehouse = priceGroup?.warehouse_id?.toString()

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
                listWarehouse = it
                sp_price_group_warehouse?.setIfExist(idWarehouse)
            }
        })

        sp_price_group_warehouse.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(listWarehouse.size > 0){
                    idWarehouse = listWarehouse[(position-1)].id
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        btn_action_submit.setOnClickListener {
            actionEditPriceGroup()
        }
    }

    private fun actionEditPriceGroup() {
        val numbersMap = validationEditPriceGroup()
        if (numbersMap["type"] as Boolean){
            context?.let { progressBar.show(it, "Silakan tunggu...") }
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_price_group_name?.text?.toString() ?: ""),
                "warehouse_id" to (idWarehouse?: "")
            )

            vmPriceGroup.putEditPriceGroup(body,priceGroup?.id.toString()){
                Toast.makeText(context, "" + it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    progressBar.dialog.dismiss()
                    this.dismiss()
                    listener(true)
                }
            }
        }else{
            AlertDialog.Builder(context)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    private fun validationEditPriceGroup(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (TextUtils.isEmpty(et_price_group_name.text)){
            message += "- Nama Price Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (idWarehouse == null || idWarehouse == ""){
            message += "- Warehouse Tidak Boleh Kosong\n"
            cek = false
        }
        return mapOf("message" to message, "type" to cek)
    }

    companion object {
        var listener: () -> Unit = {}
        fun show(
            fragmentManager: FragmentManager,
            priceGroup: PriceGroup
        ) {
            val bottomSheetFragment = BottomSheetEditPriceGroupFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_PRICE_GROUP, priceGroup)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
            bottomSheetFragment.listener={
                listener()
            }
        }
    }
}