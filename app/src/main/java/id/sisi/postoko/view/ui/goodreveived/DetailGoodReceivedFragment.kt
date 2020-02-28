package id.sisi.postoko.view.ui.goodreveived

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
import id.sisi.postoko.adapter.toDisplayDateFromDO
import id.sisi.postoko.utils.extensions.toCurrencyID
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
            viewModel = ViewModelProvider(this, AddGoodReceivedFactory(it.toInt())).get(AddGoodReceivedViewModel::class.java)
            viewModel.getDetailGoodReceived().observe(viewLifecycleOwner, Observer {
                tv_good_received_detail_from_name?.text = it?.company_name
                tv_good_received_detail_from_address_1?.text = it?.company_name
//                val distributorName = "${it?.distributor} (${it?.nama_shipto})"
                tv_good_received_detail_to_name?.text = it?.distributor
                tv_good_received_detail_to_address_1?.text = it?.alamat_shipto
                val distributorName = "${it?.nama_kecamatan} (${it?.nama_shipto})"
                tv_good_received_detail_to_address_2?.text = distributorName
                tv_good_received_detail_to_name?.text = it?.distributor
                tv_good_received_detail_do_number?.text = it?.no_do
                tv_good_received_detail_grand_total?.text = it?.grand_total?.toCurrencyID()
                tv_good_received_pp_number?.text = it?.no_pp
                tv_good_received_pp_date?.text = it?.tanggal_pp?.toDisplayDateFromDO()
                tv_good_received_so_number?.text = it?.no_so
                tv_good_received_so_date?.text = it?.tanggal_so?.toDisplayDateFromDO()
                tv_good_received_transaction_number?.text = it?.no_transaksi
                tv_good_received_order_type?.text = it?.tipe_order
                tv_good_received_spj_number?.text = it?.no_spj
                tv_good_received_spj_date?.text = it?.tanggal_spj?.toDisplayDateFromDO()
                tv_good_received_police_number?.text = it?.no_polisi
                tv_good_received_driver_name?.text = it?.nama_sopir
                tv_good_received_plant_name?.text = it?.nama_plant
                tv_good_received_plant_number?.text = it?.kode_plant

                adapter.updatePurchasesItem(it?.good_received_items)
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