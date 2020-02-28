package id.sisi.postoko.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListOrderAdapter
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_menu_confirmation_delivery)
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_list_order?.layoutManager = LinearLayoutManager(this.context)
        rv_list_order?.setHasFixedSize(false)
        rv_list_order?.adapter = ListOrderAdapter()
    }

    companion object {
        val TAG: String = HistoryFragment::class.java.simpleName
        fun newInstance() = HistoryFragment()
    }
}