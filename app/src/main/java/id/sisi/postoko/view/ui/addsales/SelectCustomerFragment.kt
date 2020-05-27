package id.sisi.postoko.view.ui.addsales

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.helper.findSaleFragmentByTag
import id.sisi.postoko.view.ui.sales.FragmentSearchCustomer
import id.sisi.postoko.view.ui.warehouse.WarehouseDialogFragment
import kotlinx.android.synthetic.main.select_customer_add_sale_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class SelectCustomerFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = null
        /*(activity as AppCompatActivity?)?.setSupportActionBar(toolbar)*/
        return inflater.inflate(R.layout.select_customer_add_sale_fragment, container, false)
    }

    private val myDialog = MyDialog()

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AddSaleActivity?)?.currentFragmentTag = TAG

        if ((activity as AddSaleActivity?)?.idWarehouse != null){
            sp_warehouse.setText((activity as AddSaleActivity?)?.warehouseName)
        }

        if ((activity as AddSaleActivity?)?.idCustomer != null){
            sp_customer.setText((activity as AddSaleActivity?)?.customerName)
        }

        sp_warehouse.setOnClickListener {
            val dialogFragment = WarehouseDialogFragment()
            dialogFragment.listener={ warehouse ->
                (activity as AddSaleActivity?)?.idWarehouse = warehouse.id
                (activity as AddSaleActivity?)?.warehouseName = warehouse.name
                sp_warehouse.setText(warehouse.name)
            }
            dialogFragment.show(childFragmentManager, WarehouseDialogFragment().tag)
        }

        sp_customer.setOnClickListener {
            val dialogFragment = FragmentSearchCustomer()
            dialogFragment.listener={ customer ->
                (activity as AddSaleActivity?)?.idCustomer = customer.id
                (activity as AddSaleActivity?)?.customerName = customer.company
                sp_customer.setText(customer.company)
            }
            dialogFragment.show(childFragmentManager, FragmentSearchCustomer().tag)
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        et_date_sale?.setText(currentDate.toDisplayDate())
        et_date_sale?.hint = currentDate.toDisplayDate()
        (activity as AddSaleActivity).date = currentDate
        et_date_sale.setOnClickListener {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = if (et_date_sale.tag == null) {
                inputDateFormat.format(Date())
            } else {
                et_date_sale.tag.toString() + " 00:00:00"
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
                            et_date_sale.setText(selectedDate.toDisplayDate())
                            (activity as AddSaleActivity).date = selectedDate
                        }
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }

        btn_action_submit.setOnClickListener {
            /*when {
                TextUtils.isEmpty((activity as AddSaleActivity?)?.idCustomer) -> {
                    alert.alert(getString(R.string.txt_alert_id_customer), context)
                }
                TextUtils.isEmpty((activity as AddSaleActivity?)?.idWarehouse) -> {
                    alert.alert(getString(R.string.txt_alert_id_warehouse), context)
                }
                else -> {*/
                    (activity as AddSaleActivity?)?.switchFragment(findSaleFragmentByTag(AddItemAddSaleFragment.TAG))
                /*}
            }*/
        }
    }

    companion object {
        val TAG: String = SelectCustomerFragment::class.java.simpleName
        fun newInstance() =
            SelectCustomerFragment()
    }
}