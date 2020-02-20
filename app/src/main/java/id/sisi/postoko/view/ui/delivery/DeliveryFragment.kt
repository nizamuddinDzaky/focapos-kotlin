package id.sisi.postoko.view.ui.delivery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListPengirimanAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.AddProductActivity
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.pengiriman_fragment.*

class DeliveryFragment : Fragment(){
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

        val id_sales_booking = (activity as? DetailSalesBookingActivity)?.id_sales_booking
        logE("delivery sales booking id $id_sales_booking")

        rv_list_item_pengiriman?.layoutManager = LinearLayoutManager(this.context)
        rv_list_item_pengiriman?.setHasFixedSize(false)
        rv_list_item_pengiriman?.adapter = ListPengirimanAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            startActivity(Intent(this.context, AddProductActivity::class.java))
        }
    }

    companion object {
        val TAG: String = DeliveryFragment::class.java.simpleName
        fun newInstance() =
            DeliveryFragment()
    }
}