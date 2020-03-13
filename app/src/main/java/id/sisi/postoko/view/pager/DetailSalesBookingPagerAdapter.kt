package id.sisi.postoko.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.view.ui.delivery.DeliveryFragment
import id.sisi.postoko.view.ui.payment.PaymentFragment
import id.sisi.postoko.view.ui.sales.DetailSalesBookingFragment

class DetailSalesBookingPagerAdapter (fm: FragmentManager):
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    private val pages = listOf(
        DetailSalesBookingFragment(),
        PaymentFragment(),
        DeliveryFragment()
    )
    private var currentPosition: Int = 0

    fun getCurrentFragment() = pages[currentPosition]

    fun getpPosisition() :Int = currentPosition

    override fun getItem(position: Int): Fragment {
        currentPosition = position
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            1 -> "PEMBAYARAN"
            2 -> "PENGIRIMAN"
            else -> "DETAIL"
        }
    }
}