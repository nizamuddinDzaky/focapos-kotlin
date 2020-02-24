package id.sisi.postoko.view.ui.delivery

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
import id.sisi.postoko.adapter.ListPengirimanAdapter
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.AddProductActivity
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.pengiriman_fragment.*

class DeliveryFragment : Fragment(){

    private lateinit var viewModel: DeliveryViewModel
    private lateinit var adapter: ListPengirimanAdapter
    private var id_sales_booking = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pengiriman_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id_sales_booking = (activity as? DetailSalesBookingActivity)?.id_sales_booking ?: 0
        logE("delivery sales booking id $id_sales_booking")

        setupUI()

        viewModel = ViewModelProvider(this, DeliveryFactory(id_sales_booking)).get(DeliveryViewModel::class.java)
        viewModel.getListDeliveries().observe(viewLifecycleOwner, Observer {
            adapter.updateData(it)
        })
        rv_list_item_pengiriman?.layoutManager = LinearLayoutManager(this.context)
        rv_list_item_pengiriman?.setHasFixedSize(false)
        rv_list_item_pengiriman?.adapter = ListPengirimanAdapter()
    }

    private fun setupUI() {
        setupRecycleView()
    }

    private fun setupRecycleView() {
        adapter = ListPengirimanAdapter {
            //            showBottomSheetDetailPayment(it)
        }
        rv_list_item_pengiriman?.layoutManager = LinearLayoutManager(this.context)
        rv_list_item_pengiriman?.setHasFixedSize(false)
        rv_list_item_pengiriman?.adapter = adapter
    }

    private fun showBottomSheetAddPayment(id_sales_booking: Int) {
        val sale = (activity as? DetailSalesBookingActivity)?.tempSale

        val bottomSheetFragment = BottomSheetAddDeliveryFragment()
        val bundle = Bundle()
        bundle.putInt(KEY_ID_SALES_BOOKING, id_sales_booking)
        bundle.putParcelable("sale_booking", sale)
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.listener = {
            viewModel.getListDelivery()
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.getTag())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            showBottomSheetAddPayment(id_sales_booking)
        }
    }

    companion object {
        val TAG: String = DeliveryFragment::class.java.simpleName
        fun newInstance() =
            DeliveryFragment()
    }
}