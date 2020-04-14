package id.sisi.postoko.view.ui.pricegroup

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.model.Sales
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.RC_ADD_PRICE_GROUP
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.setIfExist
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel

import kotlinx.android.synthetic.main.activity_edit_price_group.*
import kotlinx.android.synthetic.main.content_edit_price_group.*
import kotlinx.android.synthetic.main.content_edit_price_group.btn_action_submit
import kotlinx.android.synthetic.main.content_edit_price_group.et_price_group_name
import kotlinx.android.synthetic.main.content_edit_price_group.sp_price_group_warehouse
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_price_group.*
import java.util.ArrayList

class EditPriceGroupActivity : AppCompatActivity() {
    private var priceGroup: PriceGroup? = null
    private lateinit var vmWarehouse: WarehouseViewModel
    private var idWarehouse: String? = null
    private var listWarehouse: List<Warehouse> = ArrayList()
    private lateinit var vmPriceGroup: PriceGroupViewModel
    private val progressBar = CustomProgressBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_price_group)
        setSupportActionBar(toolbar)

        val bundle = intent.extras
        priceGroup = bundle?.getParcelable<PriceGroup>(KEY_PRICE_GROUP)
        supportActionBar?.title = null

        toolbar_subtitle.text = "( ${priceGroup?.name} )"
        vmPriceGroup = ViewModelProvider(this).get(PriceGroupViewModel::class.java)

        idWarehouse = priceGroup?.warehouse_id?.toString()

        val adapterWarehouse =
            MySpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        adapterWarehouse.udpateView(mutableListOf(DataSpinner(getString(R.string.txt_no_data), "")))
        sp_price_group_warehouse?.adapter = adapterWarehouse
        vmWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        vmWarehouse.getListWarehouses().observe(this, Observer {
            it?.let {
                adapterWarehouse.udpateView(it.map { pg ->
                    return@map DataSpinner(pg.name, pg.id)
                }.toMutableList(), hasHeader = getString(R.string.txt_choose_warehouse))
                listWarehouse = it
                sp_price_group_warehouse?.setIfExist(idWarehouse)
            }
        })

        et_price_group_name.setText(priceGroup?.name)

        btn_action_submit.setOnClickListener {
            actionEditPriceGroup()
        }
    }

    private fun actionEditPriceGroup() {
        val numbersMap = validationEditPriceGroup()
        if (numbersMap["type"] as Boolean){
            progressBar.show(this, "Silakan tunggu...")
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_price_group_name?.text?.toString() ?: ""),
                "warehouse_id" to (idWarehouse?: "")
            )

            vmPriceGroup.putEditPriceGroup(body,priceGroup?.id.toString()){
                Toast.makeText(this, "" + it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    progressBar.dialog.dismiss()
                    val returnIntent = Intent()
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }else{
            AlertDialog.Builder(this)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity,
            priceGroup: PriceGroup
        ) {
            val page = Intent(fragmentActivity, EditPriceGroupActivity::class.java)
            page.putExtra(KEY_PRICE_GROUP, priceGroup)
            fragmentActivity.startActivityForResult(page, RC_ADD_PRICE_GROUP)
        }
    }

}
