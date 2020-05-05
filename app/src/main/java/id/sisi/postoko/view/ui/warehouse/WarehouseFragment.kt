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
    private var listWarehouse: List<Warehouse>? = arrayListOf()

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



        viewModel = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        viewModel.getListWarehouses().observe(viewLifecycleOwner, Observer {
            /**/
            listWarehouse = it
            listWarehouse?.let { it1 -> setupUI(it1) }
        })

        sv_master.setOnClickListener {
            sv_master?.onActionViewExpanded()
        }
        sv_master.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    startSearchData(newText)
                } else {
                    listWarehouse?.let { setupUI(it) }
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    private fun startSearchData(query: String) {
        listWarehouse?.let {
            val listSearchResult = listWarehouse!!.filter {
                it.name.contains(query, true) or it.address.contains(query, true)
            }
            setupUI(listSearchResult)
        }
    }

    private fun setupUI(listWarehouse: List<Warehouse>) {
        setupRecycleView(listWarehouse)
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            viewModel.getListWarehouse()
        }
    }

    private fun setupRecycleView(listWarehouse: List<Warehouse>) {
        adapter = ListMasterAdapter()
        adapter.updateMasterData(listWarehouse)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapter
    }
}
