package id.sisi.postoko.view.pager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.utils.extensions.tryValue
import id.sisi.postoko.view.sales.SBFragment
import id.sisi.postoko.view.ui.sales.SaleStatus.*
import id.sisi.postoko.view.ui.sales.SalesBookingFragment

class SalesBookingPagerAdapter(fm: FragmentManager, private var ctx: Context?, isAksestoko: Boolean) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pages = listOf(
        SBFragment(PENDING, isAksestoko),
        SBFragment(RESERVED, isAksestoko),
        SBFragment(CLOSED, isAksestoko)
        /*SalesBookingFragment(PENDING),
        SalesBookingFragment(RESERVED),
        SalesBookingFragment(CLOSED)*/
    )
    private var currentPosition: Int = 0

    fun getCurrentFragment() = pages[currentPosition]

    override fun getItem(position: Int): Fragment {
        currentPosition = position
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ctx?.getString(PENDING.tryValue(pages[position].tagName)?.stringId ?: 0)
    }
}