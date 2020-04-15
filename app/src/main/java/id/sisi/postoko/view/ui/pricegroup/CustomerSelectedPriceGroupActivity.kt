package id.sisi.postoko.view.ui.pricegroup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListCustomerSelectedPGAdapter
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.RC_ADD_CUSTOMER_TO_PG
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.activity_customer_selected_price_group.*
import kotlinx.android.synthetic.main.content_customer_selected_price_group.*
import kotlinx.android.synthetic.main.failed_load_data.*

class CustomerSelectedPriceGroupActivity : BaseActivity() {
    private var priceGroup: PriceGroup? = PriceGroup(name = "ForcaPoS")
    private lateinit var vmPriceGroup: PriceGroupViewModel
    private lateinit var adapterCustomer: ListCustomerSelectedPGAdapter
    var firstListCustomer: List<Customer> = listOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_selected_price_group)
        setSupportActionBar(toolbar)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = null
        intent?.getParcelableExtra<PriceGroup>(KEY_PRICE_GROUP)?.let {
            priceGroup = it
        }
        toolbar_subtitle.text = "( ${priceGroup?.name} )"

        initView()

        vmPriceGroup.getIsExecute().observe(this, Observer {
            swipeRefreshLayout?.isRefreshing = it
        })

        swipeRefreshLayout?.setOnRefreshListener {
            vmPriceGroup.getListCustomerPriceGroup(priceGroup?.id.toString(), true)
        }

        vmPriceGroup.getListCustomers().observe(this, Observer {
            firstListCustomer = it ?: listOf()
            setDataCustomer(it)

            if (it?.size ?: 0 == 0) {
                layout_status_progress?.visible()
                rv_list_customer_selected?.gone()
                val status = when(it?.size) {
                    0 -> "Belum ada pembayaran"
                    else -> "Gagal Memuat Data"
                }
                tv_status_progress?.text = status
            } else {
                layout_status_progress?.gone()
                rv_list_customer_selected?.visible()
            }
        })

        vmPriceGroup.getListCustomerPriceGroup(priceGroup?.id.toString(), true)

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

        fab.setOnClickListener { _ ->
            priceGroup?.let {
                AddCustomerPriceGroupActivity.show(
                    this as FragmentActivity,
                    it
                )
            }
        }
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

    private fun initView() {
        adapterCustomer = ListCustomerSelectedPGAdapter(fragmentActivity = this)
        rv_list_customer_selected?.layoutManager = LinearLayoutManager(this)
        rv_list_customer_selected?.setHasFixedSize(false)
        rv_list_customer_selected?.adapter = adapterCustomer
        vmPriceGroup = ViewModelProvider(this).get(PriceGroupViewModel::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logE("request code : $requestCode")
        if(requestCode == RC_ADD_CUSTOMER_TO_PG){
            vmPriceGroup.getListCustomerPriceGroup(priceGroup?.id.toString(), true)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity,
            priceGroup: PriceGroup
        ) {
            val page = Intent(fragmentActivity, CustomerSelectedPriceGroupActivity::class.java)
//            page.putExtra("fragment_activty", fragmentActivity)
            page.putExtra(KEY_PRICE_GROUP, priceGroup)
            fragmentActivity.startActivity(page)
        }
    }
}
