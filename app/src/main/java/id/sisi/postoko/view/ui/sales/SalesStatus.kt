package id.sisi.postoko.view.ui.sales

import id.sisi.postoko.R

enum class SaleStatus(val stringId: Int) {
    PENDING(R.string.txt_status_pending),
    RESERVED(R.string.txt_status_reserved),
    CLOSED(R.string.txt_status_closed),
    ALL(R.string.txt_status_all),
}