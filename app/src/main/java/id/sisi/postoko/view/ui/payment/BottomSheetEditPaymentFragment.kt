package id.sisi.postoko.view.ui.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Payment
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_payment.*
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetEditPaymentFragment : BottomSheetDialogFragment() {
//    private val numberSparator id.sisi.postoko.utils.NumberSeparator()
    private var sales: Sales? = null
    private lateinit var payment: Payment
    private var mustPaid: Double = 0.0
    lateinit var viewModel: AddPaymentViewModel
    private val progressBar = CustomProgressBar()
    var listener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_edit_payment, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())*/

        et_add_payment_total.addTextChangedListener(NumberSeparator(et_add_payment_total))
        sales = arguments?.getParcelable("sale")
        val idSalesBooking = arguments?.getInt(KEY_ID_SALES_BOOKING) ?: 0
        mustPaid = sales?.paid?.let { sales?.grand_total?.minus(it) } ?: 0.0
        arguments?.getParcelable<Payment>("payment")?.let {
            payment = it
            et_add_payment_reference.setText(it.reference_no)
            et_add_payment_date?.setText(it.date.toDisplayDate())
            et_add_payment_date?.hint = it.date.toDisplayDate()
            et_add_payment_date?.tag = it.date
            et_add_payment_total.setText(it.amount.toInt().toString())
            et_add_payment_note.setText(it.note)
            mustPaid = mustPaid + it.amount
        }
        viewModel = ViewModelProvider(this, AddPaymentFactory(idSalesBooking)).get(AddPaymentViewModel::class.java)

        et_add_payment_date.setOnClickListener {

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val date = if (et_add_payment_date.tag == null){
                inputDateFormat.format(Date())
            }else{
                et_add_payment_date.tag.toString() + " 00:00:00"
            }
            val resultDate = inputDateFormat.parse(date)
            val calendar: Calendar = GregorianCalendar()
            resultDate?.let {
                calendar.time = resultDate
            }
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]

            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, _, monthOfYear, dayOfMonth ->
                        val parseDate =
                            inputDateFormat.parse("$year-${monthOfYear + 1}-$dayOfMonth 00:00:00")
                        parseDate?.let {
                            val selectedDate = inputDateFormat.format(parseDate)
                            et_add_payment_date.setText(selectedDate.toDisplayDate())
                            et_add_payment_date?.tag = selectedDate

                        }
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }

        btn_close.setOnClickListener {
            this.dismiss()
        }

        btn_confirmation_add_payment?.setOnClickListener {
            actionAddPayment()
        }
    }

    private fun actionAddPayment() {
        logE("total : $mustPaid")
        val numbersMap = validationFormAddPayment()
        if (numbersMap["type"] as Boolean) {
            context?.let { progressBar.show(it, "Silakan tunggu...") }

            val body = mutableMapOf(
                "date" to (et_add_payment_date?.tag?.toString() ?: ""),
                "amount_paid" to (et_add_payment_total?.tag?.toString() ?: "0"),
                "note" to (et_add_payment_note?.text?.toString() ?: "")
            )
            logE("nizamuddin $body")
            viewModel.putEditayment(body, payment.id) {
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    listener()
                    this.dismiss()
                }
                Toast.makeText(context, "" + it["message"], Toast.LENGTH_SHORT).show()
                progressBar.dialog.dismiss()
            }
        }else{
            AlertDialog.Builder(context)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    private fun validationFormAddPayment(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (et_add_payment_total.tag.toString().toDouble() > mustPaid){
            message += "- Payment Melebihi\n"
            cek = false
        }

        if (et_add_payment_total.text.toString() == ""){
            message += "- Payment Tidak Boleh Kosong\n"
            cek = false
        }
        return mapOf("message" to message, "type" to cek)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int { // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay
            .getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}