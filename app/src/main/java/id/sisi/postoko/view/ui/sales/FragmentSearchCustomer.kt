package id.sisi.postoko.view.ui.sales

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListSearchCustomerAdapter
import id.sisi.postoko.model.Customer
import id.sisi.postoko.view.ui.customer.CustomerViewModel
import kotlinx.android.synthetic.main.fragment_search_customer.*

class FragmentSearchCustomer : DialogFragment() {
    private lateinit var viewModel: CustomerViewModel
    private lateinit var adapter: ListSearchCustomerAdapter
    var listCustomer: List<Customer>? = arrayListOf()
    var listener: (Customer) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments != null) {
            if (arguments?.getBoolean("notAlertDialog")!!) {
                return super.onCreateDialog(savedInstanceState)
            }
        }
        val builder = AlertDialog.Builder(activity)

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_customer, container, false)
        return inflater.inflate(R.layout.fragment_search_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModel.getListCustomers().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                listCustomer = it
                setupUI(it)
            }
        })
        sv_search_customer_add_sales?.onActionViewExpanded()
        sv_search_customer_add_sales.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    startSearchData(newText)
                } else {
                    listCustomer?.let { setupUI(it) }
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var setFullScreen = false
        if (arguments != null) {
            setFullScreen = requireNotNull(arguments?.getBoolean("fullScreen"))
        }
        if (setFullScreen)
            setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

    }

    private fun dismissDialog() {
        this.dismiss()
    }

    private fun setupUI(it: List<Customer>) {
        setupRecycleView(it)
    }

    private fun startSearchData(query: String) {
        listCustomer?.let {
            val listSearchResult = listCustomer!!.filter {
                it.company.contains(query, true) or it.address.contains(query, true)
            }
            setupUI(listSearchResult)
        }
    }

    private fun setupRecycleView(it: List<Customer>) {
        adapter = ListSearchCustomerAdapter()
        adapter.updateMasterData(it)
        adapter.listener = {
            listener(it)
            dismissDialog()
        }
        rv_list_search_customer?.layoutManager = LinearLayoutManager(this.context)
        rv_list_search_customer?.setHasFixedSize(false)
        rv_list_search_customer?.adapter = adapter
    }
}