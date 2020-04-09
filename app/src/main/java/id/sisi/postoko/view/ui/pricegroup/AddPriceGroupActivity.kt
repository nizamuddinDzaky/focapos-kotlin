package id.sisi.postoko.view.ui.pricegroup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.activity_price_group_add.*
import java.util.ArrayList

class AddPriceGroupActivity : BaseActivity() {
    private lateinit var vmPriceGroup: PriceGroupViewModel
    private lateinit var vmWarehouse: WarehouseViewModel
    private var listWarehouse: List<Warehouse> = ArrayList()
    private var idWarehouse: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_price_group_add)
        displayHomeEnable()

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
                if (listWarehouse.size > 0){
                    idWarehouse = listWarehouse[position].id
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        btn_action_submit.setOnClickListener {
            actionAddPriceGroup()
        }
    }

    private fun actionAddPriceGroup() {
        val body: MutableMap<String, Any> = mutableMapOf(
            "name" to (et_price_group_name?.text?.toString() ?: ""),
            "warehouse_id" to (idWarehouse?: "")
        )

        vmPriceGroup.postAddPriceGroup(body){
            Toast.makeText(this, "" + it["message"], Toast.LENGTH_SHORT).show()
            if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                val returnIntent = Intent()
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
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