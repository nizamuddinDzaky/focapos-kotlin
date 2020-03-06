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
        if (requestCode == 2020) {
            val sale_status = data?.getStringExtra("sale_status")
            if(sale_status == "pending"){
                main_view_pager.setCurrentItem(0)
            }else if(sale_status == "reserved"){
                main_view_pager.setCurrentItem(1)
            }
        }
        (main_view_pager?.adapter as? SalesPagerAdapter)?.getCurrentFragment()
            ?.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val TAG: String = SalesRootFragment::class.java.simpleName
        fun newInstance() = SalesRootFragment()
    }
}