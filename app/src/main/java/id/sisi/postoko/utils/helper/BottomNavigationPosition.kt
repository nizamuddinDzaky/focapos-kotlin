package id.sisi.postoko.utils.helper

import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.view.ui.dashboard.AccountFragment
import id.sisi.postoko.view.HistoryFragment
import id.sisi.postoko.view.ui.dashboard.HomeFragment
import id.sisi.postoko.view.PurchaseFragment
import id.sisi.postoko.view.ui.MasteDataFragment
import id.sisi.postoko.view.ui.dashboard.DashboardFragment

enum class BottomNavigationPosition(val position: Int, val id: Int) {
    HOME(0, R.id.menu_home),
    SALES(1, R.id.menu_sales_booking),
    RECEIVE(2, R.id.menu_good_receive),
    MASTER(3, R.id.menu_master_data);
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.HOME.id -> BottomNavigationPosition.HOME
    BottomNavigationPosition.SALES.id -> BottomNavigationPosition.SALES
    BottomNavigationPosition.RECEIVE.id -> BottomNavigationPosition.RECEIVE
    BottomNavigationPosition.MASTER.id -> BottomNavigationPosition.MASTER
    else -> BottomNavigationPosition.SALES
}

fun BottomNavigationPosition.createFragment(): Fragment = when (this) {
    BottomNavigationPosition.HOME -> DashboardFragment.newInstance()
    BottomNavigationPosition.SALES -> PurchaseFragment.newInstance()
    BottomNavigationPosition.RECEIVE -> HistoryFragment.newInstance()
    BottomNavigationPosition.MASTER -> MasteDataFragment.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this) {
    BottomNavigationPosition.HOME -> HomeFragment.TAG
    BottomNavigationPosition.SALES -> PurchaseFragment.TAG
    BottomNavigationPosition.RECEIVE -> HistoryFragment.TAG
    BottomNavigationPosition.MASTER -> AccountFragment.TAG
}
