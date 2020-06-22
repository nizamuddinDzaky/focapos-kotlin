package id.sisi.postoko.view.ui.customer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AddCustomerPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pages = listOf(
        AddDataCustomerFragment(),
        AddWarehouseCustomerFragment()
    )
    private var currentPosition: Int = 0

    fun getCurrentFragment() = pages[currentPosition-1]

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