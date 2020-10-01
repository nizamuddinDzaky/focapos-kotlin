package id.sisi.postoko.view.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.pager.SalesBookingPagerAdapter
import id.sisi.postoko.view.pager.SalesPagerAdapter
import kotlinx.android.synthetic.main.fragment_root_sales_booking.*

class SaleRootFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.title = getString(R.string.txt_sales)
        return inflater.inflate(R.layout.fragment_root_sales, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        main_container?.gone()
        main_view_pager?.let {
            it.adapter = SalesPagerAdapter(childFragmentManager, context)
            tabs_main_pagers?.setupWithViewPager(it)
        }

    }



    companion object {
        val TAG: String = SaleRootFragment::class.java.simpleName
        fun newInstance() = SaleRootFragment()
    }
}