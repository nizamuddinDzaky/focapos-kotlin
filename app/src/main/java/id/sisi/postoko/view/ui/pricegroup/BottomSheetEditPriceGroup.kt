package id.sisi.postoko.view.ui.pricegroup

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setIfExist
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_form_price_group.*
import java.util.ArrayList

class BottomSheetEditPriceGroup: BottomSheetDialogFragment() {
    private var priceGroup: PriceGroup? = null
    private lateinit var vmWarehouse: WarehouseViewModel
    private var idWarehouse: String? = null
    private var listWarehouse: List<Warehouse> = ArrayList()
    private lateinit var vmPriceGroup: PriceGroupViewModel
    private val progressBar = CustomProgressBar()
    var listener: () -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_form_price_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        priceGroup = arguments?.getParcelable(KEY_PRICE_GROUP)

        vmPriceGroup = ViewModelProvider(this).get(PriceGroupViewModel::class.java)

        vmPriceGroup.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
        vmPriceGroup.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {
                context?.let { ctx -> progressBar.show(ctx, getString(R.string.txt_please_wait)) }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        vmWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)

        tv_title_bottom_sheet.text = getString(R.string.title_activity_edit_price_group)
        tv_subtitle_bottom_sheet.visible()
        val strSubTitle = "( ${priceGroup?.name})"
        tv_subtitle_bottom_sheet.text = strSubTitle
        idWarehouse = priceGroup?.warehouse_id?.toString()

        val adapterWarehouse =
            context?.let { MySpinnerAdapter(it, android.R.layout.simple_spinner_dropdown_item) }
        adapterWarehouse?.udpateView(mutableListOf(DataSpinner(getString(R.string.txt_no_data), "")))
        sp_price_group_warehouse?.adapter = adapterWarehouse

        vmWarehouse.getListWarehouses().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterWarehouse?.udpateView(it.map { pg ->
                    return@map DataSpinner(pg.name, pg.id)
                }.toMutableList(), hasHeader = getString(R.string.txt_choose_warehouse))
                listWarehouse = it
                sp_price_group_warehouse?.setIfExist(priceGroup?.warehouse_id.toString())
            }
        })

        sp_price_group_warehouse.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                idWarehouse = if ((position-1) >= 0){
                    listWarehouse[position-1].id
                }else{
                    null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        et_price_group_name.setText(priceGroup?.name)

        btn_action_submit.setOnClickListener {
            actionEditPriceGroup()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }

    private fun actionEditPriceGroup() {
        val numbersMap = validationEditPriceGroup()
        if (numbersMap["type"] as Boolean){
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_price_group_name?.text?.toString() ?: ""),
                "warehouse_id" to (idWarehouse?: "")
            )

            vmPriceGroup.putEditPriceGroup(body,priceGroup?.id.toString()){
                this.dismiss()
                listener()
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
}