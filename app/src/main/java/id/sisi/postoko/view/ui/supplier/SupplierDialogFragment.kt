package id.sisi.postoko.view.ui.supplier

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
import id.sisi.postoko.model.Supplier
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import kotlinx.android.synthetic.main.dialog_fragment_search.*
import kotlinx.android.synthetic.main.failed_load_data.*

class SupplierDialogFragment: DialogFragment() {

    private lateinit var viewModel: SupplierViewModel
    private lateinit var adapter: ListSearchDialogFragmentAdapter<Supplier>
    private var listSupplier: ArrayList<Supplier> = arrayListOf()
    var listener: (Supplier) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })

        viewModel.getListSuppliers().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                listSupplier= arrayListOf()
                /*header.let {w ->
                    if (w != null) {
                        listWarehouse.add(w)
                    }
                }*/
                listSupplier.addAll(it)
                setupUI(listSupplier)
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
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupUI(it: List<Supplier>) {
        setupRecycleView(it)
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            viewModel.getListSuppliers()
        }
    }
    private fun setupRecycleView(it: List<Supplier>) {
        adapter = ListSearchDialogFragmentAdapter()
        adapter.updateMasterData(it)
        adapter.listenerSupplier = {
            this.dismiss()
            listener(it)
        }
        rv_list_search_master?.layoutManager = LinearLayoutManager(this.context)
        rv_list_search_master?.setHasFixedSize(false)
        rv_list_search_master?.adapter = adapter
    }
}