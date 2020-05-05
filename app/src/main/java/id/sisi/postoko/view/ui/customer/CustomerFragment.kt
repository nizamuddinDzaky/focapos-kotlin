package id.sisi.postoko.view.ui.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListMasterAdapter
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.RC_ADD_CUSTOMER
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.master_data_fragment.*

class CustomerFragment : BaseFragment() {

    companion object {
        fun newInstance() = CustomerFragment()
    }

    private lateinit var viewModel: CustomerViewModel
    private lateinit var adapter: ListMasterAdapter<Customer>
    var listCustomer: List<Customer>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.master_data_fragment, container, false)
    }

    override var tagName: String
        get() = "Pelanggan"
        set(_) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        viewModel.getListCustomers().observe(viewLifecycleOwner, Observer {
            listCustomer = it

            it?.let { it1 -> setupUI(it1) }

        })

        viewModel.getListCustomer()
        fb_add_master.setOnClickListener {
            startActivityForResult(Intent(this.context, AddCustomerActivity::class.java), RC_ADD_CUSTOMER)
        }

        sv_master.setOnClickListener {
            sv_master?.onActionViewExpanded()
        }
        sv_master.setOnQueryTextListener(object :
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
    }

    private fun startSearchData(query: String) {
        listCustomer?.let {
            val listSearchResult = listCustomer!!.filter {
                it.company?.contains(query, true)!! or it.address?.contains(query, true)!!
            }
            setupUI(listSearchResult)
        }
    }

    private fun setupUI(listCustomer: List<Customer>) {
        setupRecycleView(listCustomer)
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            viewModel.getListCustomer()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (::viewModel.isInitialized) {
                viewModel.getListCustomer()
            }
        }
    }

    private fun setupRecycleView(listCustomer: List<Customer>) {
        adapter = ListMasterAdapter()
        adapter.updateMasterData(listCustomer)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapter
    }
}
