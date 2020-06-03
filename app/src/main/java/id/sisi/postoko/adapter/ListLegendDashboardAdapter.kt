package id.sisi.postoko.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toDisplayStatus
import kotlinx.android.synthetic.main.list_legend_dashboard.view.*

class ListLegendDashboardAdapter (
        private var status: List<Int>? = arrayListOf(),
        private var jumlah: List<String>? = arrayListOf(),
        private var image: List<Int>? = arrayListOf(),
        private var fragmentActivity: FragmentActivity? = null
    ) : RecyclerView.Adapter<ListLegendDashboardAdapter.LegendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegendViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_legend_dashboard, parent, false)

        return LegendViewHolder(view)
    }

    override fun getItemCount(): Int {
        return status?.size ?: 0
    }

    override fun onBindViewHolder(holder: LegendViewHolder, position: Int) {
        holder.bind(status?.get(position), jumlah?.get(position), image?.get(position), fragmentActivity)
    }

    class LegendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeface = TypeFace()
        fun bind(status: Int?, jumlah: String?, image: Int?, fragmentActivity: FragmentActivity? = null) {
            itemView.iv_icon_legend_dashboard.setImageResource(image?:0)
            itemView.tv_status_legend_dashboard.text = status?.let { itemView.context.getText(it) }
            itemView.tv_jumlah_legend_dashboard.text = jumlah
            fragmentActivity?.assets?.let {
                typeface.typeFace("robot_font/Roboto-Bold.ttf",itemView.tv_status_legend_dashboard,it)
                typeface.typeFace("robot_font/Roboto-Regular.ttf",itemView.tv_jumlah_legend_dashboard,it)
            }
        }
    }
    fun updateLegendData(newStatus: List<Int>?, newJumlah: ArrayList<String>?, newImage: ArrayList<Int>?) {
        status = newStatus
        jumlah = newJumlah
        image = newImage
        notifyDataSetChanged()
    }
}