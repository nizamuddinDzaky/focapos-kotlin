package id.sisi.postoko.utils.extensions

import id.sisi.postoko.utils.ROLE_CASHIER
import id.sisi.postoko.utils.ROLE_SUPER_ADMIN

fun Int.isSuperAdmin() = this == ROLE_SUPER_ADMIN

fun Int.isNotCashier() = this != ROLE_CASHIER