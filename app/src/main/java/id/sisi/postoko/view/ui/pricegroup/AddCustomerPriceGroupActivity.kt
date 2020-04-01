package id.sisi.postoko.view.ui.pricegroup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.activity_customer_price_group.*
import kotlinx.android.synthetic.main.list_item_master.view.*
import kotlinx.android.synthetic.main.master_data_fragment.*

class AddCustomerPriceGroupActivity : BaseActivity() {
    val customer = CustomerPriceGroupFragment.newInstance()
    val cartCustomer = CartCustomerToPGFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_price_group)
        displayHomeEnable()

        main_container?.gone()
        main_view_pager?.let {
            it.adapter = CustomerPriceGroupPagerAdapter(
                supportFragmentManager, mutableListOf(
                    customer,
                    cartCustomer
                )
            )
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }

    fun countInc(value: Customer) {
        cartCustomer.count = (cartCustomer.count ?: 0) + 1
        cartCustomer.addToAdapter(value)
        updateTab()
    }

    fun countDec(value: Customer) {
        if (cartCustomer.count ?: 0 > 0) {
            cartCustomer.count = cartCustomer.count?.minus(1)
        }
        customer.addToAdapter(value)
        updateTab()
    }

    fun updateTab() {
        tabs_main_pagers?.getTabAt(1)?.text = cartCustomer.tagName
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity
        ) {
            val page = Intent(fragmentActivity, AddCustomerPriceGroupActivity::class.java)
            fragmentActivity.startActivity(page)
        }
    }
}

class CustomerPriceGroupPagerAdapter(
    fm: FragmentManager,
    private val pages: MutableList<BaseFragment>
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = pages[position]

    override fun getCount() = pages.size

    override fun getPageTitle(position: Int) = pages[position].tagName
}

class CartCustomerToPGFragment : BaseFragment() {
    companion object {
        fun newInstance() = CartCustomerToPGFragment()
    }

    override var tagName: String
        get() = "Daftar Anggota${if (count == null) "" else " ($count)"}"
        set(_) {}

    private lateinit var adapter: ListCartToCustAdapter<Customer>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.master_data_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        fb_add_master?.gone()
        setupRecycleView()
    }

    fun addToAdapter(value: Customer) {
        adapter.addData(value)
    }

    private fun setupRecycleView() {
        adapter = ListCartToCustAdapter(fragmentActivity = activity)
        rv_list_master_data?.layoutManager = LinearLayoutManager(this.context)
        rv_list_master_data?.setHasFixedSize(false)
        rv_list_master_data?.adapter = adapter
    }
}

class ListCustToCartAdapter<T>(
    private var masterData: MutableList<T>? = mutableListOf(),
    private var fragmentActivity: FragmentActivity? = null
) :
    RecyclerView.Adapter<ListCustToCartAdapter.MasterViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_master, parent, false)

        return MasterViewHolder(view, fragmentActivity)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: MasterViewHolder<T>, position: Int) {
        holder.bind(masterData?.get(position), this)
    }

    class MasterViewHolder<T>(
        itemView: View,
        private val fragmentActivity: FragmentActivity? = null
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(value: T?, adapter: ListCustToCartAdapter<T>) {
            when (value) {
                is Customer -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.address
                    itemView.setOnClickListener {
                        adapter.addCustomerToCart(value)
                    }
                }
            }
        }
    }

    fun addCustomerToCart(value: T) {
        masterData?.remove(value)
        (fragmentActivity as? AddCustomerPriceGroupActivity)?.countInc(value as Customer)
        notifyDataSetChanged()
    }

    fun addData(value: T) {
        masterData?.add(value)
        notifyDataSetChanged()
    }

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData?.toMutableList()
        notifyDataSetChanged()
    }
}

class ListCartToCustAdapter<T>(
    private var masterData: MutableList<T>? = mutableListOf(),
    private var fragmentActivity: FragmentActivity? = null
) :
    RecyclerView.Adapter<ListCartToCustAdapter.MasterViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_master, parent, false)

        return MasterViewHolder(view, fragmentActivity)
    }

    override fun getItemCount(): Int {
        return masterData?.size ?: 0
    }

    override fun onBindViewHolder(holder: MasterViewHolder<T>, position: Int) {
        holder.bind(masterData?.get(position), this)
    }

    class MasterViewHolder<T>(
        itemView: View,
        private val fragmentActivity: FragmentActivity? = null
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(value: T?, adapter: ListCartToCustAdapter<T>) {
            when (value) {
                is Customer -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.address
                    itemView.setOnClickListener {
                        adapter.removeCustomerFromCart(value)
                    }
                }
            }
        }
    }

    fun removeCustomerFromCart(value: T) {
        masterData?.remove(value)
        (fragmentActivity as? AddCustomerPriceGroupActivity)?.countDec(value as Customer)
        notifyDataSetChanged()
    }

    fun addData(value: T) {
        masterData?.add(value)
        notifyDataSetChanged()
    }

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData?.toMutableList()
        notifyDataSetChanged()
    }
}