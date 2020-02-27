package id.sisi.postoko.view.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListDetailSalesBookingAdapter
import id.sisi.postoko.adapter.toCurrencyID
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.detail_sales_booking_fragment.*

class DetailSalesBookingFragment : Fragment(){

    private lateinit var viewModel: SaleBookingViewModel
    private lateinit var adapter: ListDetailSalesBookingAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detail_sales_booking_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id_sales_booking = (activity as? DetailSalesBookingActivity)?.id_sales_booking ?: 0
        logE("detail sales booking id $id_sales_booking")

        setupUI()

        viewModel = ViewModelProvider(this, SaleBookingFactory(id_sales_booking)).get(SaleBookingViewModel::class.java)
        viewModel.getDetail().observe(viewLifecycleOwner, Observer {
            logE("hasil get detail sale $it")
            setupDetailSale(it)
        })
    }

    private fun setupUI() {
        setupRecycleView()
    }

    private fun setupDetailSale(sale: Sales?){
        tv_sale_detail_reference_no?.text = sale?.reference_no
        tv_sale_detail_grand_total?.text = sale?.grand_total?.toCurrencyID()
        adapter.updateSaleItems(sale?.saleItems)
    }

    private fun setupRecycleView() {
        adapter = ListDetailSalesBookingAdapter()
        rv_list_product_sales_booking?.layoutManager = LinearLayoutManager(this.context)
        rv_list_product_sales_booking?.setHasFixedSize(false)
        rv_list_product_sales_booking?.adapter = adapter
    }
}