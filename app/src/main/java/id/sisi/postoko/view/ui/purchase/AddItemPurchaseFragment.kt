package id.sisi.postoko.view.ui.purchase

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.helper.findPurchaseFragmentByTag
import id.sisi.postoko.view.ui.addsales.AddItemSaleDialogFragment
import id.sisi.postoko.view.ui.sales.FragmentSearchCustomer
import id.sisi.postoko.view.ui.supplier.SupplierDialogFragment
import kotlinx.android.synthetic.main.add_item_add_purchase_fragment.*

class AddItemPurchaseFragment: Fragment() {

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
        return inflater.inflate(R.layout.add_item_add_purchase_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AddPurchaseActivity?)?.currentFragmentTag = TAG

        if ((activity as AddPurchaseActivity?)?.idSupplier != null){
            sp_supplier.setText((activity as AddPurchaseActivity?)?.supplierName)
        }

        sp_supplier.setOnClickListener {
            val dialogFragment = SupplierDialogFragment()
            dialogFragment.listener={ supplier ->
                (activity as AddPurchaseActivity?)?.vmProduct?.getListProductPurchase(
                    supplier.id.toInt() ?: 0
                ){listProduct ->
                    if (listProduct != null) {
                        (activity as AddPurchaseActivity?)?.listProduct = listProduct
                        this.listProduct = listProduct
                    }
                    logE("wahai bersahabatlah")
                    setupUI(this.listProduct)
                }
                (activity as AddPurchaseActivity?)?.idSupplier = supplier.id
                (activity as AddPurchaseActivity?)?.supplierName = supplier.name
                sp_supplier.setText(supplier.name)
            }
            dialogFragment.show(childFragmentManager, FragmentSearchCustomer().tag)
        }

        (activity as AddPurchaseActivity?)?.listProduct?.let {
            listProduct = it
            setupUI(it)
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

        btn_action_submit.setOnClickListener {
            if (((activity as AddPurchaseActivity?)?.countItemSelected() ?: 0) > 0)
                (activity as AddPurchaseActivity?)?.switchFragment(
                    findPurchaseFragmentByTag(
                        PaymentAddPurchaseFragment.TAG)
                )
            else
                myDialog.alert(getString(R.string.txt_helper_product), context)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cart, menu)
        val actionView = menu.findItem(R.id.action_cart)?.actionView
        val total = (activity as? AddPurchaseActivity)?.countItemSelected()
        actionView?.rootView?.findViewById<TextView>(R.id.cart_badge)?.text = total.toString()
        actionView?.setOnClickListener {
            (activity as? AddPurchaseActivity)?.showCart()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun startSearchData(query: String) {
        listProduct.let {
            val listSearchResult = listProduct.filter {
                it.name.contains(query, true)
            }
            setupUI(listSearchResult)
        }
    }

    fun setupUI(listProduct: List<Product>) {
        logE("wwkk $listProduct");
        setupRecycleView(listProduct)
    }

    private fun setupRecycleView(listProduct: List<Product>) {
        val adapter = (activity as AddPurchaseActivity).adapter
        adapter.updateData(listProduct)
        adapter.notifyDataSetChanged()
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
            logE("selected123 ${product.isSelected}")
            (activity as AddPurchaseActivity?)?.setUpBadge()
            setUpTotal()
        }
        dialogFragment.listenerRemove = {
            (activity as AddPurchaseActivity?)?.setUpBadge()
            setUpTotal()
        }
        dialogFragment.show(childFragmentManager, AddItemSaleDialogFragment(product).tag)
    }

    private fun setUpTotal(){
        val total = (activity as AddPurchaseActivity?)?.getTotal()
        val disc = (activity as AddPurchaseActivity?)?.getDiscount()
        tv_total_add_sale.text = total?.toCurrencyID()
        tv_total_diskon.text = disc?.toCurrencyID()
    }



    companion object {
        val TAG: String = AddItemPurchaseFragment::class.java.simpleName
        fun newInstance() =
            AddItemPurchaseFragment()
    }
}