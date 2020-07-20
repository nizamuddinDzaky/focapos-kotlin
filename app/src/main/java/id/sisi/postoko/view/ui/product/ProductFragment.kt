package id.sisi.postoko.view.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListMasterProdukAdapter
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.KEY_SALE_STATUS
import id.sisi.postoko.utils.MyPopupMenu
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.sales.DetailSalesBookingActivity
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.master_data_fragment.*

class ProductFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProductFragment()
    }

    private lateinit var viewModel: ProductViewModel
    private lateinit var vmWarehouse: WarehouseViewModel
    private lateinit var adapter: ListMasterProdukAdapter
    var listProdcut: List<Product>? = arrayListOf()
    val listAction: MutableList<() -> Unit?> = mutableListOf()
    val listMenu: MutableList<String> = mutableListOf()

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

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutMaster?.isRefreshing = it
        })
        viewModel.getListProducts().observe(viewLifecycleOwner, Observer {
            listProdcut=it
            listProdcut?.let { it1 -> setupUI(it1) }
            /**/
        })

        viewModel.getListProduct()

        vmWarehouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        vmWarehouse.getListWarehouses().observe(viewLifecycleOwner, Observer {
            listAction.clear()
            listMenu.clear()
            it?.forEach {wh ->
                listAction.add {
                    Toast.makeText(context, "id => ${wh.id}", Toast.LENGTH_SHORT).show()
                }

                listMenu.add(wh.name)
            }
        })

        iv_filter_warehouse.visible()
        iv_filter_warehouse.setOnClickListener {
            MyPopupMenu(
                it,
                listMenu,
                listAction,
                highlight = it
            ).show()
        }

        sv_master.setOnClickListener {
            sv_master?.onActionViewExpanded()
        }
        sv_master.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    startSearchData(newText)
                } else {
                    listProdcut?.let { setupUI(it) }
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    private fun startSearchData(query: String) {
        listProdcut?.let {
            val listSearchResult = listProdcut!!.filter {
                it.name?.contains(query, true)!!
            }
            setupUI(listSearchResult)
        }
    }

    private fun setupUI(listProdcut: List<Product>) {
        setupRecycleView(listProdcut)
        swipeRefreshLayoutMaster?.setOnRefreshListener {
            viewModel.getListProduct()
        }
    }

    private fun setupRecycleView(listProdcut: List<Product>) {
        adapter = ListMasterProdukAdapter()
        adapter.updateData(listProdcut)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapter
    }
}
