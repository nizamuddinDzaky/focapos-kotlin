package id.sisi.postoko.view.ui.pricegroup

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.view.BaseFragment

class CustomerPriceGroupPagerAdapter(
    fm: FragmentManager,
    private val pages: MutableList<BaseFragment>
) :
    FragmentPagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getItem(position: Int) = pages[position]

    override fun getCount() = pages.size

    override fun getPageTitle(position: Int) = pages[position].tagName
}