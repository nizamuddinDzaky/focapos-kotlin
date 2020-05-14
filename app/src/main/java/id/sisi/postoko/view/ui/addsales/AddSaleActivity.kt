package id.sisi.postoko.view.ui.addsales


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.attach
import id.sisi.postoko.utils.extensions.detach
import id.sisi.postoko.utils.helper.AddSaleFragment
import id.sisi.postoko.utils.helper.createFragment
import id.sisi.postoko.utils.helper.findSaleFragmentByTag
import id.sisi.postoko.utils.helper.getTag
import id.sisi.postoko.view.BaseFragment


class AddSaleActivity : AppCompatActivity() {

    var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sale)
        supportActionBar?.title = null

        initFragment(savedInstanceState)
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState ?: switchFragment(AddSaleFragment.SELECT_CUSTOMER)
    }

    fun switchFragment(fragment: AddSaleFragment): Boolean{
        return findFragment(fragment).let {
            if (it.isAdded) return false
            supportFragmentManager.detach() // Extension function
            supportFragmentManager.attach(it, fragment.getTag()) // Extension function
            supportFragmentManager.executePendingTransactions()
        }
    }

    private fun findFragment(fragment: AddSaleFragment): Fragment {
        return supportFragmentManager.findFragmentByTag(fragment.getTag())
            ?: fragment.createFragment()
    }

    override fun onBackPressed() {
        when(currentFragmentTag){
            AddItemAddSaleFragment.TAG -> switchFragment(findSaleFragmentByTag(SelectCustomerFragment.TAG))
            SelectCustomerFragment.TAG -> super.onBackPressed()
        }
    }
}
