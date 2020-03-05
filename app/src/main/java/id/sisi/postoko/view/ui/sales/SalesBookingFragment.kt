package id.sisi.postoko.view.ui.sales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListSalesAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.sales.SaleStatus.PENDING
import kotlinx.android.synthetic.main.fragment_sales_booking.*

class SalesBookingFragment(var status: SaleStatus = PENDING) : BaseFragment() {

    companion object {
        fun newInstance() = SalesBookingFragment()
    }

    private lateinit var viewModel: SalesBookingViewModel
    private lateinit var adapter: ListSalesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sales_booking, container, false)
    }

    override var tagName: String
        get() = status.name
        set(_) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()

        viewModel = ViewModelProvider(this, SalesBookingFactory(status.name)).get(SalesBookingViewModel::class.java)
        viewModel.getListSales().observe(viewLifecycleOwner, Observer {
            adapter.updateSalesData(it)
        })
    }

    private fun setupUI() {
        setupRecycleView()
    }

    private fun setupRecycleView() {
        adapter = ListSalesAdapter()
        rv_list_sales?.layoutManager = LinearLayoutManager(this.context)
        rv_list_sales?.setHasFixedSize(false)
        rv_list_sales?.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        logE("tarik data dari view model $resultCode")
        if (resultCode == Activity.RESULT_OK) {
            if (::viewModel.isInitialized) {
                viewModel.getListSale()
            }
        }
    }
}
