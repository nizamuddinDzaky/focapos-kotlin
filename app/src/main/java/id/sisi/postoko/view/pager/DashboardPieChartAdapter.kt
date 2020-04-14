package id.sisi.postoko.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.dashboard.DashboardPiechartFragment
import java.util.*

class DashboardPieChartAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var currentPage = 0
    private val pages: MutableList<Fragment> = mutableListOf(
        DashboardPiechartFragment(0),
        DashboardPiechartFragment(1),
        DashboardPiechartFragment(2),
        DashboardPiechartFragment(3),
        DashboardPiechartFragment(4),
        DashboardPiechartFragment(5),
        DashboardPiechartFragment(6),
        DashboardPiechartFragment(7),
        DashboardPiechartFragment(8),
        DashboardPiechartFragment(9),
        DashboardPiechartFragment(10),
        DashboardPiechartFragment(11)
    )

    override fun getItem(position: Int): Fragment {
        currentPage = position
        return pages[position]
    }

    override fun getItemPosition(x: Any): Int {

        return POSITION_UNCHANGED
    }

    fun getCurrentFragment(): DashboardPiechartFragment {
        logE("current Fragment : ${pages[currentPage].tag} => $currentPage")
        return pages[currentPage] as DashboardPiechartFragment
    }

    override fun getCount(): Int {
        return pages.size
    }
}

