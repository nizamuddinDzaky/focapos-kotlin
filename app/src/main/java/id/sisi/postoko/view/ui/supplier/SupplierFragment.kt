package id.sisi.postoko.view.ui.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListMasterAdapter
import id.sisi.postoko.model.Supplier
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.master_data_fragment.*

class SupplierFragment : BaseFragment() {

    companion object {
        fun newInstance() = SupplierFragment()
    }

    private lateinit var viewModel: SupplierViewModel
    private lateinit var adapter: ListMasterAdapter<Supplier>
    private var listSupplier: List<Supplier>? = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.master_data_fragment, container, false)
    }

    override var tagName: String
        get() = "Pemasok"
        set(_) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        viewModel.getListSuppliers().observe(viewLifecycleOwner, Observer {
            listSupplier = it
            listSupplier?.let { it1 -> setupUI(it1) }
            /**/
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
                    listSupplier?.let { setupUI(it) }
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    private fun startSearchData(query: String) {
        listSupplier?.let {
            val listSearchResult = listSupplier!!.filter {
                it.name.contains(query, true) or it.address.contains(query, true)
            }
            setupUI(listSearchResult)
        }
    }

    private fun setupUI(listSupplier: List<Supplier>) {
        setupRecycleView(listSupplier)
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            viewModel.getListSupplier()
        }
    }

    private fun setupRecycleView(listSupplier: List<Supplier>) {
        adapter = ListMasterAdapter()
        adapter.updateMasterData(listSupplier)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapter
    }
}
