package id.sisi.postoko.view.ui.purchase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.utils.KEY_PURCHASE_STATUS
import androidx.lifecycle.Observer
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.sales.NetworkState
import id.sisi.postoko.view.sales.SBAdapter
import id.sisi.postoko.view.sales.SBFactory
import id.sisi.postoko.view.sales.SBViewModel
import id.sisi.postoko.view.ui.sales.SaleStatus
import kotlinx.android.synthetic.main.failed_load_data.*
import kotlinx.android.synthetic.main.fragment_sales_booking.*

class PurchaseFragment(var status: PurchaseStatus = PurchaseStatus.PENDING) : BaseFragment() {
    override var tagName: String
        get() = status.name
        set(_) {}

    private val progressBar = CustomProgressBar()
    private lateinit var viewModel: PurchasesViewModel
    private lateinit var adapter: PurchaseAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        return inflater.inflate(R.layout.fragment_sales_booking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(this, PurchaseFactory(hashMapOf(KEY_PURCHASE_STATUS to status.name))).get(
                PurchasesViewModel::class.java
            )
    }

    private fun setupUI() {
        setupRecycleView()

        swipeRefreshLayoutSalesBooking?.setOnRefreshListener {
            viewModel.requestRefreshNoFilter()
        }
    }

    private fun setupRecycleView() {
        adapter = PurchaseAdapter()
        /*adapter.closeSaleListener={idSalesBooking ->
            vmSale.postCloseSale(idSalesBooking) {
                viewModel.requestRefreshNoFilter()
            }
        }*/
        layoutManager = LinearLayoutManager(this.context)
        rv_list_sales?.layoutManager = layoutManager
        rv_list_sales?.setHasFixedSize(false)
        rv_list_sales?.adapter = adapter

        viewModel.repos.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            actionCheckEmpty(it.size)
            /*scrollToTop()*/
            swipeRefreshLayoutSalesBooking?.isRefreshing = false
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            logE("networkState $it")
            if (it == NetworkState.FAILED && adapter.itemCount == 0) {
                actionCheckEmpty(null)
            }
            swipeRefreshLayoutSalesBooking?.isRefreshing = it == NetworkState.RUNNING
        })
    }

    private fun actionCheckEmpty(size: Int?) {
        val status = when (size) {
            0 -> "Belum ada transaksi"
            else -> "Gagal Memuat Data"
        }
        if (size ?: 0 == 0) {
            layout_status_progress?.visible()
            rv_list_sales?.gone()
            tv_status_progress?.text = status
        } else {
            layout_status_progress?.gone()
            rv_list_sales?.visible()
        }
        swipeRefreshLayoutSalesBooking?.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*logE("adasdasdasdasdsadsadsasadasasdads $requestCode, $resultCode")*/
        if (resultCode == Activity.RESULT_OK) {
            if (::viewModel.isInitialized) {
                viewModel.requestRefreshNoFilter()
            }
        }
    }
}