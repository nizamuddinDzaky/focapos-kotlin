package id.sisi.postoko.view.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.utils.MySession
import id.sisi.postoko.utils.extensions.isNotCashier
import id.sisi.postoko.utils.extensions.isSuperAdmin
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.showToastAccessDenied
import id.sisi.postoko.utils.helper.Prefs
import id.sisi.postoko.view.AccountViewModel
import id.sisi.postoko.view.HomeActivity
import id.sisi.postoko.view.pager.DashboardPagerAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {
    private lateinit var viewModel: AccountViewModel
    private val prefs: Prefs by lazy {
        Prefs(MyApp.instance)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_account_logout) {
            MySession.logOut(activity)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Dashboard"
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = DashboardPagerAdapter(childFragmentManager)
        view_pager_dashboard?.let {
            it.adapter = adapter
        }

        btn_next_dashboard.setOnClickListener {
            view_pager_dashboard.setCurrentItem(adapter.getCurrentPosition()+1)
        }

        btn_prev_dashboard.setOnClickListener {
            view_pager_dashboard.setCurrentItem(adapter.getCurrentPosition()-1)
        }

        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it) {
                logE("progress")
            } else {
                logE("done")
            }
        })
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            tv_user_company_name?.text = it?.company ?: "~"
            tv_user_company_address?.text = it?.address ?: "~"
            prefs.posRoleId = it?.group_id
            (activity as? HomeActivity)?.hideBottomNavigation()
        })
        view_01?.setOnClickListener {
            (activity as? HomeActivity)?.changeView(R.id.menu_sales_booking)
        }
        view_02?.setOnClickListener {
            (activity as? HomeActivity)?.changeView(R.id.menu_sales_booking)
        }
        view_03?.setOnClickListener {
            if (prefs.posRoleId?.isNotCashier() == true) {
                (activity as? HomeActivity)?.changeView(R.id.menu_good_receive)
            } else {
                context?.showToastAccessDenied()
            }
        }
        view_04?.setOnClickListener {
            if (prefs.posRoleId?.isSuperAdmin() == true) {
                (activity as? HomeActivity)?.changeView(R.id.menu_master_data)
            } else {
                context?.showToastAccessDenied()
            }
        }


//        btn_dummy?.setOnClickListener {
//            val jsonHelper =
//                Gson().fromJson<BaseResponse<DataWarehouse>>(it.context, "DummyListWarehouses.json")
//            logE("result json ${jsonHelper.data?.total_warehouses}")
//        }
    }

    companion object {
        val TAG: String = DashboardFragment::class.java.simpleName
        fun newInstance() = DashboardFragment()
    }
}