package id.sisi.postoko.view.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListWareHouseOfAddCustomerAdapter
import id.sisi.postoko.model.Warehouse
import androidx.lifecycle.Observer
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_warehouse_customer.*

class AddWarehouseCustomerFragment : BaseFragment(), ListWareHouseOfAddCustomerAdapter.OnClickListenerInterface {
    private lateinit var adapterAdd: ListWareHouseOfAddCustomerAdapter

    companion object {
        fun newInstance() = AddWarehouseCustomerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override var tagName: String
        get() = "Gudang"
        set(_) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_warehouse_customer, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AddCustomerActivity).mViewModelWarehouse.getListWarehouses().observe(viewLifecycleOwner, Observer {
            it?.let {
                /*(activity as AddCustomerActivity).listWarehouse = it*/
                it.forEach {warehouse ->
                    warehouse.isSelected = true
                }
                (activity as AddCustomerActivity).listWarehouse = it
                (activity as AddCustomerActivity).listWarehouse?.let { it1 -> setupRecycleView(it1) }
                val lastIndex = (activity as AddCustomerActivity).listWarehouse?.size?.minus(1) ?: 0
                (activity as AddCustomerActivity).listWarehouse?.get(lastIndex)?.isDefault = true
            }
        })

    }

    private fun setupRecycleView(listWarehouse: List<Warehouse>) {
        adapterAdd = ListWareHouseOfAddCustomerAdapter()
        adapterAdd.listenerItem = this
        adapterAdd.updateData(listWarehouse)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapterAdd
    }

    override fun onClickSelected(warehouse: Warehouse, isSelected: Boolean) {
        val index = (activity as AddCustomerActivity).listWarehouse?.indexOf(warehouse) ?: 0
        (activity as AddCustomerActivity).listWarehouse?.get(index)?.isSelected =isSelected
    }

    override fun onClickDefault(warehouse: Warehouse) {
        val index = (activity as AddCustomerActivity).listWarehouse?.indexOf(warehouse) ?: 0
        (activity as AddCustomerActivity).listWarehouse?.forEach {wh ->
            wh.isDefault = false
        }
        (activity as AddCustomerActivity).listWarehouse?.get(index)?.isDefault = true
        adapterAdd.notifyDataSetChanged()
    }
}
