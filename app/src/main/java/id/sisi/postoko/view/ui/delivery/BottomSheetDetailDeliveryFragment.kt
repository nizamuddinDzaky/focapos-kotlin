package id.sisi.postoko.view.ui.delivery

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListProductDetailDeliveryAdapter
import id.sisi.postoko.utils.KEY_ID_DELIVERY
import id.sisi.postoko.utils.extensions.*
import kotlinx.android.synthetic.main.fragment_detail_delivery.*


@Suppress("NAME_SHADOWING")
class BottomSheetDetailDeliveryFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: DeliveryDetailViewModel
    private lateinit var adapter: ListProductDetailDeliveryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_detail_delivery, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deliveryId = arguments?.getString(KEY_ID_DELIVERY)
        viewModel = ViewModelProvider(this).get(DeliveryDetailViewModel::class.java)
        setupRecycleView()

        btn_close.setOnClickListener {
            this.dismiss()
        }
        viewModel.getDetailDelivery().observe(viewLifecycleOwner, Observer {
            adapter.updateSalesData(it?.deliveryItems)
            tv_title_bottom_sheet.text = it?.do_reference_no
            tv_date.text = it?.date?.toDisplayDate()
            tv_sale_no.text = it?.sale_reference_no
            tv_customer.text = it?.customer

            if(TextUtils.isEmpty(it?.delivering_date)){
                layout_delivering_date.gone()
            }
            if(TextUtils.isEmpty(it?.delivered_date)){
                layout_delivered_date.gone()
            }

            tv_date_delivery.text = it?.delivering_date?.toDisplayDate()
            tv_date_delivered.text = it?.delivered_date?.toDisplayDate()
            tv_status.text = it?.status?.toDisplayStatus()?.let { it1 -> getText(it1) }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it?.status?.toDisplayStatusColor()?.let { it1 -> context?.getColor(it1)?.let { it1 -> tv_status.setTextColor(it1) } }
            }else{
                it?.status?.toDisplayStatusColor()?.let { it1 ->
                    ResourcesCompat.getColor(resources,
                        it1, null)
                }?.let { it2 -> tv_status.setTextColor(it2) }
            }

            tv_biller_address.text = it?.alamat_biller + " " + it?.state_biller
            tv_biller_email.text = it?.email_biller
            tv_biller_province.text = it?.provinsi_biller
        })

        deliveryId?.toInt()?.let { viewModel.requestDetailDelivery(it) }

    }

    private fun setupRecycleView() {
        adapter = ListProductDetailDeliveryAdapter()
        rv_item_delivery?.layoutManager = LinearLayoutManager(this.context)
        rv_item_delivery?.setHasFixedSize(false)
        rv_item_delivery?.adapter = adapter
    }
}