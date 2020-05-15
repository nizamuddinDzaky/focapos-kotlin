package id.sisi.postoko.view.ui.sales

import android.app.AlertDialog
import android.app.Dialog
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
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.ui.customer.CustomerViewModel
import kotlinx.android.synthetic.main.dialog_fragment_search.*
import kotlinx.android.synthetic.main.failed_load_data.*

class FragmentSearchCustomer : DialogFragment() {
    private lateinit var viewModel: CustomerViewModel
    private lateinit var adapter: ListSearchDialogFragmentAdapter<Customer>
    var listCustomer: List<Customer>? = arrayListOf()
    var listener: (Customer) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)

        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })

        viewModel.getListCustomers().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                listCustomer = it
                setupUI(it)
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
        viewModel.getListCustomer()
        sv_search_master?.onActionViewExpanded()
        sv_search_master.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    startSearchData(newText)
                } else {
                    listCustomer?.let { setupUI(it) }
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun dismissDialog() {
        this.dismiss()
    }

    private fun setupUI(it: List<Customer>) {
        setupRecycleView(it)
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            viewModel.getListCustomer()
        }
    }

    private fun startSearchData(query: String) {
        listCustomer?.let {
            val listSearchResult = listCustomer!!.filter {
                it.company?.contains(query, true)!! or it.address?.contains(query, true)!!
            }
            setupUI(listSearchResult)
        }
    }

    private fun setupRecycleView(it: List<Customer>) {
        adapter = ListSearchDialogFragmentAdapter()
        adapter.updateMasterData(it)
        adapter.listener = {
            listener(it)
            dismissDialog()
        }
        rv_list_search_master?.layoutManager = LinearLayoutManager(this.context)
        rv_list_search_master?.setHasFixedSize(false)
        rv_list_search_master?.adapter = adapter
    }
}