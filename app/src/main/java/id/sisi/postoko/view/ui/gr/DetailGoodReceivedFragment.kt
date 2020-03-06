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
import id.sisi.postoko.databinding.DetailGoodReceivedFragmentBinding
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.toDisplayDateFromDO
import kotlinx.android.synthetic.main.detail_good_received_fragment.*

class DetailGoodReceivedFragment : Fragment() {
    companion object {
        fun newInstance() =
            DetailGoodReceivedFragment()
    }

    lateinit var viewModel: AddGoodReceivedViewModel
    lateinit var viewBinding: DetailGoodReceivedFragmentBinding
    private lateinit var adapter: ListDetailProductGoodReceivedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = getString(R.string.txt_title_detail_good_receive)

        val idGoodReceived = (activity as? DetailGoodReceivedActivity)?.mGoodReceived?.id ?: "0"
        viewModel = ViewModelProvider(this, AddGoodReceivedFactory(idGoodReceived.toInt())).get(
            AddGoodReceivedViewModel::class.java
        )
        viewBinding = DetailGoodReceivedFragmentBinding.inflate(inflater, container, false).apply {
            vm = viewModel

            lifecycleOwner = viewLifecycleOwner
        }

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewBinding.vm?.requestDetailGoodReceived()
    }

    private fun setupRecycleView() {
        adapter = ListDetailProductGoodReceivedAdapter()
        rv_list_product_detail_good_received?.layoutManager = LinearLayoutManager(this.context)
        rv_list_product_detail_good_received?.setHasFixedSize(false)
        rv_list_product_detail_good_received?.adapter = adapter
    }
}