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
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_form_price_group.*
import java.util.*

class BottomSheetAddPriceGroup: BottomSheetDialogFragment() {

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

        tv_title_bottom_sheet.text = getString(R.string.txt_add_price_group)
        /*vmWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)*/
        vmPriceGroup = ViewModelProvider(this).get(PriceGroupViewModel::class.java)

        /*val adapterWarehouse =
            context?.let { MySpinnerAdapter(it, android.R.layout.simple_spinner_dropdown_item) }
        adapterWarehouse?.udpateView(mutableListOf(DataSpinner(getString(R.string.txt_no_data), "")))
        sp_price_group_warehouse?.adapter = adapterWarehouse
        vmWarehouse.getListWarehouses().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapterWarehouse?.udpateView(it.map { pg ->
                    return@map DataSpinner(pg.name, pg.id)
                }.toMutableList(), hasHeader = getString(R.string.txt_choose_warehouse))
                listWarehouse=it

            }
        })*/

        /*sp_price_group_warehouse.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (listWarehouse.isNotEmpty()){
                    idWarehouse = listWarehouse[position-1].id
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }*/

        btn_action_submit.setOnClickListener {
            actionAddPriceGroup()
        }

        btn_close.setOnClickListener {
            this.dismiss()
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

    private fun actionAddPriceGroup() {
        val numbersMap = validationAddPriceGroup()
        if (numbersMap["type"] as Boolean){
            context?.let { progressBar.show(it, "Silakan tunggu...") }
            val body: MutableMap<String, Any> = mutableMapOf(
                    "name" to (et_price_group_name?.text?.toString() ?: "")
//                "warehouse_id" to (idWarehouse?: "")
            )

            vmPriceGroup.postAddPriceGroup(body){
                progressBar.dialog.dismiss()
                Toast.makeText(context, "" + it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    listener()
                    this.dismiss()
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

    private fun validationAddPriceGroup(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (TextUtils.isEmpty(et_price_group_name.text)){
            message += "- Nama Price Group Tidak Boleh Kosong\n"
            cek = false
        }

        /*if (idWarehouse == null || idWarehouse == ""){
            message += "- Warehouse Tidak Boleh Kosong\n"
            cek = false
        }*/
        return mapOf("message" to message, "type" to cek)
    }
}