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
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.sales.SaleStatus.PENDING
import kotlinx.android.synthetic.main.failed_load_data.*
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
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutSalesBooking?.isRefreshing = it
        })
        viewModel.getListSales().observe(viewLifecycleOwner, Observer {
            adapter.updateSalesData(it)
            if (it?.size ?: 0 == 0) {
                layout_status_progress?.visible()
                rv_list_sales?.gone()
                val status = when(it?.size) {
                    0 -> "Belum ada pembayaran"
                    else -> "Gagal Memuat Data"
                }
                tv_status_progress?.text = status
            } else {
                layout_status_progress?.gone()
                rv_list_sales?.visible()
            }
        })
    }

    private fun setupUI() {
        setupRecycleView()
        swipeRefreshLayoutSalesBooking?.setOnRefreshListener {
            viewModel.getListSale()
        }
    }

    private fun setupRecycleView() {
        adapter = ListSalesAdapter(fragmentActivity = activity)
        rv_list_sales?.layoutManager = LinearLayoutManager(this.context)
        rv_list_sales?.setHasFixedSize(false)
        rv_list_sales?.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (::viewModel.isInitialized) {
                viewModel.getListSale()
            }
        }
    }
}
