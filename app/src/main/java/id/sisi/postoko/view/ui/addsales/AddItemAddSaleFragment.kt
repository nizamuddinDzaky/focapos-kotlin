package id.sisi.postoko.view.ui.addsales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemAddSaleAdapter
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.helper.findSaleFragmentByTag
import id.sisi.postoko.view.ui.product.ProductViewModel
import id.sisi.postoko.view.ui.sales.FragmentSearchCustomer
import id.sisi.postoko.view.ui.warehouse.WarehouseDialogFragment
import kotlinx.android.synthetic.main.add_item_add_sale_fragment.*


class AddItemAddSaleFragment: Fragment() {

    private var listProduct: List<Product> = arrayListOf()
    private lateinit var adapter: ListItemAddSaleAdapter
    private var expanded: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = null
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        return inflater.inflate(R.layout.add_item_add_sale_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AddSaleActivity?)?.currentFragmentTag = TAG

        if ((activity as AddSaleActivity?)?.idWarehouse != null){
            sp_warehouse.setText((activity as AddSaleActivity?)?.warehouseName)
        }

        if ((activity as AddSaleActivity?)?.idCustomer != null){
            sp_customer.setText((activity as AddSaleActivity?)?.customerName)
        }

        sp_warehouse.setOnClickListener {
            val dialogFragment = WarehouseDialogFragment()
            dialogFragment.listener={ warehouse ->
                (activity as AddSaleActivity?)?.idWarehouse = warehouse.id
                (activity as AddSaleActivity?)?.warehouseName = warehouse.name
                sp_warehouse.setText(warehouse.name)
            }
            dialogFragment.show(childFragmentManager, WarehouseDialogFragment().tag)
        }

        sp_customer.setOnClickListener {
            val dialogFragment = FragmentSearchCustomer()
            dialogFragment.listener={ customer ->
                (activity as AddSaleActivity?)?.idCustomer = customer.id
                (activity as AddSaleActivity?)?.customerName = customer.company
                sp_customer.setText(customer.company)
            }
            dialogFragment.show(childFragmentManager, FragmentSearchCustomer().tag)
        }

        (activity as AddSaleActivity?)?.listProdcut.let {
            if (it != null) {
                listProduct = it
                setupUI(it)
            }
        }

        if (!expanded){
            expandable_layout.collapse(true)
        }else{
            expandable_layout.expand(true)
        }

        layout_click_expandable.setOnClickListener {
            expanded = !expanded

            if (!expanded){
                expandable_layout.collapse(true)
            }else{
                expandable_layout.expand(true)
            }
        }

        toolbar.setNavigationOnClickListener {
            (activity as AddSaleActivity?)?.switchFragment(findSaleFragmentByTag(SelectCustomerFragment.TAG))
        }

        sv_item.setOnClickListener {
            sv_item?.onActionViewExpanded()
        }

        sv_item.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    startSearchData(newText)
                } else {
                    setupUI(listProduct)
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    private fun startSearchData(query: String) {
        listProduct.let {
            val listSearchResult = listProduct.filter {
                it.name.contains(query, true)
            }
            setupUI(listSearchResult)
        }
    }
    private fun setupUI(listProduct: List<Product>) {
        setupRecycleView(listProduct)
    }

    private fun setupRecycleView(listProduct: List<Product>) {
        adapter = ListItemAddSaleAdapter()
        adapter.updateData(listProduct)
        adapter.listener={product ->
            showPopUp(product)
        }
        rv_item_sale?.layoutManager = LinearLayoutManager(this.context)
        rv_item_sale?.setHasFixedSize(false)
        rv_item_sale?.adapter = adapter
    }

    private fun showPopUp(product: Product) {
        val dialogFragment = ItemAddSaleDialogFragment(product)
        dialogFragment.listener={
            val index = listProduct.indexOf(it)
            logE("saleItem = ${listProduct.get(index)}")
        }
        dialogFragment.show(childFragmentManager, ItemAddSaleDialogFragment(product).tag)
    }

    companion object {
        val TAG: String = AddItemAddSaleFragment::class.java.simpleName
        fun newInstance() =
            AddItemAddSaleFragment()
    }
}