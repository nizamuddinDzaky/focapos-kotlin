package id.sisi.postoko.view.ui.payment

import id.sisi.postoko.R

enum class PaymentType (val stringId: Int) {
    TUNAI(R.string.txt_payment_cash),
    GIFTCARD(R.string.txt_gift_card),
    CREDITCARD(R.string.txt_credit_card),
    BANK(R.string.txt_bank),
}