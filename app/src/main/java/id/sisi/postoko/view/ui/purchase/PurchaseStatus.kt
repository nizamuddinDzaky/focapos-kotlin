package id.sisi.postoko.view.ui.purchase

import id.sisi.postoko.R


enum class PurchaseStatus(val stringId: Int) {
    PENDING(R.string.txt_status_pending),
    RECEIVED(R.string.txt_status_received),
    RETURNED(R.string.txt_status_returned),
    ALL(R.string.txt_status_all),
}