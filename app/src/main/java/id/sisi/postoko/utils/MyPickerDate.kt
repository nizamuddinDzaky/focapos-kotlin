package id.sisi.postoko.utils

import android.app.DatePickerDialog
import android.view.View
import android.widget.TextView
import id.sisi.postoko.utils.extensions.toDisplayDate
import java.text.SimpleDateFormat
import java.util.*

class MyPickerDate(
    private val startDate: TextView,
    btnAction: View? = null,
    initDateCurrent: Boolean = false
) {
    private val systemDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private var currentDate = Date()
    private val currentDateString = systemDateFormat.format(currentDate)
    private var datePickerDialog: DatePickerDialog? = null
    private var chooseDateString: String? = null

    init {
        val calendar: Calendar = GregorianCalendar()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        datePickerDialog = DatePickerDialog(
            startDate.context,
            DatePickerDialog.OnDateSetListener { _, newYear, newMonth, newDay ->
                chooseDateString = "$newYear-${newMonth + 1}-$newDay 00:00:00"
                showDate()
            },
            year,
            month,
            day
        )

        if (initDateCurrent) {
            showDate()
        } else {
            emptyDate()
        }

        btnAction?.setOnClickListener {
            datePickerDialog?.show()
        }
        if (btnAction == null) {
            startDate.setOnClickListener {
                datePickerDialog?.show()
            }
        }
    }

    private fun showDate() {
        (chooseDateString ?: currentDateString)?.let {
            startDate.text = it.toDisplayDate()
            startDate.tag = it
        }
    }

    private fun emptyDate() {
        chooseDateString = null
        startDate.text = "-"
        startDate.tag = null
    }

    fun getDate() = chooseDateString

    fun initDate(dateSystemFormat: String) {
        dateSystemFormat.also {
            chooseDateString = it
            startDate.text = it.toDisplayDate()
            startDate.tag = it

            val resultDate = systemDateFormat.parse(it)
            val calendar: Calendar = GregorianCalendar()
            resultDate?.let {
                calendar.time = resultDate

                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH]
                val day = calendar[Calendar.DAY_OF_MONTH]

                datePickerDialog?.updateDate(year, month, day)
            }
        }
    }

    fun reset() {
        emptyDate()
    }
}