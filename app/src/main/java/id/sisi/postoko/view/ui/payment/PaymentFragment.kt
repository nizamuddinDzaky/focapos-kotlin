package id.sisi.postoko.view.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListPaymentAdapter
import id.sisi.postoko.model.Payment
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.failed_load_data.*
import kotlinx.android.synthetic.main.pembayaran_fragment.*
import kotlinx.android.synthetic.main.pembayaran_fragment.fb_add_transaction

class PaymentFragment : Fragment(){

    private lateinit var viewModel: PaymentViewModel
    private lateinit var adapter: ListPaymentAdapter
    private var idSalesBooking = 0
    private var totalPaid: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pembayaran_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idSalesBooking = (activity as? DetailSalesBookingActivity)?.idSalesBooking ?: 0

        setupUI()

        viewModel = ViewModelProvider(this, PaymentFactory(idSalesBooking)).get(PaymentViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutPaymentSale?.isRefreshing = it
        })
        viewModel.getListPayments().observe(viewLifecycleOwner, Observer {
            adapter.updateData(it)
            totalPaid = sumTotalPaid(it)
            if (it?.size ?: 0 == 0) {
                layout_status_progress?.visible()
                rv_list_item_pembayaran?.gone()
                val status = when(it?.size) {
                    0 -> "Belum ada pembayaran"
                    else -> "Gagal Memuat Data"
                }
                tv_status_progress?.text = status
            } else {
                layout_status_progress?.gone()
                rv_list_item_pembayaran?.visible()
            }
        })

    }

    override fun onResume() {
        val sale = (activity as? DetailSalesBookingActivity)?.tempSale
        if(sale?.paid!! >= sale.grand_total){
            fb_add_transaction.gone()
        }else{
            fb_add_transaction.visible()
        }
        super.onResume()
    }

    private fun sumTotalPaid(it: List<Payment>?): Double {
        var paid = 0.0
        for (x in 0 until it?.size!!){
            paid += it[x].amount
        }
        return paid
    }

    private fun setupUI() {
        setupRecycleView()
        swipeRefreshLayoutPaymentSale?.setOnRefreshListener {
            viewModel.getListPayment()
        }
    }

    private fun setupRecycleView() {
        adapter = ListPaymentAdapter ()

        adapter.listenerEdit = {
            showBottomSheetEditPayment(it)
        }

        adapter.listener={
            val dialogFragment = ImagePaymentDialogFragment()
            val bundle = Bundle()
            bundle.putString("payment", it)
            dialogFragment.arguments = bundle
            dialogFragment.show(childFragmentManager, "dialog")
        }
        rv_list_item_pembayaran?.layoutManager = LinearLayoutManager(this.context)
        rv_list_item_pembayaran?.setHasFixedSize(false)
        rv_list_item_pembayaran?.adapter = adapter
    }

    private fun showBottomSheetEditPayment(payment: Payment?) {
        val sale = (activity as? DetailSalesBookingActivity)?.tempSale
        val bottomSheetFragment = BottomSheetEditPaymentFragment()
        val bundle = Bundle()
        bundle.putParcelable("sale",sale)
        sale?.id?.let { bundle.putInt(KEY_ID_SALES_BOOKING, it) }
        bundle.putParcelable("payment", payment)
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.listener={
            viewModel.getListPayment()
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun showBottomSheetAddPayment(id_sales_booking: Int) {
//        if(totalPaid >= sale?.grand_total!!){
//            AlertDialog.Builder(context)
//                .setTitle("Konfirmasi")
//                .setMessage("Jumlah Pembayaran Telah Mencukupi")
//                .setPositiveButton(android.R.string.ok) { _, _ ->
//                }
//                .show()
//        }else{
            val bottomSheetFragment = BottomSheetAddPaymentFragment()
            val bundle = Bundle()
            val sale = (activity as? DetailSalesBookingActivity)?.tempSale
            bundle.putParcelable("sale",sale)
            bundle.putInt(KEY_ID_SALES_BOOKING, id_sales_booking)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.listener = {
                viewModel.getListPayment()
            }
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            showBottomSheetAddPayment(idSalesBooking)
        }
    }

    companion object {
        val TAG: String = PaymentFragment::class.java.simpleName
        fun newInstance() =
            PaymentFragment()
    }
}