package id.sisi.postoko.view.ui.gr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListDetailProductGoodReceivedAdapter
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toDisplayDateFromDO
import kotlinx.android.synthetic.main.detail_good_received_fragment.*

class DetailGoodReceivedFragment : Fragment() {
    companion object {
        fun newInstance() =
            DetailGoodReceivedFragment()
    }

    lateinit var viewModel: AddGoodReceivedViewModel
    private lateinit var adapter: ListDetailProductGoodReceivedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = getString(R.string.txt_title_detail_good_receive)
        return inflater.inflate(R.layout.detail_good_received_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idGoodReceived = (activity as? DetailGoodReceivedActivity)?.mGoodReceived?.id

        setupUI()

        idGoodReceived?.let {
            viewModel = ViewModelProvider(this, AddGoodReceivedFactory(it.toInt())).get(
                AddGoodReceivedViewModel::class.java
            )
            viewModel.getDetailGoodReceived().observe(viewLifecycleOwner, Observer { gr ->
                tv_good_received_detail_from_name?.text = gr?.company_name
                tv_good_received_detail_from_address_1?.text = gr?.company_name
                tv_good_received_detail_to_name?.text = gr?.distributor
                tv_good_received_detail_to_address_1?.text = gr?.alamat_shipto
                val distributorName = "${gr?.nama_kecamatan} (${gr?.nama_shipto})"
                tv_good_received_detail_to_address_2?.text = distributorName
                tv_good_received_detail_to_name?.text = gr?.distributor
                tv_good_received_detail_do_number?.text = gr?.no_do
                tv_good_received_detail_grand_total?.text = gr?.grand_total?.toCurrencyID()
                tv_good_received_pp_number?.text = gr?.no_pp
                tv_good_received_pp_date?.text = gr?.tanggal_pp?.toDisplayDateFromDO()
                tv_good_received_so_number?.text = gr?.no_so
                tv_good_received_so_date?.text = gr?.tanggal_so?.toDisplayDateFromDO()
                tv_good_received_transaction_number?.text = gr?.no_transaksi
                tv_good_received_order_type?.text = gr?.tipe_order
                tv_good_received_spj_number?.text = gr?.no_spj
                tv_good_received_spj_date?.text = gr?.tanggal_spj?.toDisplayDateFromDO()
                tv_good_received_police_number?.text = gr?.no_polisi
                tv_good_received_driver_name?.text = gr?.nama_sopir
                tv_good_received_plant_name?.text = gr?.nama_plant
                tv_good_received_plant_number?.text = gr?.kode_plant

                adapter.updatePurchasesItem(gr?.goodReceivedItems)
            })
            viewModel.requestDetailGoodReceived()
        }
    }

    private fun setupUI() {
        setupRecycleView()
    }

    private fun setupRecycleView() {
        adapter = ListDetailProductGoodReceivedAdapter()
        rv_list_product_detail_good_received?.layoutManager = LinearLayoutManager(this.context)
        rv_list_product_detail_good_received?.setHasFixedSize(false)
        rv_list_product_detail_good_received?.adapter = adapter
    }
}