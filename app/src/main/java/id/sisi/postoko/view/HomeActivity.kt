package id.sisi.postoko.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.utils.MySearchView
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.utils.helper.*
import id.sisi.postoko.view.research.GRFragment
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home3.*

class HomeActivity2 : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home3)

        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_good, menu)

        val item = menu?.findItem(R.id.menu_action_search)
        search_view?.setMenuItem(item)

        return true
    }

    fun hideBottomNavigation() {
    }

    fun changeView(menuSalesBooking: Int) {
    }
}

class HomeActivity : BaseActivity() {
    private val mKeyPosition = "keyPosition"
    private val prefs: Prefs by lazy {
        Prefs(MyApp.instance)
    }
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.HOME
    private val grFragment: GRFragment? = GRFragment(GoodReceiveStatus.ALL)

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
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation()
    }

    fun startSearch(item: MenuItem) {
        search_view?.setMenuItem(item)
    }

    private fun initSearch() {
        search_view?.setOnQueryTextListener(object : MySearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (!p0.isNullOrEmpty() && p0.length > 2) {
                    grFragment?.testSearch(p0)
                }
                return false
            }
        })
        search_view?.setOnSearchViewListener(object : MySearchView.SearchViewListener {
            override fun onSearchViewShown() {
                main_content_container?.gone()
                search_container?.visible()
                showSearch(true)
            }

            override fun onSearchViewClosed() {
                search_container?.gone()
                main_content_container?.visible()
                showSearch(false)
            }
        })
    }

    fun showSearch(isShown: Boolean) {
        if (isShown) {
            grFragment?.let {
                if (it.isAdded) return
                supportFragmentManager.detachSearch()
                supportFragmentManager.attachSearch(it, "search")
                supportFragmentManager.executePendingTransactions()
            }
        } else {
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
        moveTaskToBack(false)
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
        if (id.sisi.postoko.BuildConfig.DEBUG) {
            val roleId = prefs.posRoleId ?: 0
            bottom_navigation?.menu?.findItem(R.id.menu_master_data)?.isVisible =
                roleId.isSuperAdmin()
            bottom_navigation?.menu?.findItem(R.id.menu_good_receive)?.isVisible =
                roleId.isNotCashier()
        }
    }
}