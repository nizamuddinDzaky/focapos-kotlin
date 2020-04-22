package id.sisi.postoko.view.ui.sales

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import id.sisi.postoko.R
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.pager.DetailSalesBookingPagerAdapter
import kotlinx.android.synthetic.main.fragment_root_detail_sales_booking.*
import kotlinx.android.synthetic.main.fragment_root_detail_sales_booking.main_container
import kotlinx.android.synthetic.main.fragment_root_detail_sales_booking.main_view_pager
import kotlinx.android.synthetic.main.fragment_root_detail_sales_booking.tabs_main_pagers
import kotlinx.android.synthetic.main.fragment_root_sales.*


class DetailSalesBookingRootFragment : Fragment() {
    private var typeface = TypeFace()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_detail_sales_booking)
        return inflater.inflate(R.layout.fragment_root_detail_sales_booking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        main_container?.gone()
        main_view_pager?.let {
            it.adapter = DetailSalesBookingPagerAdapter(childFragmentManager)
            tabs_main_pagers?.setupWithViewPager(it)
        }
        context?.assets?.let { typeface.fontTab(tabs_main_pagers, "robot_font/Roboto-Regular.ttf", it) }

        main_view_pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int)
            {
                when (position) {
                    0 -> activity?.title = getString(R.string.txt_detail_sales_booking)
                    1 -> activity?.title = getString(R.string.txt_pembayaran)
                    2 -> activity?.title = getString(R.string.txt_pengiriman)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        (main_view_pager?.adapter as? DetailSalesBookingPagerAdapter)?.getCurrentFragment()
            ?.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val TAG: String = DetailSalesBookingRootFragment::class.java.simpleName
        fun newInstance() =
            DetailSalesBookingRootFragment()
    }
}