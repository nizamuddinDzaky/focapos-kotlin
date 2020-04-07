package id.sisi.postoko.view.ui.pricegroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.extensions.addVerticalDivider
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_cart_customer_to_price_group.*
import kotlinx.android.synthetic.main.master_data_fragment.*
import kotlinx.android.synthetic.main.master_data_fragment.rv_list_master_data

class CartCustomerToPGFragment : BaseFragment() {
    companion object {
        fun newInstance() =
            CartCustomerToPGFragment()
    }

    override var tagName: String
        get() = "Daftar Anggota${if (getCountData() == null) "" else " (${getCountData()})"}"
        set(_) {}

    private lateinit var adapter: ListCartToCustomerAdapter<Customer>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart_customer_to_price_group, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()
        setupData()
    }

    private fun setupData() {
        (activity as? AddCustomerPriceGroupActivity)?.apply {
            firstListCustomer.let {
                adapter.updateMasterData(it)
            }
            updateTab()
        }
    }

    private fun setupUI() {
        fb_add_master?.gone()
        var txtSubmit = "Tambahkan"
        (activity as? AddCustomerPriceGroupActivity)?.priceGroup?.let {
            txtSubmit = txtSubmit.plus(" ke ${it.name}")
        }
        btn_action_submit?.text = txtSubmit
        setupRecycleView()
    }

    fun addToAdapter(value: Customer) {
        adapter.addData(value)
    }

    fun getCountData(): Int? {
        if (::adapter.isInitialized) {
            return adapter.itemCount
        }
        return null
    }

//    fun firstData() {
//        //adapter.updateMasterData(listOf(Customer(), Customer(), Customer(), Customer()))
//        (activity as? AddCustomerPriceGroupActivity)?.firstListCustomer?.let {
//            adapter.updateMasterData(it)
//        }
//    }

    private fun setupRecycleView() {
        adapter = ListCartToCustomerAdapter(
            fragmentActivity = activity
        )
        rv_list_master_data?.layoutManager =
            LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.addVerticalDivider()
        rv_list_master_data?.adapter = adapter
    }
}