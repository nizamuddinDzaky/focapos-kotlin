package id.sisi.postoko.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.view.AccountFragment
import id.sisi.postoko.view.HistoryFragment
import id.sisi.postoko.view.PurchaseFragment
import id.sisi.postoko.view.ui.customer.CustomerFragment
import id.sisi.postoko.view.ui.product.ProductFragment
import id.sisi.postoko.view.ui.supplier.SupplierFragment
import id.sisi.postoko.view.ui.warehouse.WarehouseFragment

class HomePagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pages = listOf(
        CustomerFragment(),
        ProductFragment(),
        SupplierFragment(),
        WarehouseFragment()
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