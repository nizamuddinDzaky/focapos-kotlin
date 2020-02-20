package id.sisi.postoko.view.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.pager.DetailSalesBookingPagerAdapter
import kotlinx.android.synthetic.main.fragment_root_good_received.*

class DetailSalesBookingRootFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_detail_sales_booking)
        val view = inflater.inflate(R.layout.fragment_root_detail_sales_booking, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        main_container?.gone()
        main_view_pager?.let {
            it.adapter = DetailSalesBookingPagerAdapter(childFragmentManager)
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }

    companion object {
        val TAG: String = DetailSalesBookingRootFragment::class.java.simpleName
        fun newInstance() =
            DetailSalesBookingRootFragment()
    }
}