package id.sisi.postoko.view.ui.pricegroup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import id.sisi.postoko.R
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.DataCustomer
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.helper.fromJson
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.master_data_fragment.*

class CustomerPriceGroupFragment : BaseFragment() {
    companion object {
        fun newInstance() = CustomerPriceGroupFragment()
    }

    //    private lateinit var viewModel: PriceGroupViewModel
    private lateinit var adapter: ListCustToCartAdapter<Customer>

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

        setupUI()

        val jsonHelper =
            Gson().fromJson<BaseResponse<DataCustomer>>(context!!, "DummyListCustomer.json")
        adapter.updateMasterData(jsonHelper.data?.list_customers)

//        viewModel = ViewModelProvider(this).get(PriceGroupViewModel::class.java)
//        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
//            swipeRefreshLayoutMaster?.isRefreshing = it
//        })
//        viewModel.getListCustomers().observe(viewLifecycleOwner, Observer {
//            adapter.updateMasterData(it)
//        })

//        viewModel.getListCustomer()
    }

    private fun setupUI() {
        fb_add_master?.gone()
        setupRecycleView()
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            //            viewModel.getListCustomer()
        }
    }

    fun addToAdapter(value: Customer) {
        adapter.addData(value)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
//            if (::viewModel.isInitialized) {
//                viewModel.getListCustomer()
//            }
        }
    }

    private fun setupRecycleView() {
        adapter = ListCustToCartAdapter(fragmentActivity = activity)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapter
    }
}
