package id.sisi.postoko.view.sales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.utils.KEY_IS_SEARCH
import id.sisi.postoko.utils.KEY_SALE_STATUS
import id.sisi.postoko.utils.KEY_SEARCH
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toLower
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.sales.AddSalesViewModel
import id.sisi.postoko.view.ui.sales.SaleStatus
import kotlinx.android.synthetic.main.failed_load_data.*
import kotlinx.android.synthetic.main.fragment_sales_booking.*


class SBFragment(var status: SaleStatus = SaleStatus.PENDING) : BaseFragment() {

    private lateinit var viewModel: SBViewModel
    private lateinit var vmSale: AddSalesViewModel
    private lateinit var adapter: SBAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        return inflater.inflate(R.layout.fragment_sales_booking, container, false)
    }

    private val progressBar = CustomProgressBar()

    override var tagName: String
        get() = status.name
        set(_) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()
    }

    private fun setupViewModel() {

        vmSale = ViewModelProvider(
            this
        ).get(AddSalesViewModel::class.java)

        vmSale.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it) {
                context?.let { it1 -> progressBar.show(it1, getString(R.string.txt_please_wait)) }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        vmSale.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel =
            ViewModelProvider(this, SBFactory(hashMapOf(KEY_SALE_STATUS to status.name))).get(
                SBViewModel::class.java
            )
    }

    private fun setupUI() {
        setupRecycleView()

        swipeRefreshLayoutSalesBooking?.setOnRefreshListener {
            logE("bismillah")
            viewModel.requestRefreshNoFilter()
        }
    }

    fun submitQuerySearch(query: String) {
        adapter.submitList(null)

        val filter = viewModel.getFilter().value ?: hashMapOf()
        filter[KEY_SEARCH] = query
        viewModel.requestRefreshNewFilter(filter)
    }

    private fun setupRecycleView() {
        adapter = SBAdapter()
        adapter.closeSaleListener={idSalesBooking ->
            /*vmSale.postCloseSale(idSalesBooking) {
                viewModel.requestRefreshNoFilter()
            }*/
        }
        layoutManager = LinearLayoutManager(this.context)
        rv_list_sales?.layoutManager = layoutManager
        rv_list_sales?.setHasFixedSize(false)
        rv_list_sales?.adapter = adapter

        viewModel.repos.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            actionCheckEmpty(it.size)
            scrollToTop()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(status != SaleStatus.ALL)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.menu_action_filter -> {
                showBottomSheetFilter()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    fun showBottomSheetFilter(isSearch: Boolean = false) {
        val filter = viewModel.getFilter().value ?: hashMapOf()
        if (isSearch) {
            filter[KEY_IS_SEARCH] = ""
        } else {
            filter.remove(KEY_IS_SEARCH)
        }
        BottomSheetFilterFragment.show(childFragmentManager, filter) {
            if (status != SaleStatus.ALL) {
                it[KEY_SALE_STATUS] = status.name.toLower()
            }
            viewModel.requestRefreshNewFilter(it)
        }
    }

    fun scrollToTop() {
        rv_list_sales.postDelayed({
            rv_list_sales.layoutManager?.scrollToPosition(0)
        }, 400)
    }

    companion object {
        fun newInstance() =
            SBFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (::viewModel.isInitialized) {
                viewModel.requestRefreshNoFilter()
            }
        }
    }
}