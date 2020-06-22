package id.sisi.postoko.view.ui.customer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListWareHouseOfCustomerAdapter
import id.sisi.postoko.model.Warehouse
import androidx.lifecycle.Observer
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_warehouse_customer.*

class AddWarehouseCustomerFragment : BaseFragment() {
    private lateinit var adapter: ListWareHouseOfCustomerAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_warehouse.setOnClickListener {
            val intent = Intent(context, AddCustomerWarehouseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AddCustomerActivity).mViewModelWarehouse.getListWarehouses().observe(viewLifecycleOwner, Observer {
            it?.let { it1 -> setupRecycleView(it1) }
        })

    }

    private fun setupRecycleView(listWarehouse: List<Warehouse>) {
        adapter = ListWareHouseOfCustomerAdapter()
        adapter.updateData(listWarehouse)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapter
    }
}
