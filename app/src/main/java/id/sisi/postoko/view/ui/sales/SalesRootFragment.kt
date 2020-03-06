package id.sisi.postoko.view.ui.sales

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.pager.SalesPagerAdapter
import kotlinx.android.synthetic.main.fragment_root_sales.*

class SalesRootFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_sales_booking)
        return inflater.inflate(R.layout.fragment_root_sales, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            startActivityForResult(Intent(this.context, AddSalesActivity::class.java), 2020)
        }
        main_container?.gone()
        main_view_pager?.let {
            it.adapter = SalesPagerAdapter(childFragmentManager, context)
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
/*
        TODO check condition sales status
        change viewpager and tab
        then
        send activity result
        if (status) {
            fragment.onActivityResult
        }
*/
        (main_view_pager?.adapter as? SalesPagerAdapter)?.getCurrentFragment()
            ?.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val TAG: String = SalesRootFragment::class.java.simpleName
        fun newInstance() = SalesRootFragment()
    }
}