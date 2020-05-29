package id.sisi.postoko.view.ui.payment

import id.sisi.postoko.R

enum class PaymentType (val stringId: Int) {
    CASH(R.string.txt_value_cash),
    GIFT_CARD(R.string.txt_value_gift_card),
    CC(R.string.txt_value_credit_card),
    BANK(R.string.txt_value_bank),
}