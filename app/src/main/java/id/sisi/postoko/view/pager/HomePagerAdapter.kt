package id.sisi.postoko.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.view.ui.customer.CustomerFragment
import id.sisi.postoko.view.ui.customergroup.CustomerGrouFragment
import id.sisi.postoko.view.ui.pricegroup.PriceGroupFragment
import id.sisi.postoko.view.ui.product.ProductFragment
import id.sisi.postoko.view.ui.supplier.SupplierFragment
import id.sisi.postoko.view.ui.warehouse.WarehouseFragment

class HomePagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pages = listOf(
        CustomerFragment(),
        CustomerGrouFragment(),
        PriceGroupFragment(),
        ProductFragment(),
        SupplierFragment(),
        WarehouseFragment()
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