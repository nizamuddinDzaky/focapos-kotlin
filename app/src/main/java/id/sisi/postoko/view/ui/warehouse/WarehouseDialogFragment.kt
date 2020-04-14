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
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.dashboard.DashboardPiechartFragment
import kotlinx.android.synthetic.main.dialog_fragment_search.*

class WarehouseDialogFragment: DialogFragment() {
    private lateinit var viewModel: WarehouseViewModel
    private lateinit var adapter: ListSearchDialogFragmentAdapter<Warehouse>
    private var listWarehouse: ArrayList<Warehouse>? = arrayListOf()
    var listener: (Warehouse) -> Unit = {}
    private var warehouse: Warehouse = Warehouse(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "Semua Gudang",
        "",
        "",
        "",
        "",
        "")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        viewModel.getListWarehouses().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                listWarehouse= arrayListOf()
                listWarehouse!!.add(warehouse)
//                listWarehouse = it
                listWarehouse!!.addAll(it)
                setupUI(listWarehouse?: arrayListOf())
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun dismissDialog() {
        this.dismiss()
    }

    private fun setupUI(it: List<Warehouse>) {
        setupRecycleView(it)
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