package id.sisi.postoko.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.view.ui.dashboard.DashboardPisechartFragment
import java.util.*


class DashboardPieChartAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var currentPage = 0
    private val today = Calendar.getInstance()
    private val pages: MutableList<Fragment> = mutableListOf(
        DashboardPisechartFragment(0),
        DashboardPisechartFragment(1),
        DashboardPisechartFragment(2),
        DashboardPisechartFragment(3),
        DashboardPisechartFragment(4),
        DashboardPisechartFragment(5),
        DashboardPisechartFragment(6),
        DashboardPisechartFragment(7),
        DashboardPisechartFragment(8),
        DashboardPisechartFragment(9),
        DashboardPisechartFragment(10),
        DashboardPisechartFragment(11)
    )

    override fun getItem(position: Int): Fragment {
        currentPage = position
        return pages[position]
    }

    fun getCurrentFragment() = pages[currentPage]

    override fun getCount(): Int {
        return pages.size
    }


}