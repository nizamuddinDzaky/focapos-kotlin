package id.sisi.postoko.view.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListDetailSalesBookingAdapter
import kotlinx.android.synthetic.main.detail_sales_booking_fragment.*

class DetailSalesBookingFragment : Fragment(){
    
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

        rv_list_product_sales_booking?.layoutManager = LinearLayoutManager(this.context)
        rv_list_product_sales_booking?.setHasFixedSize(false)
        rv_list_product_sales_booking?.adapter = ListDetailSalesBookingAdapter()
    }
}