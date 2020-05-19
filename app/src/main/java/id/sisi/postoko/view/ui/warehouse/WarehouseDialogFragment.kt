package id.sisi.postoko.view.ui.warehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListSearchDialogFragmentAdapter
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import kotlinx.android.synthetic.main.dialog_fragment_search.*
import kotlinx.android.synthetic.main.failed_load_data.*

class WarehouseDialogFragment(var header: Warehouse? = null): DialogFragment() {
    private lateinit var viewModel: WarehouseViewModel
    private lateinit var adapter: ListSearchDialogFragmentAdapter<Warehouse>
    private var listWarehouse: ArrayList<Warehouse> = arrayListOf()
    var listener: (Warehouse) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        viewModel.getListWarehouses().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                listWarehouse= arrayListOf()
                header.let {w ->
                    if (w != null) {
                        listWarehouse.add(w)
                    }
                }
                listWarehouse.addAll(it)
                setupUI(listWarehouse)
            }

            if (it?.size ?: 0 == 0) {
                layout_status_progress?.visible()
                rv_list_search_master?.gone()
                val status = when(it?.size) {
                    0 -> "Belum ada pembayaran"
                    else -> "Gagal Memuat Data"
                }
                tv_status_progress?.text = status
            } else {
                layout_status_progress?.gone()
                rv_list_search_master?.visible()
            }
        })

        sv_search_master?.onActionViewExpanded()
        sv_search_master.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    startSearchData(newText)
                } else {
                    setupUI(listWarehouse)
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun startSearchData(query: String) {
        listWarehouse.let {
            val listSearchResult = listWarehouse.filter {
                it.name.contains(query, true) or it.address.contains(query, true)
            }
            setupUI(listSearchResult)
        }
    }

    private fun dismissDialog() {
        this.dismiss()
    }

    private fun setupUI(it: List<Warehouse>) {
        setupRecycleView(it)
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            viewModel.getListWarehouse()
        }
    }
    private fun setupRecycleView(it: List<Warehouse>) {
        adapter = ListSearchDialogFragmentAdapter()
        adapter.updateMasterData(it)
        adapter.listenerWarehouse = {
            dismissDialog()
            listener(it)
        }
        rv_list_search_master?.layoutManager = LinearLayoutManager(this.context)
        rv_list_search_master?.setHasFixedSize(false)
        rv_list_search_master?.adapter = adapter
    }
}