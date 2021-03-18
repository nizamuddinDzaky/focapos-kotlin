package id.sisi.postoko.utils.helper

import androidx.fragment.app.Fragment
import id.sisi.postoko.view.ui.purchase.AddItemPurchaseFragment
import id.sisi.postoko.view.ui.purchase.PaymentAddPurchaseFragment
import id.sisi.postoko.view.ui.purchase.SelectSupplierFragment

enum class AddPurchaseFragment(val position: Int, val tag: String) {
    SELECT_SUPPLIER(0, SelectSupplierFragment.TAG),
    ADD_PRODUCT(1, AddItemPurchaseFragment.TAG),
    PAYMENT(2, PaymentAddPurchaseFragment.TAG)
}

fun findPurchaseFragmentByTag(tag: String): AddPurchaseFragment = when (tag) {
    AddPurchaseFragment.SELECT_SUPPLIER.tag -> AddPurchaseFragment.SELECT_SUPPLIER
    AddPurchaseFragment.ADD_PRODUCT.tag -> AddPurchaseFragment.ADD_PRODUCT
    AddPurchaseFragment.PAYMENT.tag -> AddPurchaseFragment.PAYMENT
    else-> AddPurchaseFragment.SELECT_SUPPLIER
}

fun AddPurchaseFragment.createFragment(): Fragment = when (this) {
    AddPurchaseFragment.SELECT_SUPPLIER -> SelectSupplierFragment.newInstance()
    AddPurchaseFragment.ADD_PRODUCT -> AddItemPurchaseFragment.newInstance()
    AddPurchaseFragment.PAYMENT -> PaymentAddPurchaseFragment.newInstance()
}

fun AddPurchaseFragment.getTag(): String = when (this) {
    AddPurchaseFragment.SELECT_SUPPLIER -> SelectSupplierFragment.TAG
    AddPurchaseFragment.ADD_PRODUCT -> AddItemPurchaseFragment.TAG
    AddPurchaseFragment.PAYMENT -> PaymentAddPurchaseFragment.TAG
}
