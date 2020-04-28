package id.sisi.postoko.view.ui.delivery

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Delivery
import id.sisi.postoko.utils.KEY_ID_DELIVERY
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toDisplayDate
import id.sisi.postoko.utils.extensions.toDisplayStatus
import id.sisi.postoko.utils.extensions.toDisplayStatusColor
import kotlinx.android.synthetic.main.fragment_detail_delivery.*


@Suppress("NAME_SHADOWING")
class BottomSheetDetailDeliveryFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: DeliveryDetailViewModel

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

        viewModel.getDetailDelivery().observe(viewLifecycleOwner, Observer {
            tv_no_do.text = it?.do_reference_no
            tv_date.text = it?.date?.toDisplayDate()

            tv_status.text = it?.status?.toDisplayStatus()?.let { it1 -> getText(it1) }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it?.status?.toDisplayStatusColor()?.let { it1 -> context?.getColor(it1)?.let { it1 -> tv_status.setTextColor(it1) } }
            }else{
                it?.status?.toDisplayStatusColor()?.let { it1 ->
                    ResourcesCompat.getColor(resources,
                        it1, null)
                }?.let { it2 -> tv_status.setTextColor(it2) }
            }

            tv_biller_name.text = it?.biller
            tv_biller_address.text = it?.alamat_biller + " " + it?.state_biller
            tv_biller_email.text = it?.email_biller
            tv_biller_province.text = it?.provinsi_biller
            tv_customer_name.text = it?.customer
            tv_customer_address.text = it?.alamat_customer + " " + it?.state_customer
            tv_customer_email.text = it?.email_customer
            tv_customer_province.text = it?.provinsi_customer

            
        })

        deliveryId?.toInt()?.let { viewModel.requestDetailDelivery(it) }

    }
}