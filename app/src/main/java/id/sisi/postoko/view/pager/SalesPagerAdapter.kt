package id.sisi.postoko.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.view.ui.sales.SalesBookingFragment

class SalesPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pages = listOf(
        SalesBookingFragment(),
        SalesBookingFragment(),
        SalesBookingFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pages[position].tagName
    }
}