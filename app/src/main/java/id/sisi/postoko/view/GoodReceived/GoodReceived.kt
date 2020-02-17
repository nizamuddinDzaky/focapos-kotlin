package id.sisi.postoko.view.GoodReceived

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListGoodReceivedAdapter
import kotlinx.android.synthetic.main.fragment_gr.*


class GoodReceived : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //activity?.title = getString(R.string.txt_purchase)
        activity?.title = getString(CATEGORY.title)
        val view = inflater.inflate(R.layout.fragment_gr, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_list_good_received?.layoutManager = LinearLayoutManager(this.context)
        rv_list_good_received?.setHasFixedSize(false)
        rv_list_good_received?.adapter = ListGoodReceivedAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_search_good_receive, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.menu_action_search -> {
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

        fun newInstance() = GoodReceived()
    }

    enum class CategoryPurchasePage(val position: Int, val title: Int) {
        PROCESS(0, R.string.txt_category_purchase_process),
//        FINISH(1, R.string.txt_category_purchase_finish);
    }
}