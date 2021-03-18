package id.sisi.postoko.view.ui.purchase

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
import id.sisi.postoko.utils.helper.findPurchaseFragmentByTag
import id.sisi.postoko.view.ui.addsales.AddSaleActivity
import id.sisi.postoko.view.ui.supplier.SupplierDialogFragment
import id.sisi.postoko.view.ui.warehouse.WarehouseDialogFragment
import kotlinx.android.synthetic.main.select_customer_add_sale_fragment.*
import kotlinx.android.synthetic.main.select_supplier_add_purchase_fragment.*
import kotlinx.android.synthetic.main.select_supplier_add_purchase_fragment.btn_action_submit
import kotlinx.android.synthetic.main.select_supplier_add_purchase_fragment.sp_warehouse
import java.text.SimpleDateFormat
import java.util.*

class SelectSupplierFragment: Fragment() {

    private val myDialog = MyDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = null
        /*(activity as AppCompatActivity?)?.setSupportActionBar(toolbar)*/
        return inflater.inflate(R.layout.select_supplier_add_purchase_fragment, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ((activity as AddPurchaseActivity?)?.idWarehouse != null){
            sp_warehouse.setText((activity as AddPurchaseActivity?)?.warehouseName)
        }

        if ((activity as AddPurchaseActivity?)?.idSupplier != null){
            sp_supplier.setText((activity as AddPurchaseActivity?)?.supplierName)
        }

        (activity as AddPurchaseActivity?)?.currentFragmentTag = TAG
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        et_date_purchase?.setText(currentDate.toDisplayDate())
        et_date_purchase?.hint = currentDate.toDisplayDate()
        et_date_purchase?.tag = currentDate
        (activity as AddPurchaseActivity).date = currentDate
        et_date_purchase.setOnClickListener {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val resultDate = inputDateFormat.parse(currentDate)
            val calendar: Calendar = GregorianCalendar()
            resultDate?.let {
                calendar.time = resultDate
            }
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]

            val dpd = context?.let { c ->
                DatePickerDialog(
                    c,
                    DatePickerDialog.OnDateSetListener { _, _, monthOfYear, dayOfMonth ->
                        val strTime = time.format(Date())
                        val parseDate =
                            inputDateFormat.parse("$year-${monthOfYear + 1}-$dayOfMonth $strTime")
                        parseDate?.let {
                            val selectedDate = inputDateFormat.format(it)
                            et_date_purchase.setText(selectedDate.toDisplayDate())
                            (activity as AddPurchaseActivity).date = selectedDate
                        }
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }

        sp_warehouse.setOnClickListener {
            val dialogFragment = WarehouseDialogFragment()
            dialogFragment.listener={ warehouse ->
                (activity as AddPurchaseActivity?)?.idWarehouse = warehouse.id
                (activity as AddPurchaseActivity?)?.warehouseName = warehouse.name
                sp_warehouse.setText(warehouse.name)
            }
            dialogFragment.show(childFragmentManager, WarehouseDialogFragment().tag)
        }

        sp_supplier.setOnClickListener {
            val dialogFragment = SupplierDialogFragment()
            dialogFragment.listener={ supplier ->
                (activity as AddPurchaseActivity?)?.idSupplier = supplier.id
                (activity as AddPurchaseActivity?)?.supplierName = supplier.name
                sp_supplier.setText(supplier.name)

                (activity as AddPurchaseActivity?)?.vmProduct?.getListProductPurchase(
                    supplier.id.toInt()
                ){listProduct ->
                    if (listProduct != null) {
                        (activity as AddPurchaseActivity?)?.listProduct = listProduct
                    }
                }

            }
            dialogFragment.show(childFragmentManager, SupplierDialogFragment().tag)
        }

        btn_action_submit.setOnClickListener {
            when {
                TextUtils.isEmpty((activity as AddPurchaseActivity?)?.idSupplier) -> {
                    myDialog.alert(getString(R.string.txt_alert_id_customer), context)
                }
                TextUtils.isEmpty((activity as AddPurchaseActivity?)?.idWarehouse) -> {
                    myDialog.alert(getString(R.string.txt_alert_id_warehouse), context)
                }
                else -> {
                    (activity as AddPurchaseActivity?)?.switchFragment(
                        findPurchaseFragmentByTag(
                            AddItemPurchaseFragment.TAG)
                    )
                }
            }
        }
    }

    companion object {
        val TAG: String = SelectSupplierFragment::class.java.simpleName
        fun newInstance() =
            SelectSupplierFragment()
    }
}