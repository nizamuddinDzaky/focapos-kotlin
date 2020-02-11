package id.sisi.postoko.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListProductAdapter
import id.sisi.postoko.adapter.ListPurchaseAdapter
import kotlinx.android.synthetic.main.fragment_purchase.*

class PurchaseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //activity?.title = getString(R.string.txt_purchase)
        activity?.title = getString(CATEGORY.title)
        val view = inflater.inflate(R.layout.fragment_purchase, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_list_product?.layoutManager = LinearLayoutManager(this.context)
        rv_list_product?.setHasFixedSize(false)
        rv_list_product?.adapter = ListPurchaseAdapter()
    }

    companion object {
        val TAG: String = PurchaseFragment::class.java.simpleName
        var CATEGORY: CategoryPurchasePage = CategoryPurchasePage.PROCESS
        fun newInstance() = PurchaseFragment()
    }

    enum class CategoryPurchasePage(val position: Int, val title: Int) {
        PROCESS(0, R.string.txt_category_purchase_process),
        FINISH(1, R.string.txt_category_purchase_finish);
    }
}