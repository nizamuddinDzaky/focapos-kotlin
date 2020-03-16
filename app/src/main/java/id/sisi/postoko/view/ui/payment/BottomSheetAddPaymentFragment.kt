package id.sisi.postoko.view.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_payment.*
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetAddPaymentFragment : BottomSheetDialogFragment() {
    private val progressBar = CustomProgressBar()
    lateinit var viewModel: AddPaymentViewModel
    var listener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val idSalesBooking = arguments?.getInt(KEY_ID_SALES_BOOKING) ?: 0

        viewModel = ViewModelProvider(
            this,
            AddPaymentFactory(idSalesBooking)
        ).get(AddPaymentViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                logE("progress")
            } else {
                logE("done")
            }
        })
        tv_add_payment_date?.text = currentDate.toDisplayDate()
        tv_add_payment_date?.tag = currentDate
        btn_confirmation_add_payment?.setOnClickListener {
            actionAddPayment()
        }
    }

    private fun actionAddPayment() {
        context?.let { progressBar.show(it, "Silakan tunggu...") }
        val body = mutableMapOf(
            "date" to (tv_add_payment_date?.tag?.toString() ?: ""),
            "amount_paid" to (et_add_payment_total?.text?.toString() ?: "0"),
            "note" to (et_add_payment_note?.text?.toString() ?: "")
        )
        viewModel.postAddPayment(body) {
            logE("nizamuddin : "+it["networkRespone"])
            if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                this.dismiss()
            }
            Toast.makeText(context, ""+it["message"], Toast.LENGTH_SHORT).show()
            progressBar.dialog.dismiss()
        }
    }
}