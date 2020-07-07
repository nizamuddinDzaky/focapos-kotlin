package id.sisi.postoko.view.ui.sales

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListDetailSalesBookingAdapter
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.delivery.DeliveryStatus
import kotlinx.android.synthetic.main.detail_sales_booking_fragment.*
import java.util.*


class DetailSalesBookingFragment : Fragment() {

    private lateinit var adapter: ListDetailSalesBookingAdapter
    private lateinit var viewModel: AddSalesViewModel
    private val progressBar = CustomProgressBar()
    var sale: Sales? = null
    private var myDialog = MyDialog()

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

        viewModel = ViewModelProvider(
            this
        ).get(AddSalesViewModel::class.java)

        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it) {
                context?.let { it1 -> progressBar.show(it1, getString(R.string.txt_please_wait)) }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        /*VersionHelper.refreshActionBarMenu(this)*/

        viewModel.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

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
                sale = it
                (activity as DetailSalesBookingActivity).invalidateOptionsMenu()
                (activity as? DetailSalesBookingActivity)?.saleStatus = it.sale_status
                (activity as DetailSalesBookingActivity).vmSale.requestDetailCustomer(it.customer_id ?: 0)
                (activity as DetailSalesBookingActivity).vmSale.requestDetailWarehouse(it.warehouse_id ?: 0)
                (activity as DetailSalesBookingActivity).vmSale.requestDetailSupplier(it.biller_id ?: 0)
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

    override fun onPrepareOptionsMenu(menu: Menu) {
        val saleStatus= (activity as? DetailSalesBookingActivity)?.saleStatus ?: ""
        if (saleStatus == SaleStatus.RESERVED.name.toLowerCase(
                Locale.ROOT)
        ){
            menu.findItem(R.id.menu_close_sale).isVisible = true
        }
        super.onPrepareOptionsMenu(menu)
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

            tv_sale_detail_sbo_delivery_status?.text =
                it.delivery_status?.toDisplayStatus()?.let { str -> getString(str) }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (sale?.delivery_status != null){
                context?.getColor(sale.delivery_status?.toDisplayStatusColor() ?: 0)?.let {
                    tv_sale_detail_sbo_delivery_status.setTextColor(
                        it
                    )
                }
            }

            context?.getColor(sale?.sale_status?.toDisplayStatusColor() ?: 0)?.let {
                tv_sale_detail_sbo_sale_status.setTextColor(
                    it
                )
            }

        }else{
            if (sale?.delivery_status != null){
                tv_sale_detail_sbo_delivery_status.setTextColor(ResourcesCompat.getColor(context?.resources!!, sale?.delivery_status?.toDisplayStatusColor() ?: 0, null))
            }
            tv_sale_detail_sbo_sale_status.setTextColor(ResourcesCompat.getColor(context?.resources!!, sale?.sale_status?.toDisplayStatusColor() ?: 0, null))
        }

        /*tv_sale_detail_sbo_delivery_status?.text = sale*/
        tv_sale_detail_sbo_discount?.text = sale?.total_discount?.toCurrencyID()
        tv_sale_detail_sbo_total?.text = sale?.total?.toCurrencyID()
        tv_sale_detail_sbo_paid?.text = sale?.paid?.toCurrencyID()
        tv_payment_fee?.text = sale?.shipping?.toCurrencyID()
        tv_sale_detail_grand_total?.text = (sale?.grand_total?.minus(sale.paid ?: 0.0))?.toCurrencyID()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit -> {
                val result = validationActionEditSale()
                if (!(result?.get(KEY_VALIDATION_REST) as Boolean)) {
                    myDialog.alert(result[KEY_MESSAGE] as String, context)
                } else {
                    val intent = Intent((activity as DetailSalesBookingActivity), EditSaleActivity::class.java)
                    intent.putExtra(KEY_SALE, sale)
                    intent.putParcelableArrayListExtra(
                        KEY_SALE_ITEM,
                        sale?.saleItems?.let { ArrayList(it) })
                    startActivityForResult(intent, 1)
                }
                true
            }
            R.id.menu_close_sale -> {
                viewModel.postCloseSale(sale?.id ?: 0) {
                    (activity as DetailSalesBookingActivity).vmSale.requestDetailSale()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validationActionEditSale(): Map<String, Any>? {
        var message = ""
        logE("${sale?.delivery_status}")
        var cek = true
        if (sale?.sale_status == SaleStatus.values()[2].name.toLowerCase(Locale.getDefault())) {
            message += "- ${getString(R.string.txt_sale_reserved)}\n"
            cek = false
            return mapOf(KEY_MESSAGE to message, KEY_VALIDATION_REST to cek)
        }
        if (sale?.delivery_status?.toLowerCase(Locale.ROOT) != DeliveryStatus.PENDING.toString()
                .toLowerCase(
                    Locale.ROOT
                )
        ) {
            message += "- ${getString(R.string.txt_alert_sale_has_delivery)}\n"
            cek = false
            return mapOf(KEY_MESSAGE to message, KEY_VALIDATION_REST to cek)
        }
        return mapOf(KEY_MESSAGE to message, KEY_VALIDATION_REST to cek)
    }
    companion object {
        const val TAG: String = ""
        fun newInstance() =
            DetailSalesBookingRootFragment()
    }
}