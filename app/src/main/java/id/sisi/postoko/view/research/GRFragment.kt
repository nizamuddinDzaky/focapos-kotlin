package id.sisi.postoko.view.research

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.gr.BottomSheetAddGoodReceivedFragment
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus.DELIVERING
import kotlinx.android.synthetic.main.fragment_gr.*


class GRFragment(var status: GoodReceiveStatus = DELIVERING) : BaseFragment() {

    private lateinit var viewModel: GRViewModel
    private lateinit var adapter: GRAdapter
    private lateinit var layoutManager: LinearLayoutManager

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

        setupViewModel()
        setupUI()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, GRFactory(status.name)).get(GRViewModel::class.java)
    }

    private fun setupUI() {
        setupRecycleView()

        swipeRefreshLayout?.setOnRefreshListener {
            viewModel.requestRefreshListGR()
        }
    }

    private fun setupRecycleView() {
        adapter = GRAdapter {
            showBottomSheetDialogFragment(it)
        }
        layoutManager = LinearLayoutManager(this.context)
        rv_list_good_received?.layoutManager = layoutManager
        rv_list_good_received?.setHasFixedSize(false)
        rv_list_good_received?.adapter = adapter

        viewModel.repos.observe(viewLifecycleOwner, Observer {
            logE("isi data size ${it.size}")
            adapter.submitList(it)
            actionCheckEmpty(it.size)
        })
        viewModel.networkState?.observe(viewLifecycleOwner, Observer {
            logE("isi data state ${it}")
            if (it == NetworkState.FAILED && adapter.itemCount == 0) {
                actionCheckEmpty(null)
            }
            swipeRefreshLayout?.isRefreshing = it == NetworkState.RUNNING
        })
    }

    private fun actionCheckEmpty(size: Int?) {
        val status = when(size) {
            0 -> "Belum ada transaksi"
            else -> "Gagal Memuat Data"
        }
        if (size ?: 0 == 0) {
            layout_status_progress?.visible()
            rv_list_good_received?.gone()
            tv_status_progress?.text = status
        } else {
            layout_status_progress?.gone()
            rv_list_good_received?.visible()
        }
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun showBottomSheetDialogFragment(goodReceived: GoodReceived?) {
        val bottomSheetFragment = BottomSheetAddGoodReceivedFragment()
        val bundle = Bundle()
        bundle.putParcelable("good_received", goodReceived)
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.listener = {
            viewModel.requestRefreshListGR()
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_good, menu)
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

    companion object {
        fun newInstance() =
            GRFragment()
    }
}