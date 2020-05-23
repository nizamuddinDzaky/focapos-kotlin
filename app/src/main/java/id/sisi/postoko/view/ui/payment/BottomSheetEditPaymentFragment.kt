package id.sisi.postoko.view.ui.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Payment
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.setupFullHeight
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.validation
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_payment.*
import java.text.SimpleDateFormat
import java.util.*

class BottomSheetEditPaymentFragment : BottomSheetDialogFragment() {

    private var sales: Sales? = null
    private lateinit var payment: Payment
    private var mustPaid: Double = 0.0
    lateinit var viewModel: AddPaymentViewModel
    private val progressBar = CustomProgressBar()
    var listener: () -> Unit = {}
    private var alert = MyDialog()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_edit_payment, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            mustPaid += it.amount
        }
        viewModel = ViewModelProvider(this, AddPaymentFactory(idSalesBooking)).get(AddPaymentViewModel::class.java)

        viewModel.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getIsExecute().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                context?.let {c ->
                    progressBar.show(c, getString(R.string.txt_please_wait))
                }
            } else {
                progressBar.dialog.dismiss()
            }
        })

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

        val mandatory = listOf<EditText>(et_add_payment_total)

        btn_confirmation_add_payment?.setOnClickListener {
            if (!mandatory.validation()) {
                return@setOnClickListener
            }
            actionAddPayment()
        }
    }

    private fun actionAddPayment() {
        val validation = validationFormAddPayment()
        if (validation[KEY_VALIDATION_REST]as Boolean) {

            val body = mutableMapOf(
                "date" to (et_add_payment_date?.tag?.toString() ?: ""),
                "amount_paid" to (et_add_payment_total?.tag?.toString() ?: "0"),
                "note" to (et_add_payment_note?.text?.toString() ?: "")
            )
            viewModel.putEditayment(body, payment.id) {
                listener()
                this.dismiss()
            }
        }else{
            alert.alert(validation[KEY_MESSAGE] as String, context)
        }
    }

    private fun validationFormAddPayment(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (et_add_payment_total.tag.toString().toDouble() > mustPaid){
            message += "${getString(R.string.txt_overpayment)} $mustPaid \n"
            cek = false
        }
        return mapOf(KEY_MESSAGE to message, KEY_VALIDATION_REST to cek)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }

}