package id.sisi.postoko.view.ui.goodreveived

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListGoodReceivedAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_gr.*


class GoodReceivedFragment : BaseFragment() {

    private lateinit var viewModel: GoodReceivedViewModel
    private lateinit var adapter: ListGoodReceivedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gr, container, false)
        return view
    }

    override var tagName: String
        get() = ""
        set(value) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()

//        viewModel = ViewModelProvider(this, SalesBookingFactory(status.name)).get(GoodReceivedViewModel::class.java)
        viewModel = ViewModelProvider(this, GoodReceivedFactory("")).get(GoodReceivedViewModel::class.java)
        viewModel.getListGoodsReceived().observe(viewLifecycleOwner, Observer {
            adapter.updateGoodsReceivedData(it)
        })
    }

    private fun setupUI() {
        setupRecycleView()
    }

    private fun setupRecycleView() {
        adapter = ListGoodReceivedAdapter()
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
//        val TAG: String = GoodReceived::class.java.simpleName
        var CATEGORY: CategoryPurchasePage =
    CategoryPurchasePage.PROCESS

        fun newInstance() =
            GoodReceivedFragment()
    }

    enum class CategoryPurchasePage(val position: Int, val title: Int) {
        PROCESS(0, R.string.txt_category_purchase_process),
//        FINISH(1, R.string.txt_category_purchase_finish);
    }
}