package id.sisi.postoko.utils.helper

import androidx.fragment.app.Fragment
import id.sisi.postoko.view.ui.addsales.AddItemAddSaleFragment
import id.sisi.postoko.view.ui.addsales.SelectCustomerFragment

enum class AddSaleFragment(val position: Int, val tag: String) {
    SELECT_CUSTOMER(0, SelectCustomerFragment.TAG),
    ADD_PRODUCT(0, AddItemAddSaleFragment.TAG)
}

fun findSaleFragmentByTag(tag: String): AddSaleFragment = when (tag) {
    AddSaleFragment.SELECT_CUSTOMER.tag -> AddSaleFragment.SELECT_CUSTOMER
    AddSaleFragment.ADD_PRODUCT.tag -> AddSaleFragment.ADD_PRODUCT
    else-> AddSaleFragment.SELECT_CUSTOMER
}

fun AddSaleFragment.createFragment(): Fragment = when (this) {
    AddSaleFragment.SELECT_CUSTOMER -> SelectCustomerFragment.newInstance()
    AddSaleFragment.ADD_PRODUCT -> AddItemAddSaleFragment.newInstance()
}

fun AddSaleFragment.getTag(): String = when (this) {
    AddSaleFragment.SELECT_CUSTOMER -> SelectCustomerFragment.TAG
    AddSaleFragment.ADD_PRODUCT -> AddItemAddSaleFragment.TAG
}