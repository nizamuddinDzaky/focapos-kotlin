package id.sisi.postoko.view.ui.payment

import android.content.Intent
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
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.AddProductActivity
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.pembayaran_fragment.*
import kotlinx.android.synthetic.main.pembayaran_fragment.fb_add_transaction

class PaymentFragment : Fragment(){

    private lateinit var viewModel: PaymentViewModel
    private lateinit var adapter: ListPaymentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pembayaran_fragment, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id_sales_booking = (activity as? DetailSalesBookingActivity)?.id_sales_booking ?: 0
        logE("payment sales booking id $id_sales_booking")

        setupUI()

        viewModel = ViewModelProvider(this, PaymentFactory(id_sales_booking)).get(PaymentViewModel::class.java)
        viewModel.getListPayments().observe(viewLifecycleOwner, Observer {
            adapter.updateData(it)
        })
    }

    private fun setupUI() {
        setupRecycleView()
    }

    private fun setupRecycleView() {
        adapter = ListPaymentAdapter {
            showBottomSheetDialogFragment(it)
        }
        rv_list_item_pembayaran?.layoutManager = LinearLayoutManager(this.context)
        rv_list_item_pembayaran?.setHasFixedSize(false)
        rv_list_item_pembayaran?.adapter = adapter
    }

    private fun showBottomSheetDialogFragment(payment: Payment?) {
        val bottomSheetFragment = BottomSheetDetailPaymentFragment()
        val bundle = Bundle()
        bundle.putParcelable("payment", payment)
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.getTag())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            startActivity(Intent(this.context, AddProductActivity::class.java))
        }
    }

    companion object {
        val TAG: String = PaymentFragment::class.java.simpleName
        fun newInstance() =
            PaymentFragment()
    }
}