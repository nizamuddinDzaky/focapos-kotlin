package id.sisi.postoko.view.ui.sales

//import android.content.Intent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.pager.SalesPagerAdapter
import kotlinx.android.synthetic.main.fragment_root_detail_sales_booking.*
import kotlinx.android.synthetic.main.fragment_root_sales.*
import kotlinx.android.synthetic.main.fragment_root_sales.main_container
import kotlinx.android.synthetic.main.fragment_root_sales.main_view_pager
import kotlinx.android.synthetic.main.fragment_root_sales.tabs_main_pagers

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
            val intent = Intent(this.context, AddSalesActivity::class.java)
            startActivityForResult(intent, 1)
        }

        main_container?.gone()

        main_view_pager?.let {
            it.adapter = SalesPagerAdapter(childFragmentManager, context)
            tabs_main_pagers?.setupWithViewPager(it)
        }

    }

    companion object {
        val TAG: String = SalesRootFragment::class.java.simpleName
        fun newInstance() = SalesRootFragment()
    }
}