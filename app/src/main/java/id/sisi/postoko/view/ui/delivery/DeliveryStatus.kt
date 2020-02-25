package id.sisi.postoko.view.ui.delivery

import id.sisi.postoko.R

enum class DeliveryStatus(val stringId: Int) {
    PACKING(R.string.txt_status_packing),
    DELIVERING(R.string.txt_status_delivering),
    DELIVERED(R.string.txt_status_delivered),
    RETURNED(R.string.txt_status_returned)
}