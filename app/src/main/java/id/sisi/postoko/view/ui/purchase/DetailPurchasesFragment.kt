package id.sisi.postoko.view.ui.purchase

import android.content.Intent
import android.os.Bundle
import android.view.*
import id.sisi.postoko.R
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.adapter.ListDetailPurchaseAdapter
import id.sisi.postoko.model.Purchases
import id.sisi.postoko.utils.KEY_MESSAGE
import id.sisi.postoko.utils.KEY_SALE
import id.sisi.postoko.utils.KEY_SALE_ITEM
import id.sisi.postoko.utils.KEY_VALIDATION_REST
import id.sisi.postoko.utils.extensions.goneIfEmptyOrNull
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toDisplayDateTime
import id.sisi.postoko.view.ui.sales.EditSaleActivity
import kotlinx.android.synthetic.main.detail_purchase_fragment.*
import kotlinx.android.synthetic.main.detail_purchase_fragment.rv_list_product_purchase
import kotlinx.android.synthetic.main.detail_purchase_fragment.swipeRefreshLayoutDetailSales
import java.util.ArrayList

class DetailPurchasesFragment: BaseFragment() {


    private lateinit var adapter: ListDetailPurchaseAdapter
    var purchase: Purchases? = null
    override var tagName: String
        get() = "Detail Pembelian"
        set(_) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_purchase_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        (activity as DetailPurchaseActivity).vmPurchase.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutDetailSales?.isRefreshing = it
        })

        (activity as DetailPurchaseActivity).vmPurchase.getDetailPurchase().observe(viewLifecycleOwner, Observer {
            setupDetailSale(it)
            it?.let {
                purchase = it
                (activity as DetailPurchaseActivity).invalidateOptionsMenu()
                (activity as? DetailPurchaseActivity)?.purchaseStatus = it.status
                (activity as DetailPurchaseActivity).vmPurchase.requestDetailCustomer(it.company_id ?: 0)
                (activity as DetailPurchaseActivity).vmPurchase.requestDetailSupplier(it.supplier_id ?: 0)
            }
        })

        (activity as DetailPurchaseActivity).vmPurchase.getDetailCustomer().observe(viewLifecycleOwner, Observer {
            val address = listOf(it?.region, it?.state, it?.country)
            (activity as? DetailSalesBookingActivity)?.tempCustomer = it
            tv_detail_purchase_customer_name?.text = it?.name
            tv_detail_purchase_customer_address_1?.text = it?.address
            tv_detail_purchase_customer_address_2?.text = address.filterNotNull().joinToString()
            tv_detail_purchase_customer_name?.goneIfEmptyOrNull()
            tv_detail_purchase_customer_address_1?.goneIfEmptyOrNull()
            tv_detail_purchase_customer_address_2?.goneIfEmptyOrNull()
        })

        (activity as DetailPurchaseActivity).vmPurchase.getDetailSupplier().observe(viewLifecycleOwner, Observer {
            tv_detail_purchase_supplier_name?.text = it?.name
            tv_detail_purchase_supplier_address_1?.text = it?.address
            tv_detail_purchase_supplier_address_2?.text = it?.state
            tv_detail_purchase_supplier_name?.goneIfEmptyOrNull()
            tv_detail_purchase_supplier_address_1?.goneIfEmptyOrNull()
            tv_detail_purchase_supplier_address_2?.goneIfEmptyOrNull()
        })

        (activity as DetailPurchaseActivity).vmPurchase.requestDetailPurchase((activity as DetailPurchaseActivity).idPurchase)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit_sale -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupDetailSale(purchase: Purchases?) {
        (activity as? DetailPurchaseActivity)?.tempPurchase = purchase
        tv_purchase_detail_reference_no.text = purchase?.reference_no
        tv_purchase_detail_purchase_date.text = purchase?.date?.toDisplayDateTime()
        tv_purchase_detail_purchase_sale_status.text = purchase?.status
        tv_purchase_detail_purchase_payment_status.text = purchase?.payment_status
        tv_purchase_detail_purchase_discount.text = purchase?.order_discount?.toCurrencyID()
        tv_purchase_detail_purchase_total.text = purchase?.grand_total?.toCurrencyID()
        tv_purchase_detail_purchase_paid.text = purchase?.paid?.toCurrencyID()
        tv_purchase_detail_purchase_balance.text = (purchase?.grand_total?.minus(purchase.paid ?: 0.0))?.toCurrencyID()
        tv_purchase_detail_purchase_note.text = purchase?.note
        tv_purchase_detail_purchase_created_date.text = purchase?.created_at?.toDisplayDateTime()
        tv_purchase_detail_purchase_created_by.text = purchase?.created_by
        adapter.updateSaleItems(purchase?.purchaseItems)
    }

    private fun setupUI() {
        setupRecycleView()
        swipeRefreshLayoutDetailSales?.setOnRefreshListener {
            (activity as DetailPurchaseActivity).vmPurchase.requestDetailPurchase((activity as DetailPurchaseActivity).idPurchase)
        }
    }

    private fun setupRecycleView() {
        adapter = ListDetailPurchaseAdapter()
        rv_list_product_purchase?.layoutManager = LinearLayoutManager(this.context)
        rv_list_product_purchase?.setHasFixedSize(false)
        rv_list_product_purchase?.adapter = adapter

    }

    companion object {
        const val TAG: String = ""
        fun newInstance() =
            DetailPurchasesFragment()
    }
}