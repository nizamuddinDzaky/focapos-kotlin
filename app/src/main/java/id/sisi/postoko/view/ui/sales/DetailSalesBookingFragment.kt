package id.sisi.postoko.view.ui.sales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListDetailSalesBookingAdapter
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.extensions.*
import kotlinx.android.synthetic.main.detail_sales_booking_fragment.*


class DetailSalesBookingFragment : Fragment() {

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

        setupUI()

        tv_copy.setOnClickListener {
            tv_sale_detail_reference_no.text.toString().copyText(activity)
        }


        (activity as DetailSalesBookingActivity).vmSale.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutDetailSales?.isRefreshing = it
        })

        (activity as DetailSalesBookingActivity).vmSale.getDetailSale().observe(viewLifecycleOwner, Observer {
            setupDetailSale(it)
            it?.let {
                (activity as DetailSalesBookingActivity).vmSale.requestDetailCustomer(it.customer_id)
                (activity as DetailSalesBookingActivity).vmSale.requestDetailWarehouse(it.warehouse_id)
                (activity as DetailSalesBookingActivity).vmSale.requestDetailSupplier(it.biller_id)
            }
        })
        (activity as DetailSalesBookingActivity).vmSale.getDetailCustomer().observe(viewLifecycleOwner, Observer {
            val address = listOf(it?.region, it?.state, it?.country)
            (activity as? DetailSalesBookingActivity)?.tempCustomer = it
            tv_detail_sbo_customer_name?.text = it?.name
            tv_detail_sbo_customer_address_1?.text = it?.address
            tv_detail_sbo_customer_address_2?.text = address.filterNotNull().joinToString()
            tv_detail_sbo_customer_name?.goneIfEmptyOrNull()
            tv_detail_sbo_customer_address_1?.goneIfEmptyOrNull()
            tv_detail_sbo_customer_address_2?.goneIfEmptyOrNull()
        })
        (activity as DetailSalesBookingActivity).vmSale.getDetailWarehouse().observe(viewLifecycleOwner, Observer {
            tv_detail_sbo_warehouse_name?.text = it?.name
            tv_detail_sbo_warehouse_address_1?.text = it?.address
            tv_detail_sbo_warehouse_name?.goneIfEmptyOrNull()
            tv_detail_sbo_warehouse_address_1?.goneIfEmptyOrNull()
            tv_detail_sbo_warehouse_address_2?.goneIfEmptyOrNull()
        })
        (activity as DetailSalesBookingActivity).vmSale.getDetailSupplier().observe(viewLifecycleOwner, Observer {
            tv_detail_sbo_supplier_name?.text = it?.name
            tv_detail_sbo_supplier_address_1?.text = it?.address
            tv_detail_sbo_supplier_address_2?.text = it?.state
            tv_detail_sbo_supplier_name?.goneIfEmptyOrNull()
            tv_detail_sbo_supplier_address_1?.goneIfEmptyOrNull()
            tv_detail_sbo_supplier_address_2?.goneIfEmptyOrNull()
        })
        (activity as DetailSalesBookingActivity).vmSale.requestDetailSale()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_sale, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupUI() {
        setupRecycleView()
        swipeRefreshLayoutDetailSales?.setOnRefreshListener {
            (activity as DetailSalesBookingActivity).vmSale.requestDetailSale()
        }
    }

    private fun setupDetailSale(sale: Sales?) {
        (activity as? DetailSalesBookingActivity)?.tempSale = sale
        tv_sale_detail_reference_no?.text = sale?.reference_no
        tv_sale_detail_sbo_date?.text = sale?.date?.toDisplayDateTime()
        sale?.let {
            tv_sale_detail_sbo_sale_status?.text =
                getString(SaleStatus.PENDING.tryValue(sale.sale_status)?.stringId ?: R.string.empty)
        }
        tv_sale_detail_sbo_delivery_status?.text = sale?.delivery_status
        tv_sale_detail_sbo_discount?.text = sale?.total_discount?.toCurrencyID()
        tv_sale_detail_sbo_total?.text = sale?.total?.toCurrencyID()
        tv_sale_detail_sbo_paid?.text = sale?.paid?.toCurrencyID()
        tv_sale_detail_grand_total?.text = (sale?.grand_total?.minus(sale.paid))?.toCurrencyID()
        tv_employee_note?.text = sale?.staff_note
        tv_sale_note?.text = sale?.note
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
            /*if (::viewModel.isInitialized) {*/
                (activity as DetailSalesBookingActivity).vmSale.requestDetailSale()
            /*}*/
        }
    }
    companion object {
        const val TAG: String = ""
        fun newInstance() =
            DetailSalesBookingRootFragment()
    }
}