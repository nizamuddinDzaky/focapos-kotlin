package id.sisi.postoko.view.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sisi.postoko.R
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.payment.PaymentFragment

class PaymentPurchaseFragment: BaseFragment() {

    override var tagName: String
        get() = "Pembayaran"
        set(_) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pembayaran_fragment, container, false)
    }

    companion object {
        val TAG: String = PaymentPurchaseFragment::class.java.simpleName
        fun newInstance() =
            PaymentPurchaseFragment()
    }
}