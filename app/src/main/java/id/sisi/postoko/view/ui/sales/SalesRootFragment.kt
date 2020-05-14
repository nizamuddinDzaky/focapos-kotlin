package id.sisi.postoko.view.ui.sales

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import id.sisi.postoko.R
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.HomeActivity
import id.sisi.postoko.view.pager.SalesPagerAdapter
import id.sisi.postoko.view.ui.addsales.AddSaleActivity
import kotlinx.android.synthetic.main.fragment_root_sales.*


class SalesRootFragment : Fragment() {
    private val typeface = TypeFace()
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
            startActivityForResult(Intent(this.context, AddSaleActivity::class.java), 2020)
        }


        main_container?.gone()
        main_view_pager?.let {
            it.adapter = SalesPagerAdapter(childFragmentManager, context)
            tabs_main_pagers?.setupWithViewPager(it)
        }
        context?.assets?.let { typeface.fontTab(tabs_main_pagers, "robot_font/Roboto-Regular.ttf", it) }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val item = menu.findItem(R.id.menu_action_search)
        (activity as? HomeActivity)?.assignActionSearch(item, 2)

        super.onCreateOptionsMenu(menu, inflater)
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