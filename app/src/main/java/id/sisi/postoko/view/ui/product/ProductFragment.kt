package id.sisi.postoko.view.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListMasterProdukAdapter
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.master_data_fragment.*

class ProductFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProductFragment()
    }

    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ListMasterProdukAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.master_data_fragment, container, false)
    }

    override var tagName: String
        get() = "Produk"
        set(_) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        viewModel.getListProducts().observe(viewLifecycleOwner, Observer {
            adapter.updateData(it)
        })
    }

    private fun setupUI() {
        setupRecycleView()
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            viewModel.getListProduct()
        }
    }

    private fun setupRecycleView() {
        adapter = ListMasterProdukAdapter()
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapter
    }
}
