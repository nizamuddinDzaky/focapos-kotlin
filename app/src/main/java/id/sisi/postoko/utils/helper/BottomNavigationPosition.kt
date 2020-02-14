package id.sisi.postoko.utils.helper

import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.view.GoodReceivedFragment
import id.sisi.postoko.view.HistoryFragment
import id.sisi.postoko.view.ui.MasteDataFragment
import id.sisi.postoko.view.ui.dashboard.DashboardFragment

enum class BottomNavigationPosition(val position: Int, val id: Int) {
    HOME(0, R.id.menu_home),
    RECEIVE(1, R.id.menu_good_receive),
    DELIVERY(2, R.id.menu_confirmation_delivery),
    MASTER(3, R.id.menu_master_data);
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.HOME.id -> BottomNavigationPosition.HOME
    BottomNavigationPosition.RECEIVE.id -> BottomNavigationPosition.RECEIVE
    BottomNavigationPosition.DELIVERY.id -> BottomNavigationPosition.DELIVERY
    BottomNavigationPosition.MASTER.id -> BottomNavigationPosition.MASTER
    else -> BottomNavigationPosition.RECEIVE
}

fun BottomNavigationPosition.createFragment(): Fragment = when (this) {
    BottomNavigationPosition.HOME -> DashboardFragment.newInstance()
    BottomNavigationPosition.RECEIVE -> GoodReceivedFragment.newInstance()
    BottomNavigationPosition.DELIVERY -> HistoryFragment.newInstance()
    BottomNavigationPosition.MASTER -> MasteDataFragment.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this) {
    BottomNavigationPosition.HOME -> DashboardFragment.TAG
    BottomNavigationPosition.RECEIVE -> GoodReceivedFragment.TAG
    BottomNavigationPosition.DELIVERY -> HistoryFragment.TAG
    BottomNavigationPosition.MASTER -> MasteDataFragment.TAG
}
