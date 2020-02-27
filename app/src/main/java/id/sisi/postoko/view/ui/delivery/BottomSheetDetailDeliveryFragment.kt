package id.sisi.postoko.view.ui.delivery

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import id.sisi.postoko.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.adapter.toCurrencyID
import id.sisi.postoko.adapter.toDisplayDate
import id.sisi.postoko.model.Delivery


class BottomSheetDetailDeliveryFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val delivery = arguments?.getParcelable<Delivery>("delivery")

        delivery?.let {
//            tv_payment_detail_reference_no?.text = it.reference_no
//            tv_payment_detail_date?.text = it.date.toDisplayDate()
////            tv_payment_detail_payment_type?.text = it.type
//            tv_payment_detail_payment_type?.text = it.paid_by
//            tv_payment_detail_total?.text = it.amount.toCurrencyID()
//            tv_payment_detail_note?.text = it.note
        }
    }
}