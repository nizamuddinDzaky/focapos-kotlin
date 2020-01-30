package id.sisi.postoko.utils.helper

import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.view.AccountFragment
import id.sisi.postoko.view.HistoryFragment
import id.sisi.postoko.view.PurchaseFragment

enum class BottomNavigationPosition(val position: Int, val id: Int) {
    PURCHASE(0, R.id.menu_purchase),
    HISTORY(1, R.id.menu_history),
    ACCOUNT(2, R.id.menu_account);
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.PURCHASE.id -> BottomNavigationPosition.PURCHASE
    BottomNavigationPosition.HISTORY.id -> BottomNavigationPosition.HISTORY
    BottomNavigationPosition.ACCOUNT.id -> BottomNavigationPosition.ACCOUNT
    else -> BottomNavigationPosition.PURCHASE
}

fun BottomNavigationPosition.createFragment(): Fragment = when (this) {
    BottomNavigationPosition.PURCHASE -> PurchaseFragment.newInstance()
    BottomNavigationPosition.HISTORY -> HistoryFragment.newInstance()
    BottomNavigationPosition.ACCOUNT -> AccountFragment.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this) {
    BottomNavigationPosition.PURCHASE -> PurchaseFragment.TAG
    BottomNavigationPosition.HISTORY -> HistoryFragment.TAG
    BottomNavigationPosition.ACCOUNT -> AccountFragment.TAG
}
