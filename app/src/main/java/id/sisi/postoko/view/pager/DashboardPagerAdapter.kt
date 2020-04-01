package id.sisi.postoko.view.pager

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.dashboard.DashboardPagerFragment
import id.sisi.postoko.view.ui.sales.SaleStatus


class DashboardPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var currentPage = 0
    private val pages: MutableList<Fragment> = mutableListOf(
        DashboardPagerFragment(SaleStatus.PENDING),
        DashboardPagerFragment(SaleStatus.RESERVED),
        DashboardPagerFragment(SaleStatus.CLOSED),
        DashboardPagerFragment(SaleStatus.RESERVED)
    )
    private var mFragMan: FragmentManager? = fm
    override fun getItem(position: Int): Fragment {
        currentPage = position
        return pages[position]
    }

    fun getCurrentPosition() = currentPage

    override fun getCount(): Int {
        return pages.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        val cf: DashboardPagerFragment = `object` as DashboardPagerFragment
        mFragMan?.beginTransaction()?.remove(cf)?.commit()
        pages.add(position, DashboardPagerFragment(cf.getmParam1()))
        logE("nizamuddin: $position , ${position+1}, ${pages.size}")
        pages.removeAt(position + 1)
    }
}