package id.sisi.postoko.view.ui.warehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListMasterAdapter
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.master_data_fragment.*

class WarehouseFragment : BaseFragment() {

    companion object {
        fun newInstance() = WarehouseFragment()
    }

    private lateinit var viewModel: WarehouseViewModel
    private lateinit var adapter: ListMasterAdapter<Warehouse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.master_data_fragment, container, false)
    }

    override var tagName: String
        get() = "Gudang"
        set(_) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()

        viewModel = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        viewModel.getListWarehouses().observe(viewLifecycleOwner, Observer {
            adapter.updateMasterData(it)
        })
    }

    private fun setupUI() {
        setupRecycleView()
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            viewModel.getListWarehouse()
        }
    }

    private fun setupRecycleView() {
        adapter = ListMasterAdapter()
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapter
    }
}
