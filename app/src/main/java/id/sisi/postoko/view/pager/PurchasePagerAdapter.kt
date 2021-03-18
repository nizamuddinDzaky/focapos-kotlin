package id.sisi.postoko.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.view.BaseFragment

class PurchasePagerAdapter(fm: FragmentManager, val pages: List<BaseFragment>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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