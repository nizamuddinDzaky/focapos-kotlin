package id.sisi.postoko.view.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListSalesAdapter
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_sales_booking.*

class SalesBookingFragment : BaseFragment() {

    companion object {
        fun newInstance() = SalesBookingFragment()
    }

    private lateinit var viewModel: SalesBookingViewModel
    private lateinit var adapter: ListSalesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sales_booking, container, false)
    }

    override var tagName: String
        get() = "Menunggu"
        set(value) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()

        viewModel = ViewModelProvider(this).get(SalesBookingViewModel::class.java)
        viewModel.getListSales().observe(viewLifecycleOwner, Observer {
            logE("cek data ${it}")
            adapter.updateSalesData(it)
        })
    }

    private fun setupUI() {
        setupRecycleView()
    }

    private fun setupRecycleView() {
        adapter = ListSalesAdapter()
        rv_list_sales?.layoutManager = LinearLayoutManager(this.context)
        rv_list_sales?.setHasFixedSize(false)
        rv_list_sales?.adapter = adapter
    }
}
