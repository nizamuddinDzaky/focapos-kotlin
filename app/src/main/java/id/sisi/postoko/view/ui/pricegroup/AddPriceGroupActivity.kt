package id.sisi.postoko.view.ui.pricegroup

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.RC_ADD_PRICE_GROUP
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.activity_price_group_add.*
import kotlinx.android.synthetic.main.content_add_price_group.*
import java.util.*

class AddPriceGroupActivity : BaseActivity() {
    private lateinit var vmPriceGroup: PriceGroupViewModel
    private lateinit var vmWarehouse: WarehouseViewModel
    private var listWarehouse: List<Warehouse> = ArrayList()
    private var idWarehouse: String? = null
    private val progressBar = CustomProgressBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_price_group_add)
        vmWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        vmPriceGroup = ViewModelProvider(this).get(PriceGroupViewModel::class.java)

        val adapterWarehouse =
            MySpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        adapterWarehouse.udpateView(mutableListOf(DataSpinner(getString(R.string.txt_no_data), "")))
        sp_price_group_warehouse?.adapter = adapterWarehouse
        vmWarehouse.getListWarehouses().observe(this, Observer {
            it?.let {
                adapterWarehouse.udpateView(it.map { pg ->
                    return@map DataSpinner(pg.name, pg.id)
                }.toMutableList(), hasHeader = getString(R.string.txt_choose_warehouse))
                listWarehouse=it

            }
        })

        sp_price_group_warehouse.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
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
        }

        btn_action_submit.setOnClickListener {
            actionAddPriceGroup()
        }
    }

    private fun actionAddPriceGroup() {
        val numbersMap = validationAddPriceGroup()
        if (numbersMap["type"] as Boolean){
            progressBar.show(this, "Silakan tunggu...")
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_price_group_name?.text?.toString() ?: ""),
                "warehouse_id" to (idWarehouse?: "")
            )

            vmPriceGroup.postAddPriceGroup(body){
                progressBar.dialog.dismiss()
                Toast.makeText(this, "" + it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
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

    private fun validationAddPriceGroup(): Map<String, Any?> {
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

        super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity
        ) {
            val page = Intent(fragmentActivity, AddPriceGroupActivity::class.java)
            fragmentActivity.startActivityForResult(page, RC_ADD_PRICE_GROUP)
        }
    }
}