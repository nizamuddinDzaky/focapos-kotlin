package id.sisi.postoko.view.ui.goodreveived

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.adapter.toCurrencyID
import id.sisi.postoko.adapter.toNumberID
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.utils.extensions.format
import id.sisi.postoko.utils.extensions.tryMe
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_good_received.*


class BottomSheetAddGoodReceivedFragment : BottomSheetDialogFragment() {

    lateinit var viewModel: AddGoodReceivedViewModel
    var listener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add_good_received, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goodReceived = arguments?.getParcelable<GoodReceived>("good_received")

        goodReceived?.let {
            viewModel = ViewModelProvider(this, AddGoodReceivedFactory(it.id?.toInt() ?: 0)).get(
                AddGoodReceivedViewModel::class.java
            )
            viewModel.getDetailGoodReceived().observe(viewLifecycleOwner, Observer { gr ->
                gr?.let {
                    tv_detail_good_received_name?.text = gr.nama_produk
                    tryMe {
                        val price = (gr.grand_total?.toDouble() ?: 0.0).div(gr.qty_do?.toDouble() ?: 1.0)
                        tv_detail_good_received_price.text = price.toCurrencyID()
                        et_detail_good_received_new_price?.setText(price.format(0))
                    }
                    tv_detail_good_received_quantity?.text = gr.qty_do?.toDouble()?.toNumberID()
                    tv_detail_good_received_spj_no?.text = gr.no_spj
                    tv_detail_good_received_total?.text = gr.grand_total?.toDouble()?.toCurrencyID()
                }
            })
            viewModel.requestDetailGoodReceived()
        }

/*
        goodReceived?.let {
//            tv_detail_good_received_name?.text = it.
//            tv_detail_good_received_price.text = it.pri
            tv_detail_good_received_quantity?.text = it.qty_do
            tv_detail_good_received_spj_no?.text = it.no_spj
        }
*/

        btn_confirmation_good_recieved?.setOnClickListener {
            actionAddGoodReceived()
        }
    }

    private fun actionAddGoodReceived() {
//        val body = mutableMapOf(
//            "price" to (et_detail_good_received_new_price?.text?.toString() ?: "0")
//        )
//        viewModel.postAddGoodReceived(body) {
        viewModel.postAddGoodReceived {
            listener()
            this.dismiss()
        }
    }
}