package id.sisi.postoko.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.active
import id.sisi.postoko.utils.extensions.attach
import id.sisi.postoko.utils.extensions.detach
import id.sisi.postoko.utils.helper.BottomNavigationPosition
import id.sisi.postoko.utils.helper.createFragment
import id.sisi.postoko.utils.helper.findNavigationPositionById
import id.sisi.postoko.utils.helper.getTag
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val mKeyPosition = "keyPosition"

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
        return supportFragmentManager.findFragmentByTag(position.getTag()) ?: position.createFragment()
    }
}