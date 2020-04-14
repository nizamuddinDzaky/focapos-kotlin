package id.sisi.postoko.view.ui.gr

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListGoodReceivedAdapter
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus.DELIVERING
import kotlinx.android.synthetic.main.fragment_gr.*


class GoodReceivedFragment(var status: GoodReceiveStatus = DELIVERING) : BaseFragment() {

    private lateinit var viewModel: GoodReceivedViewModel
    private lateinit var adapter: ListGoodReceivedAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading = false
    private var isLastPage = false
    private var mPageSize = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gr, container, false)
    }

    override var tagName: String
        get() = status.name
        set(_) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, GoodReceivedFactory(status.name)).get(GoodReceivedViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout?.isRefreshing = it
        })
        viewModel.getListGoodsReceived().observe(viewLifecycleOwner, Observer {
            adapter.updateGoodsReceivedData(it)
            logE("progress $it")
            if (it?.size ?: 0 == 0) {
                layout_status_progress?.visible()
                rv_list_good_received?.gone()
                val status = when(it?.size) {
                    0 -> "Belum ada transaksi"
                    else -> "Gagal Memuat Data"
                }
                tv_status_progress?.text = status
            } else {
                layout_status_progress?.gone()
                rv_list_good_received?.visible()
            }
        })
    }

    private fun showBottomSheetDialogFragment(goodReceived: GoodReceived?) {
        val bottomSheetFragment = BottomSheetAddGoodReceivedFragment()
        val bundle = Bundle()
        bundle.putParcelable("good_received", goodReceived)
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.listener = {
            viewModel.getListGoodReceived()
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun setupUI() {
        setupRecycleView()

        swipeRefreshLayout?.setOnRefreshListener {
            viewModel.getListGoodReceived()
        }
    }

    private fun setupRecycleView() {
        adapter = ListGoodReceivedAdapter(status = status) {
//            showBottomSheetDialog()
            showBottomSheetDialogFragment(it)
        }
        layoutManager = LinearLayoutManager(this.context)
        rv_list_good_received?.layoutManager = layoutManager
        rv_list_good_received?.setHasFixedSize(false)
        rv_list_good_received?.adapter = adapter

        //val listenerRecyclerView =
        rv_list_good_received?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = layoutManager.getChildCount()
                val totalItemCount: Int = layoutManager.getItemCount()
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= mPageSize) {

                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.menu_action_search -> {
                logE("tes")
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeListeners()
    }

    private fun removeListeners() {
        rv_list_good_received?.clearOnScrollListeners()
    }

    companion object {
        fun newInstance() =
            GoodReceivedFragment()
    }
}