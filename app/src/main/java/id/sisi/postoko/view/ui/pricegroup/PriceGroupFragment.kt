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
    private var listPriceGroup: List<PriceGroup>? = arrayListOf()

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



        mViewModel = ViewModelProvider(this).get(PriceGroupViewModel::class.java)
        mViewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        mViewModel.getListPriceGroups().observe(viewLifecycleOwner, Observer {
            listPriceGroup = it
            listPriceGroup?.let { it1 -> setupUI(it1) }
        })

        mViewModel.getListPriceGroup()

//        val bottomSheetFragment = BottomSheetEditPriceGroupFragment()
//        bottomSheetFragment.listener={
//            logE("masuk gak hayooo1")
//            mViewModel.getListPriceGroup()
//        }

        sv_master.setOnClickListener {
            sv_master?.onActionViewExpanded()
        }
        sv_master.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    startSearchData(newText)
                } else {
                    listPriceGroup?.let { setupUI(it) }
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

        fb_add_master.setOnClickListener {
            AddPriceGroupActivity.show(activity as FragmentActivity)
        }
    }

    private fun setupUI(listPriceGroup: List<PriceGroup>) {
        setupRecycleView(listPriceGroup)
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            mViewModel.getListPriceGroup()
        }
    }

    private fun startSearchData(query: String) {
        listPriceGroup?.let {
            val listSearchResult = listPriceGroup!!.filter {
                it.name!!.contains(query, true)
            }
            setupUI(listSearchResult)
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

    private fun setupRecycleView(listPriceGroup: List<PriceGroup>) {
        mAdapter = ListMasterAdapter(fragmentActivity = activity)
        mAdapter.updateMasterData(listPriceGroup)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = mAdapter
    }
}
