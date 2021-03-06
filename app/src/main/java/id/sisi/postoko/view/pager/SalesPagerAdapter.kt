package id.sisi.postoko.view.pager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.view.ui.sales.SalesBookingRootFragment

class SalesPagerAdapter(fm: FragmentManager, private var ctx: Context?) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pages = listOf(
        SalesBookingRootFragment(false, "Sales Booking"),
        SalesBookingRootFragment(true, "Akses Toko")
        /*SalesBookingFragment(PENDING),
        SalesBookingFragment(RESERVED),
        SalesBookingFragment(CLOSED)*/
    )
    private var currentPosition: Int = 0

    override fun getItem(position: Int): Fragment {
        currentPosition = position
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pages[position].tagName
    }
}