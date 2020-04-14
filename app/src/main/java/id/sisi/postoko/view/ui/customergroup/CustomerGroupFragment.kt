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
import id.sisi.postoko.view.ui.pricegroup.PriceGroupViewModel
import kotlinx.android.synthetic.main.master_data_fragment.*

class CustomerGroupFragment : BaseFragment() {
    companion object {
        fun newInstance() = CustomerGroupFragment()
    }

    private lateinit var mViewModel: CustomerGroupViewModel
    private lateinit var mAdapter: ListMasterAdapter<CustomerGroup>

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

        setupUI()

        mViewModel = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)
        mViewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        mViewModel.getListCustomerGroups().observe(viewLifecycleOwner, Observer {
            mAdapter.updateMasterData(it)
        })

        mViewModel.getListCustomerGroup()
        fb_add_master.setOnClickListener {
            AddCustomerGroupActivity.show(activity as FragmentActivity)
        }
    }

    private fun setupUI() {
        setupRecycleView()
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

    private fun setupRecycleView() {
        mAdapter = ListMasterAdapter(fragmentActivity = activity)
        mAdapter.listenerCustomerGroup={
            showBottomEditSheetCustomerGroup(it)
        }
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = mAdapter
    }

    private fun showBottomEditSheetCustomerGroup(it: CustomerGroup) {
        BottomSheetEditCustomerGroupFragment.show(
            childFragmentManager,
            it
        )
        BottomSheetEditCustomerGroupFragment.listener={
            mViewModel.getListCustomerGroup()
        }
    }
}