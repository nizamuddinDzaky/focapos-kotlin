package id.sisi.postoko.view.ui.sales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListDetailSalesBookingAdapter
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.extensions.*
import kotlinx.android.synthetic.main.detail_sales_booking_fragment.*

class DetailSalesBookingFragment : Fragment() {

    private lateinit var viewModel: SaleBookingViewModel
    private lateinit var adapter: ListDetailSalesBookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_sales_booking_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idSalesBooking = (activity as? DetailSalesBookingActivity)?.idSalesBooking ?: 0

        setupUI()

        viewModel = ViewModelProvider(
            this,
            SaleBookingFactory(idSalesBooking)
        ).get(SaleBookingViewModel::class.java)

        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutDetailSales?.isRefreshing = it
        })

        viewModel.getDetailSale().observe(viewLifecycleOwner, Observer {
            setupDetailSale(it)
            it?.let {
                viewModel.requestDetailCustomer(it.customer_id)
                viewModel.requestDetailWarehouse(it.warehouse_id)
                viewModel.requestDetailSupplier(it.biller_id)
            }
        })
        viewModel.getDetailCustomer().observe(viewLifecycleOwner, Observer {
            val address = listOf(it?.region, it?.state, it?.country)
            tv_detail_sbo_customer_name?.text = it?.name
            tv_detail_sbo_customer_address_1?.text = it?.address
            tv_detail_sbo_customer_address_2?.text = address.filterNotNull().joinToString()
            tv_detail_sbo_customer_name?.goneIfEmptyOrNull()
            tv_detail_sbo_customer_address_1?.goneIfEmptyOrNull()
            tv_detail_sbo_customer_address_2?.goneIfEmptyOrNull()
        })
        viewModel.getDetailWarehouse().observe(viewLifecycleOwner, Observer {
            tv_detail_sbo_warehouse_name?.text = it?.name
            tv_detail_sbo_warehouse_address_1?.text = it?.address
//            tv_detail_sbo_warehouse_address_2?.text = it?.address
            tv_detail_sbo_warehouse_name?.goneIfEmptyOrNull()
            tv_detail_sbo_warehouse_address_1?.goneIfEmptyOrNull()
            tv_detail_sbo_warehouse_address_2?.goneIfEmptyOrNull()
        })
        viewModel.getDetailSupplier().observe(viewLifecycleOwner, Observer {
            tv_detail_sbo_supplier_name?.text = it?.name
            tv_detail_sbo_supplier_address_1?.text = it?.address
            tv_detail_sbo_supplier_address_2?.text = it?.state
            tv_detail_sbo_supplier_name?.goneIfEmptyOrNull()
            tv_detail_sbo_supplier_address_1?.goneIfEmptyOrNull()
            tv_detail_sbo_supplier_address_2?.goneIfEmptyOrNull()
        })
        viewModel.requestDetailSale()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_sale, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupUI() {
        setupRecycleView()
        swipeRefreshLayoutDetailSales?.setOnRefreshListener {
            viewModel.requestDetailSale()
        }
    }

    private fun setupDetailSale(sale: Sales?) {
        (activity as? DetailSalesBookingActivity)?.tempSale = sale
        tv_sale_detail_reference_no?.text = sale?.reference_no
        tv_sale_detail_sbo_date?.text = sale?.date?.toDisplayDate()
        sale?.let {
            tv_sale_detail_sbo_sale_status?.text =
                getString(SaleStatus.PENDING.tryValue(sale.sale_status)?.stringId ?: R.string.empty)
        }
        tv_sale_detail_sbo_delivery_status?.text = sale?.delivery_status
        tv_sale_detail_sbo_discount?.text = sale?.total_discount?.toCurrencyID()
        tv_sale_detail_sbo_total?.text = sale?.total?.toCurrencyID()
        tv_sale_detail_sbo_paid?.text = sale?.paid?.toCurrencyID()
        tv_sale_detail_grand_total?.text = (sale?.grand_total?.minus(sale.paid))?.toCurrencyID()
        adapter.updateSaleItems(sale?.saleItems)
    }

    private fun setupRecycleView() {
        adapter = ListDetailSalesBookingAdapter()
        rv_list_product_sales_booking?.layoutManager = LinearLayoutManager(this.context)
        rv_list_product_sales_booking?.setHasFixedSize(false)
        rv_list_product_sales_booking?.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (::viewModel.isInitialized) {
                viewModel.requestDetailSale()
            }
        }
    }
    companion object {
        const val TAG: String = ""
        fun newInstance() =
            DetailSalesBookingRootFragment()
    }
}