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
import kotlinx.android.synthetic.main.select_customer_add_sale_fragment.*


class SelectCustomerFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = null
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        return inflater.inflate(R.layout.select_customer_add_sale_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(context, "Select Customer", Toast.LENGTH_SHORT).show()

        (activity as AddSaleActivity?)?.currentFragmentTag = TAG

        toolbar.setNavigationOnClickListener {
            (activity as AddSaleActivity?)?.finish()
        }

        btn_action_submit.setOnClickListener {
            (activity as AddSaleActivity?)?.switchFragment(findSaleFragmentByTag(AddItemAddSaleFragment.TAG))
        }
    }

    companion object {
        val TAG: String = SelectCustomerFragment::class.java.simpleName
        fun newInstance() =
            SelectCustomerFragment()
    }
}