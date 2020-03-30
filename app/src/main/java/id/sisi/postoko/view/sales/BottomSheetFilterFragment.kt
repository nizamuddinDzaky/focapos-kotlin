package id.sisi.postoko.view.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.ui.payment.PaymentStatus
import id.sisi.postoko.view.ui.sales.SaleStatus
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter_sales.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter_sales.view.*


class BottomSheetFilterFragment : BottomSheetDialogFragment() {
    var listener: (HashMap<String, String>) -> Unit = {}
    private var mFilter = hashMapOf<String, String>()
    private val dataSortBy = mutableListOf<DataSpinner>()
    private val dataSortType = mutableListOf<DataSpinner>()
    private var mStartPickerDate: MyPickerDate? = null
    private var mEndPickerDate: MyPickerDate? = null
    private lateinit var toast: Toast

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toast = MyToast.make(context)
        mFilter =
            (arguments?.getSerializable(KEY_FILTER) as? HashMap<String, String>) ?: hashMapOf()
        return inflater.inflate(R.layout.fragment_bottom_sheet_filter_sales, container, false).apply {
            layout_delivery_status?.checkVisibility(mFilter.containsKey(KEY_IS_SEARCH))

            btn_close?.setOnClickListener {
                dismiss()
            }
            btn_reset?.setOnClickListener {
                resetForm()
            }
            btn_action_submit?.setOnClickListener {
                sendFilter()
            }
            rbtn_cancel?.setOnClickListener(listenerRadioButton)
            rbtn_confirmed?.setOnClickListener(listenerRadioButton)
            rbtn_reserved?.setOnClickListener(listenerRadioButton)
            rbtn_pending?.setOnClickListener(listenerRadioButton)
            rbtn_closed?.setOnClickListener(listenerRadioButton)
            rbtn_partial?.setOnClickListener(listenerRBPaymentStatus)
            rbtn_pay_due?.setOnClickListener(listenerRBPaymentStatus)
            rbtn_paid?.setOnClickListener(listenerRBPaymentStatus)
            rbtn_pay_pending?.setOnClickListener(listenerRBPaymentStatus)

            dataSortBy.add(DataSpinner("Tanggal", "date"))
            dataSortBy.add(DataSpinner("Jumlah", "amount"))
            spinner_sort_by.adapter =
                MySpinnerAdapter(context, android.R.layout.simple_list_item_1, dataSortBy)

            dataSortType.add(DataSpinner("Besar > Kecil", "desc"))
            dataSortType.add(DataSpinner("Kecil > Besar", "asc"))
            spinner_sort_type.adapter =
                MySpinnerAdapter(context, android.R.layout.simple_list_item_1, dataSortType)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mStartPickerDate = MyPickerDate(tv_start_date_range, btn_start_date_range)
        mEndPickerDate = MyPickerDate(tv_end_date_range, btn_end_date_range)

        if (mFilter.containsKey(KEY_START_DATE)) {
            mStartPickerDate?.initDate(mFilter[KEY_START_DATE] as String)
        }
        if (mFilter.containsKey(KEY_END_DATE)) {
            mEndPickerDate?.initDate(mFilter[KEY_END_DATE] as String)
        }

        rbtn_cancel?.isChecked =
            mFilter.containsKey(KEY_SALE_STATUS) && mFilter[KEY_SALE_STATUS] == "SaleStatus.ALL.name"
        rbtn_confirmed?.isChecked =
            mFilter.containsKey(KEY_SALE_STATUS) && mFilter[KEY_SALE_STATUS] == SaleStatus.ALL.name
        rbtn_reserved?.isChecked =
            mFilter.containsKey(KEY_SALE_STATUS) && mFilter[KEY_SALE_STATUS] == SaleStatus.RESERVED.name
        rbtn_pending?.isChecked =
            mFilter.containsKey(KEY_SALE_STATUS) && mFilter[KEY_SALE_STATUS] == SaleStatus.PENDING.name
        rbtn_closed?.isChecked =
            mFilter.containsKey(KEY_SALE_STATUS) && mFilter[KEY_SALE_STATUS] == SaleStatus.CLOSED.name
        rbtn_partial?.isChecked =
            mFilter.containsKey(KEY_PAYMENT_STATUS) && mFilter[KEY_PAYMENT_STATUS] == PaymentStatus.PARTIAL.name
        rbtn_paid?.isChecked =
            mFilter.containsKey(KEY_PAYMENT_STATUS) && mFilter[KEY_PAYMENT_STATUS] == PaymentStatus.PAID.name
        rbtn_pay_pending?.isChecked =
            mFilter.containsKey(KEY_PAYMENT_STATUS) && mFilter[KEY_PAYMENT_STATUS] == PaymentStatus.PENDING.name
        rbtn_pay_due?.isChecked =
            mFilter.containsKey(KEY_PAYMENT_STATUS) && mFilter[KEY_PAYMENT_STATUS] == PaymentStatus.DUE.name

        if (mFilter.containsKey(KEY_SORT_BY)) {
            spinner_sort_by?.setIfExist(mFilter[KEY_SORT_BY] as String)
        }
        if (mFilter.containsKey(KEY_SORT_TYPE)) {
            spinner_sort_type?.setIfExist(mFilter[KEY_SORT_TYPE] as String)
        }
    }

    private val listenerRadioButton = View.OnClickListener {
        checkRadio(it.id)
        val saleStatus = when (it.id) {
            R.id.rbtn_reserved -> SaleStatus.RESERVED.name
            R.id.rbtn_pending -> SaleStatus.PENDING.name
            R.id.rbtn_closed -> SaleStatus.CLOSED.name
            else -> SaleStatus.ALL.name
        }
        mFilter[KEY_SALE_STATUS] = saleStatus.toLower()
    }

    private val listenerRBPaymentStatus = View.OnClickListener {
        checkRadioPayment(it.id)
        val paymentStatus = when (it.id) {
            R.id.rbtn_partial -> PaymentStatus.PARTIAL.name
            R.id.rbtn_paid -> PaymentStatus.PAID.name
            R.id.rbtn_pay_pending -> PaymentStatus.PENDING.name
            R.id.rbtn_pay_due -> PaymentStatus.DUE.name
            else -> null
        }
        paymentStatus?.let {
            mFilter[KEY_PAYMENT_STATUS] = paymentStatus.toLower()
        }
    }

    private fun sendFilter() {
        if (mStartPickerDate?.getDate().isNullOrEmpty() xor mEndPickerDate?.getDate().isNullOrEmpty()) {
            toast.showErrorL("Format range tanggal salah")
            return
        }
        val sortBy = spinner_sort_by?.getSelectedValue()
        val sortType = spinner_sort_type?.getSelectedValue()
        sortBy?.let {
            mFilter[KEY_SORT_BY] = sortBy
        }
        sortType?.let {
            mFilter[KEY_SORT_TYPE] = sortType
        }
        mStartPickerDate?.getDate()?.let {
            mFilter[KEY_START_DATE] = it
        }
        mEndPickerDate?.getDate()?.let {
            mFilter[KEY_END_DATE] = it
        }
        listener(mFilter)
    }

    private fun resetForm() {
        mFilter.remove(KEY_PAYMENT_STATUS)
        mFilter.remove(KEY_SALE_STATUS)
        mFilter.remove(KEY_START_DATE)
        mFilter.remove(KEY_END_DATE)
        mStartPickerDate?.reset()
        mEndPickerDate?.reset()
        spinner_sort_by?.setDefault()
        spinner_sort_type?.setDefault()
        checkRadio(0)
    }

    private fun checkRadio(id: Int) {
        rbtn_cancel?.isChecked = id == R.id.rbtn_cancel
        rbtn_confirmed?.isChecked = id == R.id.rbtn_confirmed
        rbtn_reserved?.isChecked = id == R.id.rbtn_reserved
        rbtn_pending?.isChecked = id == R.id.rbtn_pending
        rbtn_closed?.isChecked = id == R.id.rbtn_closed
    }

    private fun checkRadioPayment(id: Int) {
        rbtn_pending?.isChecked = id == R.id.rbtn_pending
        rbtn_paid?.isChecked = id == R.id.rbtn_paid
        rbtn_pay_pending?.isChecked = id == R.id.rbtn_pay_pending
        rbtn_pay_due?.isChecked = id == R.id.rbtn_pay_due
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            filter: HashMap<String, String>,
            listener: (HashMap<String, String>) -> Unit
        ) {
            val bottomSheetFragment = BottomSheetFilterFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_FILTER, filter)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.listener = listener
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}