package id.sisi.postoko.view.ui.pricegroup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.activity_price_group_add.*

class AddPriceGroupActivity : BaseActivity() {
    private lateinit var vmPriceGroup: PriceGroupViewModel
    private lateinit var vmWarehouse: WarehouseViewModel

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
            }
        })
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity
        ) {
            val page = Intent(fragmentActivity, AddPriceGroupActivity::class.java)
            fragmentActivity.startActivity(page)
        }
    }
}