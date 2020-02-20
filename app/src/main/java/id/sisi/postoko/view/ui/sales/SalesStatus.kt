package id.sisi.postoko.view.ui.sales

import id.sisi.postoko.R

enum class SaleStatus(val stringId: Int) {
    PENDING(R.string.txt_status_pending),
    CONFIRMED(R.string.txt_status_confirmed),
    RESERVED(R.string.txt_status_reserved),
    CLOSED(R.string.txt_status_closed),
    CANCELED(R.string.txt_status_canceled)
}