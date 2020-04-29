package id.sisi.postoko.view.ui.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListPengirimanAdapter
import id.sisi.postoko.model.Delivery
import id.sisi.postoko.model.SaleItem
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_ID_DELIVERY
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import id.sisi.postoko.view.ui.sales.SaleStatus
import kotlinx.android.synthetic.main.failed_load_data.*
import kotlinx.android.synthetic.main.pengiriman_fragment.*
import java.util.*

class DeliveryFragment : Fragment() {

    private lateinit var viewModel: DeliveryViewModel
    private lateinit var adapter: ListPengirimanAdapter
    private var sale: Sales? = null
    private var listSaleItems  = ArrayList<SaleItem>()
    private var idSalesBooking = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pengiriman_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idSalesBooking = (activity as? DetailSalesBookingActivity)?.idSalesBooking ?: 0
        sale = (activity as? DetailSalesBookingActivity)?.tempSale
        setupUI()

        listSaleItems = arrayListOf()
        for (x in 0 until sale?.saleItems?.size!!){
            if (sale?.saleItems!![x].quantity!! > sale?.saleItems!![x].sent_quantity){
                sale?.saleItems?.get(x)?.let { listSaleItems.add(it) }
            }
        }
        sale?.saleItems = listSaleItems
        if (sale?.saleItems?.size!! < 1){
                fb_add_transaction.gone()
            }else{
                fb_add_transaction.visible()
            }
        viewModel = ViewModelProvider(
            this,
            DeliveryFactory(idSalesBooking)
        ).get(DeliveryViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutDeliverySale?.isRefreshing = it
        })
        viewModel.getListDeliveries().observe(viewLifecycleOwner, Observer {
            adapter.updateData(it)
            if (it?.size ?: 0 == 0) {
                layout_status_progress?.visible()
                rv_list_item_pengiriman?.gone()
                val status = when(it?.size) {
                    0 -> "Belum ada pengiriman"
                    else -> "Gagal Memuat Data"
                }
                tv_status_progress?.text = status
            } else {
                layout_status_progress?.gone()
                rv_list_item_pengiriman?.visible()
            }
        })
    }

    private fun setupUI() {
        setupRecycleView()
        swipeRefreshLayoutDeliverySale?.setOnRefreshListener {
            viewModel.getListDelivery()
        }
    }

    private fun setupRecycleView() {
        adapter = ListPengirimanAdapter {
            //TODO detail delivery

            showBottomSheetDetailDelivery(it)
        }
        rv_list_item_pengiriman?.layoutManager = LinearLayoutManager(this.context)
        rv_list_item_pengiriman?.setHasFixedSize(false)
        rv_list_item_pengiriman?.adapter = adapter
        rv_list_item_pengiriman?.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun showBottomSheetDetailDelivery(delivery: Delivery?) {
        val bottomSheetFragment = BottomSheetDetailDeliveryFragment()
        val bundle = Bundle()
        bundle.putString(KEY_ID_DELIVERY, delivery?.id)
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun showBottomSheetAddDelivery(id_sales_booking: Int) {

        val bottomSheetFragment = BottomSheetAddDeliveryFragment()
        val bundle = Bundle()
        bundle.putInt(KEY_ID_SALES_BOOKING, id_sales_booking)
        bundle.putParcelable("sale_booking", sale)
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.listener = {
            viewModel.getListDelivery()
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
//
            if (sale?.sale_status == SaleStatus.RESERVED.toString().toLowerCase(Locale.ROOT)) {
                showBottomSheetAddDelivery(idSalesBooking)
            }else{
                Toast.makeText(context, "Maaf Sale Belum Disetujui", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        val TAG: String = DeliveryFragment::class.java.simpleName
        fun newInstance() =
            DeliveryFragment()
    }
}