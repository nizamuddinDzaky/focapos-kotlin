package id.sisi.postoko.utils.helper

import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.view.AccountFragment
import id.sisi.postoko.view.HistoryFragment
import id.sisi.postoko.view.HomeFragment
import id.sisi.postoko.view.GoodReceivedFragment

enum class BottomNavigationPosition(val position: Int, val id: Int) {
    HOME(0, R.id.menu_home),
    PURCHASE(1, R.id.menu_purchase),
    HISTORY(2, R.id.menu_history),
    ACCOUNT(3, R.id.menu_account);
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.HOME.id -> BottomNavigationPosition.HOME
    BottomNavigationPosition.PURCHASE.id -> BottomNavigationPosition.PURCHASE
    BottomNavigationPosition.HISTORY.id -> BottomNavigationPosition.HISTORY
    BottomNavigationPosition.ACCOUNT.id -> BottomNavigationPosition.ACCOUNT
    else -> BottomNavigationPosition.PURCHASE
}

fun BottomNavigationPosition.createFragment(): Fragment = when (this) {
    BottomNavigationPosition.HOME -> HomeFragment.newInstance()
    BottomNavigationPosition.PURCHASE -> GoodReceivedFragment.newInstance()
    BottomNavigationPosition.HISTORY -> HistoryFragment.newInstance()
    BottomNavigationPosition.ACCOUNT -> AccountFragment.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this) {
    BottomNavigationPosition.HOME -> HomeFragment.TAG
    BottomNavigationPosition.PURCHASE -> GoodReceivedFragment.TAG
    BottomNavigationPosition.HISTORY -> HistoryFragment.TAG
    BottomNavigationPosition.ACCOUNT -> AccountFragment.TAG
}
