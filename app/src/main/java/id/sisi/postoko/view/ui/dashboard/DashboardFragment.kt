package id.sisi.postoko.view.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.AccountViewModel
import id.sisi.postoko.view.HomeActivity
import id.sisi.postoko.view.MainActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {
    private lateinit var viewModel: AccountViewModel

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
            MyApp.prefs.isLogin = false
            val page = Intent(context, MainActivity::class.java)
            page.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(page)
            activity?.finish()
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
        })
        view_01?.setOnClickListener {
            (activity as? HomeActivity)?.changeView(R.id.menu_sales_booking)
        }
        view_02?.setOnClickListener {
            (activity as? HomeActivity)?.changeView(R.id.menu_sales_booking)
        }
        view_03?.setOnClickListener {
            (activity as? HomeActivity)?.changeView(R.id.menu_good_receive)
        }
        view_04?.setOnClickListener {
            (activity as? HomeActivity)?.changeView(R.id.menu_master_data)
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