package id.sisi.postoko.view.ui.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemDeliveryAdapter
import id.sisi.postoko.adapter.toDisplayDate
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_delivery.*
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetAddDeliveryFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: AddDeliveryViewModel
    var listener: () -> Unit = {}
    private lateinit var adapter: ListItemDeliveryAdapter

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

        val id_sales_booking = arguments?.getInt(KEY_ID_SALES_BOOKING) ?: 0
        val sale = arguments?.getParcelable<Sales>("sale_booking")

        viewModel = ViewModelProvider(
            this,
            AddDeliveryFactory(id_sales_booking)
        ).get(AddDeliveryViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                logE("progress")
            } else {
                logE("selesai")
            }
        })

        //setupUI
        et_add_delivery_date?.setText(currentDate.toDisplayDate())
        et_add_delivery_date?.tag = currentDate
        et_add_delivery_reference_no?.setText(sale?.reference_no)
        adapter = ListItemDeliveryAdapter(sale?.saleItems)
        rv_list_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_data?.setHasFixedSize(false)
        rv_list_data?.adapter = adapter
        btn_confirmation_add_delivery?.setOnClickListener {
            actionAddDelivery()
        }
        rg_add_delivery_status?.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            rg_add_delivery_status?.tag = radioButton.text
        }
    }

    private fun actionAddDelivery() {
        val sale = arguments?.getParcelable<Sales>("sale_booking")
        val body = mutableMapOf(
            "date" to (et_add_delivery_date?.tag?.toString() ?: ""),
            "sale_reference_no" to (et_add_delivery_reference_no?.text?.toString() ?: ""),
            "customer" to "",
            "address" to "",
            "status" to (rg_add_delivery_status?.tag?.toString() ?: ""),
            "delivered_by" to (et_add_delivery_delivered_by?.text?.toString() ?: ""),
            "received_by" to (et_add_delivery_received_by?.text?.toString() ?: ""),
            "product" to sale?.saleItems,
            "note" to (et_add_delivery_note?.text?.toString() ?: "")
        )
        logE("kirim json $body")
//        viewModel.postAddDelivery(body) {
//            listener()
//            this.dismiss()
//        }
    }
}