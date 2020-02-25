package id.sisi.postoko.view.ui.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SimpleAdapter
import androidx.fragment.app.DialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.KEY_CUSTOMER_DATA
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.fragment_search_dialog.*

class CustomerSearchDialogFragment(var listener: () -> Unit? = {}) : DialogFragment() {
    private var customers: List<Customer>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customers = arguments?.getParcelableArrayList(KEY_CUSTOMER_DATA) ?: arrayListOf()

        sv_search_dialog?.queryHint = "Cari Pelanggan"
        sv_search_dialog?.onActionViewExpanded()
        sv_search_dialog?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (!p0.isNullOrEmpty() && p0.length > 2) {
                    startSearchData(p0)
                }
                return true
            }

        })
        btn_dismiss_dialog?.setOnClickListener {
            dismiss()
        }
    }

    private fun startSearchData(query: String) {
        customers?.let {
            val listSearchResult = customers!!.filter {
                it.name.contains(query, true) or it.address.contains(query, true)
            }
            val listData = listSearchResult.map {
                return@map mutableMapOf("name" to it.name, "address" to it.address)
            }
            putToListView(listData)
        }
    }

    private fun putToListView(listData: List<Map<String, String>>) {
        context?.let {
            val adapter = SimpleAdapter(
                this.context,
                listData,
                android.R.layout.simple_list_item_2,
                arrayOf("name", "address"),
                arrayOf(android.R.id.text1, android.R.id.text2).toIntArray()
            )
            lv_search_dialog?.adapter = adapter
            lv_search_dialog?.setOnItemClickListener { adapterView, view, i, l ->
                logE("cek data ${listData[i]}")
                listener()
            }
        }
    }
}