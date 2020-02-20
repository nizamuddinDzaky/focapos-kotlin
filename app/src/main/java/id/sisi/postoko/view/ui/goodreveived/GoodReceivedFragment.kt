package id.sisi.postoko.view.ui.goodreveived

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListGoodReceivedAdapter
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.fragment_gr.*


class GoodReceivedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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