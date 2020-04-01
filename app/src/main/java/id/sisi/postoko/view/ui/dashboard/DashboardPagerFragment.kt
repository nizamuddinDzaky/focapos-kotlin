package id.sisi.postoko.view.ui.dashboard

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListLegendDashboardAdapter
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.ui.sales.SaleStatus
import kotlinx.android.synthetic.main.fragment_dashboard_pager.*


/**
 * A simple [Fragment] subclass.
 */
class DashboardPagerFragment(var status: SaleStatus  = SaleStatus.PENDING) : BaseFragment() {
    companion object {
        fun newInstance() = DashboardPagerFragment()
    }
    private lateinit var adapter: ListLegendDashboardAdapter
    private var listStatus: ArrayList<String> = arrayListOf("Cancel", "Sukses", " Pending")
    private var listImage: ArrayList<Int> = arrayListOf(R.drawable.circle_cancel, R.drawable.circle_sukses, R.drawable.circle_pending)
    private var listJumlah: ArrayList<String> = arrayListOf()
    override lateinit var tagName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard_pager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecycleView()
        listJumlah.add("30 Transaksi")
        listJumlah.add("30 Transaksi")
        listJumlah.add("30 Transaksi")

        setData()
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.setDragDecelerationFrictionCoef(0.95f)
        pieChart.setDrawRoundedSlices(true)
//        pieChart.setCenterTextTypeface(tfLight)
        pieChart.setCenterText(generateCenterSpannableText(21000000.0))

        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        pieChart.setDrawCenterText(true)

        pieChart.setRotationAngle(0f)
        // enable rotation of the pieChart by touch
        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // chart.spin(2000, 0, 360);
        pieChart.getLegend().setEnabled(false)
    }

    private fun setData() {
        val entries = java.util.ArrayList<PieEntry>()
        entries.add(PieEntry(0.3f,""))
        entries.add(PieEntry(0.2f,""))
        entries.add(PieEntry(0.5f,""))
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

        val colors = java.util.ArrayList<Int>()

        for (c in colorChart) colors.add(c)

        dataSet.colors = colors

        dataSet.setSelectionShift(0f)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)
        pieChart.highlightValues(null)
        pieChart.invalidate()
    }

    private fun setupRecycleView() {
        adapter = ListLegendDashboardAdapter()
        adapter.updateLegendData(listStatus,listJumlah,listImage)
        rv_legend_dashboard?.layoutManager = LinearLayoutManager(this.context)
        rv_legend_dashboard?.setHasFixedSize(false)
        rv_legend_dashboard?.adapter = adapter
    }

    private fun generateCenterSpannableText(totalTransaksi: Double): SpannableString? {
        val s = SpannableString("Total Transaksi\n 21000000")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 15, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 15, s.length, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 15, s.length, 0)
        s.setSpan(RelativeSizeSpan(1.1f), 15, s.length, 0)
        return s
    }
    fun getmParam1(): SaleStatus {

        return status
    }
}
