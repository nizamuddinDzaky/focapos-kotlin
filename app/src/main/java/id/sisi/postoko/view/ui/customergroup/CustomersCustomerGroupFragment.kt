package id.sisi.postoko.view.ui.customergroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import id.sisi.postoko.R
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.DataCustomer
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.helper.fromJson
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.pricegroup.ListCustomerCGToCartAdapter
import id.sisi.postoko.view.ui.pricegroup.PriceGroupViewModel
import kotlinx.android.synthetic.main.master_data_fragment.*

class CustomersCustomerGroupFragment : BaseFragment(){
    private lateinit var mViewModel: PriceGroupViewModel
    private lateinit var adapter: ListCustomerCGToCartAdapter<Customer>
    override var tagName: String
        get() = "Pelanggan"
        set(_) {}
    companion object {
        fun newInstance() = CustomersCustomerGroupFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.master_data_fragment, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()

        val jsonHelper =
            Gson().fromJson<BaseResponse<DataCustomer>>(context!!, "DummyListCustomer.json")
        val customerGroup = (activity as AddCustomerToCustomerGoupActivity).customerGroup
        adapter.updateMasterData(jsonHelper.data?.list_customers)

//        mViewModel = ViewModelProvider(this).get(PriceGroupViewModel::class.java)
//        mViewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
//            swipeRefreshLayoutMaster?.isRefreshing = it
//        })
//        mViewModel.getListCustomers().observe(viewLifecycleOwner, Observer {
//            adapter.updateMasterData(it)
//        })
//
//        mViewModel.getListCustomer()
    }

    fun addToAdapter(value: Customer) {
        adapter.addData(value)
    }

    private fun setupUI() {
        fb_add_master?.gone()
        setupRecycleView()
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            //            viewModel.getListCustomer()
        }
    }

    private fun setupRecycleView() {
        adapter = ListCustomerCGToCartAdapter(fragmentActivity = activity)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.addVerticalDivider()
        rv_list_master_data?.adapter = adapter
    }
}