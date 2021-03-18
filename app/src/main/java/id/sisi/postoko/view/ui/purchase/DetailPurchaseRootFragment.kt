package id.sisi.postoko.view.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import id.sisi.postoko.R
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.pager.DetailSalesBookingPagerAdapter
import id.sisi.postoko.view.pager.PurchasePagerAdapter
import id.sisi.postoko.view.ui.gr.GoodReceivedRootFragment
import id.sisi.postoko.view.ui.sales.DetailSalesBookingRootFragment
import kotlinx.android.synthetic.main.fragment_root_detail_sales_booking.*

class DetailPurchaseRootFragment: Fragment() {

    private val pages = listOf(
        DetailPurchasesFragment.newInstance(),
        PaymentPurchaseFragment.newInstance()
    )

    private var typeface = TypeFace()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_detail_purchase)
        return inflater.inflate(R.layout.fragment_root_detail_sales_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_container?.gone()
        main_view_pager?.let {
            it.adapter = PurchasePagerAdapter(childFragmentManager, pages)
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
                    0 -> activity?.title = getString(R.string.txt_detail_purchase)
                    1 -> activity?.title = getString(R.string.txt_pembayaran)
                }
            }
        })
    }

    companion object {
        val TAG: String = DetailPurchaseRootFragment::class.java.simpleName
        fun newInstance() =
            DetailPurchaseRootFragment()
    }
}