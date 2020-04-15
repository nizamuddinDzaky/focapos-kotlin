package id.sisi.postoko.view.ui.customergroup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListCustomerSelectedAdapter
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.utils.KEY_CUSTOMER_GROUP
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseActivity

import kotlinx.android.synthetic.main.activity_customer_selected_price_group.*
import kotlinx.android.synthetic.main.content_customer_selected_price_group.*
import kotlinx.android.synthetic.main.failed_load_data.*

class CustomerSelectedCustomerGroupActivity : BaseActivity() {
    private var customerGroup: CustomerGroup = CustomerGroup(id = "0", name = "ForcaPoS")
    private lateinit var vmCustomerGroup: CustomerGroupViewModel
    private lateinit var adapterCustomer: ListCustomerSelectedAdapter
    var firstListCustomer: List<Customer> = listOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_selected_price_group)
        setSupportActionBar(toolbar)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title=null
        intent?.getParcelableExtra<CustomerGroup>(KEY_CUSTOMER_GROUP)?.let {
            customerGroup = it
            toolbar_subtitle.text = "( ${customerGroup.name} )"
        }

        initView()

        vmCustomerGroup.getIsExecute().observe(this, Observer {
            swipeRefreshLayout?.isRefreshing = it
        })

        swipeRefreshLayout?.setOnRefreshListener {
            vmCustomerGroup.getListCustomerCustomerGroup(customerGroup.id, true)
        }



        vmCustomerGroup.getListCustomers().observe(this, Observer {
            firstListCustomer = it ?: listOf()
            setDataCustomer(it)

            if (it?.size ?: 0 == 0) {
                layout_status_progress?.visible()
                rv_list_customer_selected?.gone()
                val status = when(it?.size) {
                    0 -> "Belum ada Data"
                    else -> "Gagal Memuat Data"
                }
                tv_status_progress?.text = status
            } else {
                layout_status_progress?.gone()
                rv_list_customer_selected?.visible()
            }
        })

        vmCustomerGroup.getListCustomerCustomerGroup(customerGroup.id, true)

        fab.setOnClickListener { view ->
            AddCustomerToCustomerGoupActivity.show(
                this as FragmentActivity,
                customerGroup
            )
        }

        sv_customer.setOnClickListener {
            sv_customer?.onActionViewExpanded()
        }
        sv_customer.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    startSearchData(newText)
                } else {
                    setDataCustomer(firstListCustomer)
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    private fun startSearchData(query: String) {
        firstListCustomer.let {
            val listSearchResult = firstListCustomer.filter {
                it.customer_name?.contains(query, true)!!
            }
            setDataCustomer(listSearchResult)
        }
    }

    private fun setDataCustomer(it: List<Customer>?) {
        adapterCustomer.updateCustomerData(it)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        adapterCustomer = ListCustomerSelectedAdapter(fragmentActivity = this)
        adapterCustomer.listenerCustomer={
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
        }
        vmCustomerGroup = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)
        rv_list_customer_selected?.layoutManager = LinearLayoutManager(this)
        rv_list_customer_selected?.setHasFixedSize(false)
        rv_list_customer_selected?.adapter = adapterCustomer
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity,
            customerGroup: CustomerGroup
        ) {
            val page = Intent(fragmentActivity, CustomerSelectedCustomerGroupActivity::class.java)
            page.putExtra(KEY_CUSTOMER_GROUP, customerGroup)
            fragmentActivity.startActivity(page)
        }
    }

}
