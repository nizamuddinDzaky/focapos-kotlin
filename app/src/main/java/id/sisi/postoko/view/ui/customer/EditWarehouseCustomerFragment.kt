package id.sisi.postoko.view.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListWareHouseOfAddCustomerAdapter
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit_warehouse_customer.*

class EditWarehouseCustomerFragment : BaseFragment(), ListWareHouseOfAddCustomerAdapter.OnClickListenerInterface {

    companion object {
        fun newInstance() = EditWarehouseCustomerFragment()
    }

    private lateinit var adapterAdd: ListWareHouseOfAddCustomerAdapter

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
        return inflater.inflate(R.layout.fragment_edit_warehouse_customer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as EditCustomerActivity).viewModelCustomer.getListWarehouse().observe(viewLifecycleOwner, Observer {
            it?.let {listWarehouse ->
                logE("warehouse : $listWarehouse")
                (activity as EditCustomerActivity).listWarehouse = listWarehouse
                setupRecycleView(listWarehouse)

            }
        })

        (activity as EditCustomerActivity).viewModelCustomer.requestSelectedWarehouse((activity as EditCustomerActivity).customer?.id?.toInt() ?: 0)
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
        val index = (activity as EditCustomerActivity).listWarehouse?.indexOf(warehouse) ?: 0
        if((activity as EditCustomerActivity).listWarehouse?.get(index)?.isDefault == true && !isSelected){
            Toast.makeText(context, "Terpilih sebagai gudang default", Toast.LENGTH_SHORT).show()
        }else{
            (activity as EditCustomerActivity).listWarehouse?.get(index)?.isSelected = isSelected
        }
        adapterAdd.notifyItemChanged(index)
    }

    override fun onClickDefault(warehouse: Warehouse) {
        val index = (activity as EditCustomerActivity).listWarehouse?.indexOf(warehouse) ?: 0
        (activity as EditCustomerActivity).listWarehouse?.forEach {wh ->
            wh.isDefault = false
        }
        (activity as EditCustomerActivity).listWarehouse?.get(index)?.isDefault = true
        (activity as EditCustomerActivity).listWarehouse?.get(index)?.isSelected = true
        adapterAdd.notifyDataSetChanged()
    }
}
