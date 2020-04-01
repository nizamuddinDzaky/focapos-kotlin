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
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Sales
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.list_legend_dashboard.view.*

class ListLegendDashboardAdapter (
        private var status: List<String>? = arrayListOf(),
        private var jumlah: List<String>? = arrayListOf(),
        private var image: List<Int>? = arrayListOf()
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
        holder.bind(status?.get(position), jumlah?.get(position), image?.get(position))
    }

    class LegendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(status: String?, jumlah: String?, image: Int?) {
            itemView.iv_icon_legend_dashboard.setImageResource(image?:0)
            itemView.tv_status_legend_dashboard.text = status
            itemView.tv_jumlah_legend_dashboard.text = jumlah
        }
    }
    fun updateLegendData(newStatus: List<String>?, newJumlah: ArrayList<String>?, newImage: ArrayList<Int>?) {
        status = newStatus
        jumlah = newJumlah
        image = newImage
        notifyDataSetChanged()
    }
}