package id.sisi.postoko.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.utils.helper.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val mKeyPosition = "keyPosition"
    private val prefs: Prefs by lazy {
        Prefs(MyApp.instance)
    }
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreSaveInstanceState(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.elevation = 0.0F

        bottom_navigation?.apply {
            active(navPosition.position) // Extension function
            setOnNavigationItemSelectedListener { item ->
                navPosition = findNavigationPositionById(item.itemId)
                switchFragment(navPosition)
            }
        }

        initFragment(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation()
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