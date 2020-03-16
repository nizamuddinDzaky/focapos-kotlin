package id.sisi.postoko.view.ui.gr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListDetailProductGoodReceivedAdapter
import id.sisi.postoko.databinding.DetailGoodReceivedFragmentBinding

class DetailGoodReceivedFragment : Fragment() {
    companion object {
        fun newInstance() =
            DetailGoodReceivedFragment()
    }

    lateinit var viewModel: AddGoodReceivedViewModel
    private lateinit var viewBinding: DetailGoodReceivedFragmentBinding
    private var mAdapter = ListDetailProductGoodReceivedAdapter()

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
            vModel = viewModel
            adapter = mAdapter
            fragmentManager = childFragmentManager

            lifecycleOwner = viewLifecycleOwner
        }
        viewModel.getDetailGoodReceived().observe(viewLifecycleOwner, Observer {
            mAdapter.updatePurchasesItem(it?.goodReceivedItems)
        })

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewBinding.vModel?.requestDetailGoodReceived()
    }
}