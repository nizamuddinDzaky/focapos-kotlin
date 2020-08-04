package id.sisi.postoko.view.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListLegendDashboardAdapter
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.sales.SaleStatus
import id.sisi.postoko.view.ui.warehouse.WarehouseDialogFragment
import kotlinx.android.synthetic.main.fragment_dashboard_pager.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DashboardPiechartFragment(private var month: Int) : BaseFragment() {

    private lateinit var adapter: ListLegendDashboardAdapter
    private var listStatus: ArrayList<Int> =
        arrayListOf(
            R.string.txt_status_closed,
            R.string.txt_status_reserved,
            R.string.txt_status_pending
        )
    private var listImage: ArrayList<Int> = arrayListOf(R.drawable.circle_cancel, R.drawable.circle_sukses, R.drawable.circle_pending)
    private var listJumlah: ArrayList<String> = arrayListOf()

    private var warehouse: Warehouse = Warehouse(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "Semua Gudang",
        "",
        "",
        "",
        "",
        "")
//    private var idWarehouse: String? = null
    private var totClosed: Double = 0.0
    private var totPending: Double = 0.0
    private var totReserved: Double = 0.0
    private var totalTransaksi: Double = 0.0
    private lateinit var viewModel: PiechartViewModel
    override lateinit var tagName: String

    companion object {
        fun newInstance(): DashboardPiechartFragment {
            val calendar: Calendar = GregorianCalendar()
            return DashboardPiechartFragment(calendar[Calendar.MONTH])
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (parentFragment as DashboardFragment).selectedYear

        viewModel = ViewModelProvider(this).get(PiechartViewModel::class.java)

        viewModel.getPieChartData().observe(viewLifecycleOwner, Observer {
            totClosed = it.closed?.toDouble() ?: 0.0
            totPending = it.pending?.toDouble() ?: 0.0
            totReserved = it.reserved?.toDouble() ?: 0.0
            listJumlah.clear()
            listJumlah.add("$totClosed Transaksi")
            listJumlah.add("$totReserved Transaksi")
            listJumlah.add("$totPending Transaksi")
            setupRecycleView()
            setUpPieChart()
        })

        return inflater.inflate(R.layout.fragment_dashboard_pager, container, false)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val selectedYear = (parentFragment as DashboardFragment).selectedYear
        val warehouseId = (parentFragment as DashboardFragment).idWarehouse
        SimpleDateFormat("MM")
        SimpleDateFormat("MMMM")
        l_filter_warehouse.setOnClickListener{
            val dialogFragment = WarehouseDialogFragment(header = warehouse)
            dialogFragment.listener = {
                (parentFragment as DashboardFragment).idWarehouse =it.id
                (parentFragment as DashboardFragment).warehouseName =it.name
                tv_warehouse_name_piechart.text = (parentFragment as DashboardFragment).warehouseName

                this.refresh(selectedYear, it.id)
            }
            dialogFragment.show(childFragmentManager, "dialog")
        }

        /*iv_delete_warehouse_piechart.setOnClickListener {
            tv_warehouse_name_piechart.text = context?.getString(R.string.txt_warehouse)
            iv_delete_warehouse_piechart.gone()
            (parentFragment as DashboardFragment).idWarehouse = ""
            (parentFragment as DashboardFragment).warehouseName = null
        }*/
        viewModel.requestPieChartData("$selectedYear-${month+1}", warehouseId)
    }

    private fun setUpPieChart() {
        setDataPieChart()
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.setDrawRoundedSlices(true)
        totalTransaksi = totClosed + totPending + totReserved
        pieChart.centerText = generateCenterSpannableText(totalTransaksi)

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        pieChart.setDrawCenterText(true)

        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.legend.isEnabled = false
    }

    private fun setDataPieChart() {

        val entries = ArrayList<PieEntry>()
        val totalTransaksi = totClosed + totPending + totReserved
        entries.add(PieEntry((totClosed/totalTransaksi).toFloat(),""))
        entries.add(PieEntry((totReserved/totalTransaksi).toFloat(),""))
        entries.add(PieEntry((totPending/totalTransaksi).toFloat(),""))
        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(true)

        dataSet.sliceSpace = 10f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colorChart=intArrayOf (
            Color.rgb(247, 37, 100),
            Color.rgb(28, 168, 100),
            Color.rgb(37, 206, 217)
        )

        val colors = ArrayList<Int>()

        for (c in colorChart) colors.add(c)

        dataSet.colors = colors

        dataSet.selectionShift = 0f
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()
    }

    private fun setupRecycleView() {
        adapter = ListLegendDashboardAdapter(fragmentActivity = activity)
        adapter.updateLegendData(listStatus,listJumlah,listImage)
        rv_legend_dashboard?.layoutManager = LinearLayoutManager(this.context)
        rv_legend_dashboard?.setHasFixedSize(false)
        rv_legend_dashboard?.adapter = adapter
    }

    private fun generateCenterSpannableText(totalTransaksi: Double): SpannableString? {
        val s = SpannableString("Total Transaksi\n $totalTransaksi")
        s.setSpan(RelativeSizeSpan(1.5f), 0, 15, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 15, s.length, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 15, s.length, 0)
        s.setSpan(RelativeSizeSpan(1.1f), 15, s.length, 0)
        return s
    }

    fun refresh(selectedYear: Int, warehouse_id: String){
        if (::viewModel.isInitialized) {
            viewModel.requestPieChartData("$selectedYear-${month+1}", warehouse_id)
        }
    }
}
