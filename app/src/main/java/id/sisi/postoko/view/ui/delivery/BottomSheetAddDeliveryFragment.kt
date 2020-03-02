package id.sisi.postoko.view.ui.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemDeliveryAdapter
import id.sisi.postoko.adapter.toDisplayDate
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.MasterDetailViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_delivery.*
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetAddDeliveryFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: AddDeliveryViewModel
    private lateinit var viewModelCustomer: MasterDetailViewModel
    var listener: () -> Unit = {}
    private lateinit var adapter: ListItemDeliveryAdapter
    private var customer: Customer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val sale = arguments?.getParcelable<Sales>("sale_booking")

        viewModel = ViewModelProvider(
            this,
            AddDeliveryFactory(sale?.id ?: 0)
        ).get(AddDeliveryViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it) {
                logE("progress")
            } else {
                logE("done")
            }
        })

        viewModelCustomer = ViewModelProvider(this).get(MasterDetailViewModel::class.java)
        viewModelCustomer.getDetailCustomer().observe(viewLifecycleOwner, Observer {
            customer = it
            et_add_delivery_customer_name?.setText(customer?.name)
            et_add_delivery_customer_address?.setText(customer?.address)
        })
        viewModelCustomer.requestDetailCustomer(sale?.customer_id ?: 1)

        //setupUI
        et_add_delivery_date?.setText(currentDate.toDisplayDate())
        et_add_delivery_date?.hint = currentDate.toDisplayDate()
        et_add_delivery_date?.tag = currentDate
        et_add_delivery_reference_no?.setText(sale?.reference_no)
        adapter = ListItemDeliveryAdapter(sale?.saleItems)
        rv_list_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_data?.setHasFixedSize(false)
        rv_list_data?.adapter = adapter
        btn_confirmation_add_delivery?.setOnClickListener {
            actionAddDelivery()
        }
//        et_add_delivery_customer?.keyListener = null
//        et_add_delivery_customer?.setOnClickListener {
//            showSearchDialog()
//        }
        for (i in 0 until (rg_add_delivery_status?.childCount ?: 0)) {
            (rg_add_delivery_status?.get(i) as? RadioButton)?.tag =
                DeliveryStatus.values()[i].name.toLowerCase(
                    Locale.getDefault()
                )
        }
        rg_add_delivery_status?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
//            rg_add_delivery_status?.tag = radioButton.text
            rg_add_delivery_status?.tag = radioButton.tag
        }
        rg_add_delivery_status?.check(rg_add_delivery_status?.get(0)?.id ?: 0)
    }

/*
    private fun showSearchDialog() {
        val dialogFragment = CustomerSearchDialogFragment()
        val bundle = Bundle()
        customers?.let {
            bundle.putParcelableArrayList(KEY_CUSTOMER_DATA, ArrayList(it))
        }
        dialogFragment.arguments = bundle
        dialogFragment.isCancelable = false
        dialogFragment.show(childFragmentManager, dialogFragment.tag)
    }
*/

    private fun actionAddDelivery() {
        val sale = arguments?.getParcelable<Sales>("sale_booking")

        val saleItems = sale?.saleItems?.map {
            return@map mutableMapOf(
                "sale_items_id" to it.id.toString(),
                "sent_quantity" to it.sent_quantity.toString()
            )
        }

        val body = mutableMapOf(
            "date" to (et_add_delivery_date?.tag?.toString() ?: ""),
            "sale_reference_no" to (et_add_delivery_reference_no?.text?.toString() ?: ""),
            "customer" to (et_add_delivery_customer_name?.text?.toString() ?: ""),
            "address" to (et_add_delivery_customer_address?.text?.toString() ?: ""),
            "delivery_status" to (rg_add_delivery_status?.tag?.toString() ?: ""),
            "delivered_by" to (et_add_delivery_delivered_by?.text?.toString() ?: ""),
            "received_by" to (et_add_delivery_received_by?.text?.toString() ?: ""),
            "products" to saleItems,
            "note" to (et_add_delivery_note?.text?.toString() ?: "")
        )
        viewModel.postAddDelivery(body) {
            listener()
            this.dismiss()
        }
    }
}