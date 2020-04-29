package id.sisi.postoko.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.utils.MySearchView
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.utils.helper.*
import id.sisi.postoko.view.research.GRFragment
import id.sisi.postoko.view.sales.SBFragment
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus
import id.sisi.postoko.view.ui.sales.SaleStatus
//import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home3.*


class HomeActivity : BaseActivity() {
    private val mKeyPosition = "keyPosition"
    private val prefs: Prefs by lazy {
        Prefs(MyApp.instance)
    }
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.HOME
    val grFragment: GRFragment? = GRFragment(GoodReceiveStatus.ALL)
    val sbFragment: SBFragment? = SBFragment(SaleStatus.ALL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreSaveInstanceState(savedInstanceState)
        setContentView(R.layout.activity_home3)

        setSupportActionBar(toolbar)

        bottom_navigation?.apply {
            active(navPosition.position) // Extension function
            setOnNavigationItemSelectedListener { item ->
                navPosition = findNavigationPositionById(item.itemId)
                switchFragment(navPosition)
            }
        }

        initFragment(savedInstanceState)
        initSearch()
        

        //Update available
        var appUpdater = AppUpdater(this);
        appUpdater.setDisplay(com.github.javiersantos.appupdater.enums.Display.DIALOG);
        appUpdater.setUpdateFrom(UpdateFrom.GOOGLE_PLAY);
        appUpdater.start();
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
}