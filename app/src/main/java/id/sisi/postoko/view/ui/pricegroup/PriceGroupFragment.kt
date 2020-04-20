package id.sisi.postoko.view.ui.pricegroup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListMasterAdapter
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.master_data_fragment.*

class PriceGroupFragment : BaseFragment() {

    companion object {
        fun newInstance() = PriceGroupFragment()
    }

    private lateinit var mViewModel: PriceGroupViewModel
    private lateinit var mAdapter: ListMasterAdapter<PriceGroup>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.master_data_fragment, container, false)
    }

    override var tagName: String
        get() = "Kelompok Harga"
        set(_) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()

        mViewModel = ViewModelProvider(this).get(PriceGroupViewModel::class.java)
        mViewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        mViewModel.getListPriceGroups().observe(viewLifecycleOwner, Observer {
            logE("masuk gak hayooo2")
            mAdapter.updateMasterData(it)
        })

        mViewModel.getListPriceGroup()

//        val bottomSheetFragment = BottomSheetEditPriceGroupFragment()
//        bottomSheetFragment.listener={
//            logE("masuk gak hayooo1")
//            mViewModel.getListPriceGroup()
//        }

        fb_add_master.setOnClickListener {
            AddPriceGroupActivity.show(activity as FragmentActivity)
        }
    }

    private fun setupUI() {
        setupRecycleView()
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            mViewModel.getListPriceGroup()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        logE("masuk ke price group")
        if (resultCode == Activity.RESULT_OK) {
            if (::mViewModel.isInitialized) {
                mViewModel.getListPriceGroup()
            }
        }
    }

    private fun setupRecycleView() {
        mAdapter = ListMasterAdapter(fragmentActivity = activity)

        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = mAdapter
    }
}
