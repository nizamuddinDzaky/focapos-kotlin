package id.sisi.postoko.view.ui.customergroup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.MyPopupMenu
import id.sisi.postoko.utils.extensions.visible
import kotlinx.android.synthetic.main.list_item_master.view.*

class ListCartCGToCustomerAdapter<T>(
    private var masterData: MutableList<T>? = mutableListOf(),
    private var fragmentActivity: FragmentActivity? = null
) :
    RecyclerView.Adapter<ListCartCGToCustomerAdapter.MasterViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_master, parent, false)

        return MasterViewHolder(view)
    }

    override fun getItemCount() = masterData?.size ?: 0

    override fun onBindViewHolder(holder: MasterViewHolder<T>, position: Int) {
        holder.bind(masterData?.get(position), this)
    }

    class MasterViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(value: T?, adapter: ListCartCGToCustomerAdapter<T>) {
            when (value) {
                is Customer -> {
                    itemView.tv_master_data_name?.text = value.name
                    itemView.tv_master_data_description?.text = value.address
                    //itemView.btn_menu_more?.setBackgroundResource(R.drawable.ic_close_black_24dp)
                    itemView.btn_menu_more?.setImageResource(R.drawable.ic_close_black_24dp)
                    itemView.btn_menu_more?.visible()
                    itemView.btn_menu_more?.setOnClickListener {
                        MyPopupMenu(
                            it,
                            mutableListOf("Hapus?"),
                            mutableListOf({ adapter.removeCustomerFromCart(value) }),
                            highlight = itemView
                        ).show()
                    }
                    itemView.setOnClickListener {
                        itemView.btn_menu_more?.performClick()
                        //adapter.removeCustomerFromCart(value)
                    }
                }
            }
        }
    }

    fun removeCustomerFromCart(value: T) {
        masterData?.remove(value)
        (fragmentActivity as? AddCustomerToCustomerGoupActivity)?.countDec(value as Customer)
        notifyDataSetChanged()
    }

    fun addData(value: T) {
        masterData?.add(value)
        notifyDataSetChanged()
    }

    fun updateMasterData(newMasterData: List<T>?) {
        masterData = newMasterData?.toMutableList()
        notifyDataSetChanged()
    }
}