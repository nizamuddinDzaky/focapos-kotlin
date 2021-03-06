package id.sisi.postoko.view.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.pager.DetailSalesBookingPagerAdapter
import kotlinx.android.synthetic.main.fragment_root_detail_sales_booking.*


class DetailPaymentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_detail_sales_booking)
        return inflater.inflate(R.layout.fragment_detail_payment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        main_container?.gone()
        main_view_pager?.let {
            it.adapter = DetailSalesBookingPagerAdapter(childFragmentManager)
            tabs_main_pagers?.setupWithViewPager(it)
        }
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

    companion object {
        val TAG: String = DetailPaymentFragment::class.java.simpleName
        fun newInstance() =
            DetailPaymentFragment()
    }
}