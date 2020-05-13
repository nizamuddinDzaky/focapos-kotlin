package id.sisi.postoko.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.google.firebase.iid.FirebaseInstanceId
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.MySearchView
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.utils.helper.*
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.research.GRFragment
import id.sisi.postoko.view.sales.SBFragment
import id.sisi.postoko.view.ui.MasterDetailViewModel
import id.sisi.postoko.view.ui.customer.CustomerViewModel
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus
import id.sisi.postoko.view.ui.sales.SaleStatus
//import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home3.*
import kotlinx.android.synthetic.main.content_detail_customer.*
import kotlinx.android.synthetic.main.dialog_syncron_master_customer.*


class HomeActivity : BaseActivity() {
    private val progressBar = CustomProgressBar()
    private val mKeyPosition = "keyPosition"
    private val prefs: Prefs by lazy {
        Prefs(MyApp.instance)
    }
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.HOME
    val grFragment: GRFragment? = GRFragment(GoodReceiveStatus.ALL)
    val sbFragment: SBFragment? = SBFragment(SaleStatus.ALL)

    var customer: Customer? = null
    private lateinit var viewModelCustomer: MasterDetailViewModel
    private lateinit var viewCustomerViewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreSaveInstanceState(savedInstanceState)
        setContentView(R.layout.activity_home3)

        setSupportActionBar(toolbar)

        initFragment(savedInstanceState)
        bottom_navigation?.apply {
            active(navPosition.position) // Extension function
            setOnNavigationItemSelectedListener { item ->
                navPosition = findNavigationPositionById(item.itemId)
                switchFragment(navPosition)
            }
        }


        initSearch()

        viewCustomerViewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)

        viewModelCustomer = ViewModelProvider(this).get(MasterDetailViewModel::class.java)
        viewModelCustomer.getDetailCustomer().observe(this, Observer {
            if (it != null) {
                customer = it
            }
        })
        viewModelCustomer.requestDetailCustomer(2)

        //Update available
        val appUpdater = AppUpdater(this)
        appUpdater.setDisplay(com.github.javiersantos.appupdater.enums.Display.DIALOG)
        appUpdater.setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
        appUpdater.start()

        val firebaseToken = FirebaseInstanceId.getInstance().getToken()
        Toast.makeText(this, "FCM Registration Token: $firebaseToken", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation()
    }

    fun assignActionSearch(item: MenuItem, typeView: Int) {
        search_view?.setMenuItem(item)
        search_view?.typeView = typeView
    }

    private fun isSearchViewActive() = search_container.visibility == android.view.View.VISIBLE

    private fun initSearch() {
        search_view?.setOnQueryTextListener(object : MySearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    if (search_view.typeView == 1) {
                        grFragment?.submitQuerySearch(newText)
                    } else if (search_view.typeView == 2) {
                        sbFragment?.submitQuerySearch(newText)
                    }
                }
                return false
            }
        })
        search_view?.setOnSearchViewListener(object : MySearchView.SearchViewListener {
            override fun onSearchViewShown() {
                showSearch(true)
            }

            override fun onSearchViewClosed() {
                showSearch(false)
            }

            override fun onFilter() {
                if (search_view.typeView == 1) {
                    grFragment?.showBottomSheetFilter(true)
                } else if (search_view.typeView == 2) {
                    sbFragment?.showBottomSheetFilter(true)
                }
            }
        })
    }

    fun showSearch(isShown: Boolean) {
        if (isShown) {
//            main_content_container?.gone()
            search_container?.visible()
            if (search_view.typeView == 1) {
                grFragment?.let {
                    if (it.isAdded) return
                    supportFragmentManager.detachSearch()
                    supportFragmentManager.attachSearch(it, "search")
                    supportFragmentManager.executePendingTransactions()
                }
            } else if (search_view.typeView == 2) {
                sbFragment?.let {
                    if (it.isAdded) return
                    supportFragmentManager.detachSearch()
                    supportFragmentManager.attachSearch(it, "search")
                    supportFragmentManager.executePendingTransactions()
                }
            }
        } else {
            search_container?.gone()
//            main_content_container?.visible()
            switchFragment(navPosition)
        }
    }

    fun changeView(itemId: Int) {
        navPosition = findNavigationPositionById(itemId)
        bottom_navigation?.active(navPosition.position)
        switchFragment(navPosition)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(mKeyPosition, navPosition.id)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (isSearchViewActive()) {
            search_view?.closeSearch()
        } else {
            moveTaskToBack(false)
        }
    }

    private fun restoreSaveInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.getInt(mKeyPosition, BottomNavigationPosition.HOME.id)?.also {
            navPosition = findNavigationPositionById(it)
        }
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState ?: switchFragment(BottomNavigationPosition.HOME)
    }

    private fun switchFragment(navPosition: BottomNavigationPosition): Boolean {
        if (BottomNavigationPosition.HOME == navPosition) {
            app_bar?.setBackgroundResource(R.drawable.gradient_bg_appbar)
        } else {
            app_bar?.setBackgroundResource(R.color.main_blue)
            app_bar?.setExpanded(true)
        }
        return findFragment(navPosition).let {
            if (it.isAdded) return false
            supportFragmentManager.detach() // Extension function
            supportFragmentManager.attach(it, navPosition.getTag()) // Extension function
            supportFragmentManager.executePendingTransactions()
        }
    }

    private fun findFragment(position: BottomNavigationPosition): Fragment {
        return supportFragmentManager.findFragmentByTag(position.getTag())
            ?: position.createFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        findFragment(navPosition).onActivityResult(requestCode, resultCode, data)
    }

    fun hideBottomNavigation() {
        val roleId = prefs.posRoleId ?: 0
        bottom_navigation?.menu?.findItem(R.id.menu_master_data)?.isVisible =
            roleId.isSuperAdmin()
        bottom_navigation?.menu?.findItem(R.id.menu_good_receive)?.isVisible =
            roleId.isNotCashier()
    }

    fun syncMasterCustomer(){
        val dialog = Dialog(this, R.style.MyCustomDialogFullScreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_syncron_master_customer)
        dialog.tv_message.setText(Html.fromHtml("Anda yakin akan melakukan sinkron data pelanggan?"))

        viewCustomerViewModel.getSyncCustomerToBK().observe(this, Observer {
            progressBar.dialog.dismiss()
            if (it != null) {
                Toast.makeText(this, "Sinkron Sukses, Total pelanggan berhasil disinkron : "+it.total_customer_data, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Post Sync customer to bk failed because data store active not found!", Toast.LENGTH_LONG).show()
            }
        })

        dialog.tv_batal.setOnClickListener {
            dialog.dismiss()
        }

        dialog.tv_syncron.setOnClickListener {
            dialog.dismiss()
            progressBar.show(this, "Silakan tunggu...")
            viewCustomerViewModel.regSyncCustomerToBK()
        }

        dialog.show()
    }
}