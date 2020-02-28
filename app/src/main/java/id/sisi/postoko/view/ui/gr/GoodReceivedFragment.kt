package id.sisi.postoko.view.ui.gr

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListGoodReceivedAdapter
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus.DELIVERING
import kotlinx.android.synthetic.main.fragment_gr.*


class GoodReceivedFragment(var status: GoodReceiveStatus = DELIVERING) : BaseFragment() {

    private lateinit var viewModel: GoodReceivedViewModel
    private lateinit var adapter: ListGoodReceivedAdapter

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

        viewModel = ViewModelProvider(this, GoodReceivedFactory(status.name)).get(GoodReceivedViewModel::class.java)
        viewModel.getListGoodsReceived().observe(viewLifecycleOwner, Observer {
            adapter.updateGoodsReceivedData(it)
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
    }

    private fun setupRecycleView() {
        adapter = ListGoodReceivedAdapter(status = status) {
//            showBottomSheetDialog()
            showBottomSheetDialogFragment(it)
        }
        rv_list_good_received?.layoutManager = LinearLayoutManager(this.context)
        rv_list_good_received?.setHasFixedSize(false)
        rv_list_good_received?.adapter = adapter
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
            GoodReceivedFragment()
    }
}