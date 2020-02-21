package id.sisi.postoko.view.ui.payment

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import id.sisi.postoko.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.adapter.toCurrencyID
import id.sisi.postoko.adapter.toDisplayDate
import id.sisi.postoko.model.Payment


class BottomSheetAddPaymentFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val payment = arguments?.getParcelable<Payment>("payment")

        payment?.let {
        }
    }
}