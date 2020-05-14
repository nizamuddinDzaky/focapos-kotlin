package id.sisi.postoko.view.ui.addsales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.helper.findSaleFragmentByTag
import kotlinx.android.synthetic.main.add_item_add_sale_fragment.*


class AddItemAddSaleFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = null
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        return inflater.inflate(R.layout.add_item_add_sale_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AddSaleActivity?)?.currentFragmentTag = TAG

        Toast.makeText(context, "Add Item", Toast.LENGTH_SHORT).show()
        toolbar.setNavigationOnClickListener {
            (activity as AddSaleActivity?)?.switchFragment(findSaleFragmentByTag(SelectCustomerFragment.TAG))
        }
    }
    fun onBackPressed(): Boolean {
        return false
    }

    companion object {
        val TAG: String = AddItemAddSaleFragment::class.java.simpleName
        fun newInstance() =
            AddItemAddSaleFragment()
    }
}