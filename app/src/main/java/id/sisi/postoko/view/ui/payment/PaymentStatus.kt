package id.sisi.postoko.view.ui.payment

import id.sisi.postoko.R

enum class PaymentStatus(val stringId: Int) {
    PENDING(R.string.txt_status_pending),
    PARTIAL(R.string.txt_status_partial),
    PAID(R.string.txt_status_paid),
    DUE(R.string.txt_status_due),
    ALL(R.string.txt_status_all),
}