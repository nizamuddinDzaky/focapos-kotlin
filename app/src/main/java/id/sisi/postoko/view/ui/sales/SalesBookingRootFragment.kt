package id.sisi.postoko.view.ui.sales

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.HomeActivity
import id.sisi.postoko.view.pager.SalesBookingPagerAdapter
import id.sisi.postoko.view.sales.SBFragment
import id.sisi.postoko.view.ui.addsales.AddSaleActivity
import kotlinx.android.synthetic.main.fragment_root_sales_booking.*


class SalesBookingRootFragment(var isAksestoko: Boolean = false, var strTag: String = "Sales Booking") : BaseFragment() {
    private val typeface = TypeFace()
    override var tagName: String
        get() = strTag
        set(_) {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_root_sales_booking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            startActivityForResult(Intent(this.context, AddSaleActivity::class.java), 2020)
        }

        /*(activity as? HomeActivity)?.initSBFragment(isAksestoko)*/
        main_container?.gone()
        main_view_pager?.let {
            it.adapter = SalesBookingPagerAdapter(childFragmentManager, context, isAksestoko)
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
        (activity as? HomeActivity)?.initSBFragment(isAksestoko, item, 2)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.menu_action_filter -> {
                true
            }
            R.id.menu_action_search -> {
                /*(activity as? HomeActivity)?.initSBFragment(isAksestoko)
                (activity as? HomeActivity)?.assignActionSearch(item, 2)*/
                /*Toast.makeText(context, "qqqqqq", Toast.LENGTH_SHORT).show()*/
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2020) {
            val saleStatus = data?.getStringExtra("sale_status")
            var activeFragment = 0
            if(saleStatus == "closed"){
                activeFragment = 2
            }else if(saleStatus == "reserved"){
                activeFragment = 1
            }
            main_view_pager.currentItem = activeFragment
            (main_view_pager?.adapter as? SalesBookingPagerAdapter)?.getItem(activeFragment)
                ?.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        val TAG: String = SalesBookingRootFragment::class.java.simpleName
        fun newInstance() = SalesBookingRootFragment()
    }
}