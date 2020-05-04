package id.sisi.postoko.view.ui.customergroup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListMasterAdapter
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.master_data_fragment.*

class CustomerGroupFragment : BaseFragment() {
    companion object {
        fun newInstance() = CustomerGroupFragment()
    }

    private lateinit var mViewModel: CustomerGroupViewModel
    private lateinit var mAdapter: ListMasterAdapter<CustomerGroup>
    private var listCustomerGroup: List<CustomerGroup>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.master_data_fragment, container, false)
    }

    override var tagName: String
        get() = "Kelompok Pelanggan"
        set(_) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        mViewModel = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)
        mViewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        mViewModel.getListCustomerGroups().observe(viewLifecycleOwner, Observer {
            listCustomerGroup = it
            listCustomerGroup?.let { it1 -> setupUI(it1) }
        })

        mViewModel.getListCustomerGroup()
        fb_add_master.setOnClickListener {
            AddCustomerGroupActivity.show(activity as FragmentActivity)
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
                    listCustomerGroup?.let { setupUI(it) }
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    private fun startSearchData(query: String) {
        listCustomerGroup?.let {
            val listSearchResult = listCustomerGroup!!.filter {
                it.name.contains(query, true)
            }
            setupUI(listSearchResult)
        }
    }

    private fun setupUI(listCustomerGroup: List<CustomerGroup>) {
        setupRecycleView(listCustomerGroup)
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            mViewModel.getListCustomerGroup()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (::mViewModel.isInitialized) {
                mViewModel.getListCustomerGroup()
            }
        }
    }

    private fun setupRecycleView(listCustomerGroup: List<CustomerGroup>) {
        mAdapter = ListMasterAdapter(fragmentActivity = activity)
        mAdapter.updateMasterData(listCustomerGroup)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = mAdapter
    }

}