package id.sisi.postoko.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListPurchaseAdapter
import kotlinx.android.synthetic.main.fragment_gr.*


class GoodReceived : Fragment (){

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

        rv_list_product?.layoutManager = LinearLayoutManager(this.context)
        rv_list_product?.setHasFixedSize(false)
        rv_list_product?.adapter = ListPurchaseAdapter()
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
        return (when(item.itemId) {
            R.id.menu_action_search -> {
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    companion object {
        val TAG: String = GoodReceived::class.java.simpleName
        var CATEGORY: CategoryPurchasePage = CategoryPurchasePage.PROCESS
        fun newInstance() = GoodReceived()
    }

    enum class CategoryPurchasePage(val position: Int, val title: Int) {
        PROCESS(0, R.string.txt_category_purchase_process),
        FINISH(1, R.string.txt_category_purchase_finish);
    }
}