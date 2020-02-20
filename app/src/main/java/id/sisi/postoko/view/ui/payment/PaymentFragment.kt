package id.sisi.postoko.view.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListPembayaranAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.AddProductActivity
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import kotlinx.android.synthetic.main.pembayaran_fragment.*
import kotlinx.android.synthetic.main.pembayaran_fragment.fb_add_transaction

class PaymentFragment : Fragment(){

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

        val id_sales_booking = (activity as? DetailSalesBookingActivity)?.id_sales_booking
        logE("payment sales booking id $id_sales_booking")

        rv_list_item_pembayaran?.layoutManager = LinearLayoutManager(this.context)
        rv_list_item_pembayaran?.setHasFixedSize(false)
        rv_list_item_pembayaran?.adapter = ListPembayaranAdapter()
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