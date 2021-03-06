package id.sisi.postoko.utils.helper

import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.view.ui.MasterDataFragment
import id.sisi.postoko.view.ui.dashboard.DashboardFragment
import id.sisi.postoko.view.ui.gr.GoodReceivedRootFragment
import id.sisi.postoko.view.ui.purchase.PurchaseRootFragment
import id.sisi.postoko.view.ui.sales.SaleRootFragment

enum class BottomNavigationPosition(val position: Int, val id: Int) {
    HOME(0, R.id.menu_home),
    RECEIVE(1, R.id.menu_good_receive),
    SALES(2, R.id.menu_sales_booking),
    MASTER(3, R.id.menu_master_data);
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.HOME.id -> BottomNavigationPosition.HOME
    BottomNavigationPosition.RECEIVE.id -> BottomNavigationPosition.RECEIVE
    BottomNavigationPosition.SALES.id -> BottomNavigationPosition.SALES
    BottomNavigationPosition.MASTER.id -> BottomNavigationPosition.MASTER
    else -> BottomNavigationPosition.RECEIVE
}

fun BottomNavigationPosition.createFragment(): Fragment = when (this) {
    BottomNavigationPosition.HOME -> DashboardFragment.newInstance()
    BottomNavigationPosition.RECEIVE -> PurchaseRootFragment.newInstance()
    BottomNavigationPosition.SALES -> SaleRootFragment.newInstance()
    BottomNavigationPosition.MASTER -> MasterDataFragment.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this) {
    BottomNavigationPosition.HOME -> DashboardFragment.TAG
    BottomNavigationPosition.RECEIVE -> PurchaseRootFragment.TAG
    BottomNavigationPosition.SALES -> SaleRootFragment.TAG
    BottomNavigationPosition.MASTER -> MasterDataFragment.TAG
}
