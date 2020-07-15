package id.sisi.postoko.view.ui.addsales

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.utils.helper.findSaleFragmentByTag
import id.sisi.postoko.view.ui.sales.FragmentSearchCustomer
import id.sisi.postoko.view.ui.warehouse.WarehouseDialogFragment
import kotlinx.android.synthetic.main.add_item_add_sale_fragment.*

class AddItemAddSaleFragment: Fragment() {

    private var listProduct: List<Product> = arrayListOf()
    private var expanded: Boolean = true
    private val myDialog = MyDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = null
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

        (activity as AddSaleActivity?)?.listProduct.let {
            if (it != null) {
                listProduct = it
                setupUI(it)
            }
        }

        if (!expanded){
            iv_arrow_up.visible()
            iv_arrow_down.gone()
            expandable_layout.collapse(true)
        }else{
            iv_arrow_up.gone()
            iv_arrow_down.visible()
            expandable_layout.expand(true)
        }

        layout_click_expandable.setOnClickListener {
            expanded = !expanded

            if (!expanded){
                iv_arrow_up.visible()
                iv_arrow_down.gone()
                expandable_layout.collapse(true)
            }else{
                iv_arrow_up.gone()
                iv_arrow_down.visible()
                expandable_layout.expand(true)
            }
        }

        setUpTotal()

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

        btn_action_submit.setOnClickListener {
            if (((activity as AddSaleActivity?)?.countItemSelected() ?: 0) > 0)
                (activity as AddSaleActivity?)?.switchFragment(findSaleFragmentByTag(PaymentAddSaleFragment.TAG))
            else
                myDialog.alert(getString(R.string.txt_alert_id_warehouse), context)
        }
    }

    private fun setUpTotal(){
        val total = (activity as AddSaleActivity?)?.getTotal()
        val disc = (activity as AddSaleActivity?)?.getDiscount()
        tv_total_add_sale.text = total?.toCurrencyID()
        tv_total_diskon.text = disc?.toCurrencyID()
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
        val adapter = (activity as AddSaleActivity).adapter
        adapter.updateData(listProduct)
        adapter.listener={product ->
            showPopUp(product)
        }
        rv_item_sale?.layoutManager = LinearLayoutManager(this.context)
        rv_item_sale?.setHasFixedSize(false)
        rv_item_sale?.adapter = adapter
    }

    private fun showPopUp(product: Product) {
        val dialogFragment = AddItemSaleDialogFragment(product)
        dialogFragment.listenerAdd={
            (activity as AddSaleActivity?)?.setUpBadge()
            setUpTotal()
        }
        dialogFragment.listenerRemove = {
            (activity as AddSaleActivity?)?.setUpBadge()
            setUpTotal()
        }
        dialogFragment.show(childFragmentManager, AddItemSaleDialogFragment(product).tag)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cart, menu)
        val actionView = menu.findItem(R.id.action_cart)?.actionView
        val total = (activity as? AddSaleActivity)?.countItemSelected()
        actionView?.rootView?.findViewById<TextView>(R.id.cart_badge)?.text = total.toString()
        actionView?.setOnClickListener {
            (activity as? AddSaleActivity)?.showCart()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }



    companion object {
        val TAG: String = AddItemAddSaleFragment::class.java.simpleName
        fun newInstance() =
            AddItemAddSaleFragment()
    }
}