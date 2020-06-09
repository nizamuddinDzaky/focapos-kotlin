package id.sisi.postoko.view.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.whiteelephant.monthpicker.MonthPickerDialog
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.utils.LoadImageFromUrl
import id.sisi.postoko.utils.MySession
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.URL_AVATAR_PROFILE
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.utils.helper.Prefs
import id.sisi.postoko.view.AccountViewModel
import id.sisi.postoko.view.HomeActivity
import id.sisi.postoko.view.pager.DashboardPieChartAdapter
import id.sisi.postoko.view.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard_pager.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("SimpleDateFormat")
class DashboardFragment : Fragment() {
    private lateinit var viewModel: AccountViewModel
    private val typeface = TypeFace()
    private val today: Calendar = getInstance()
    private val calendar: Calendar = GregorianCalendar()
    val inputDateFormat = SimpleDateFormat("yyyy MM")
    val outputDateFormat = SimpleDateFormat("MMMM yyyy")
    var selectedMonth = calendar[MONTH]
    var selectedYear = calendar[YEAR]
    var idWarehouse: String = ""
    var warehouseName: String? = null

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

    @SuppressLint("SimpleDateFormat")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = DashboardPieChartAdapter(childFragmentManager)
        view_pager_dashboard?.let {
            it.adapter = adapter
        }

        activity?.assets?.let {
            typeface.typeFace("robot_font/Roboto-Bold.ttf",tv_user_company_name, it)
            typeface.typeFace("robot_font/Roboto-Regular.ttf",tv_user_company_address, it)
            typeface.typeFace("robot_font/Roboto-Regular.ttf",tv_view_01, it)
            typeface.typeFace("robot_font/Roboto-Regular.ttf",tv_view_02, it)
            typeface.typeFace("robot_font/Roboto-Regular.ttf",tv_view_03, it)
            typeface.typeFace("robot_font/Roboto-Regular.ttf",tv_view_04, it)
            typeface.typeFace("robot_font/Roboto-Bold.ttf",tv_date_filter, it)
        }

        view_pager_dashboard.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {
                selectedMonth = position
                val mFragment: DashboardPiechartFragment = adapter.getItem(view_pager_dashboard.currentItem) as DashboardPiechartFragment
                if(warehouseName != null){
                    mFragment.tv_warehouse_name_piechart.text = warehouseName
                    mFragment.iv_delete_warehouse_piechart.visible()
                }
                mFragment.refresh(selectedYear, idWarehouse)
                tv_date_filter.text = outputDateFormat.format(inputDateFormat.parse("$selectedYear ${selectedMonth+1}"))
            }
        })

        btn_next_dashboard.setOnClickListener {
            val tmpMonth = selectedMonth +1
            if(tmpMonth < 12){
                selectedMonth=tmpMonth
                view_pager_dashboard.currentItem = selectedMonth
                tv_date_filter.text = outputDateFormat.format(inputDateFormat.parse("$selectedYear ${selectedMonth+1}"))
            }
        }

        btn_prev_dashboard.setOnClickListener {
            val tmpMonth = selectedMonth - 1
            if(tmpMonth > -1){
                selectedMonth=tmpMonth
                view_pager_dashboard.currentItem = selectedMonth
                tv_date_filter.text = outputDateFormat.format(inputDateFormat.parse("$selectedYear ${selectedMonth+1}"))
            }
        }

        val resultDate = inputDateFormat.parse("$selectedYear ${selectedMonth+1}")
        val strDate = outputDateFormat.format(resultDate)
        tv_date_filter.text = strDate
        view_pager_dashboard.currentItem = selectedMonth

        layout_filter_date.setOnClickListener {
            val builder = MonthPickerDialog.Builder(context,
                MonthPickerDialog.OnDateSetListener { month, year ->
                    selectedMonth = month
                    selectedYear = year
                    view_pager_dashboard.currentItem = selectedMonth
                    val page = childFragmentManager.findFragmentByTag("android:switcher:${R.id.view_pager_dashboard}:${selectedMonth}") as DashboardPiechartFragment
                    page.refresh(selectedYear, idWarehouse)
                    tv_date_filter.text = outputDateFormat.format(inputDateFormat.parse("$selectedYear ${selectedMonth+1}"))
                }, selectedYear, selectedMonth)

            builder.setActivatedMonth(selectedMonth)
                .setMinYear(1990)
                .setActivatedYear(selectedYear)
                .setMaxYear(today.get(YEAR))
                .setMinMonth(JANUARY)
                .setTitle(null)
                .setMonthRange(JANUARY, DECEMBER)
                .setOnMonthChangedListener { }
                .setOnYearChangedListener { }
                .build()
                .show()
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
            val loadImage = context?.let { context ->
                LoadImageFromUrl(
                    iv_header_avatar,
                    context,
                    R.drawable.avatar_account
                )
            }
            loadImage?.execute("$URL_AVATAR_PROFILE${it?.avatar}")
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
        iv_header_avatar?.setOnClickListener {
            ProfileActivity.show(activity)
        }
        tv_user_company_name?.setOnClickListener { iv_header_avatar?.performClick() }
        tv_user_company_address?.setOnClickListener { iv_header_avatar?.performClick() }
    }

    companion object {
        val TAG: String = DashboardFragment::class.java.simpleName
        fun newInstance() = DashboardFragment()
    }
}