package id.sisi.postoko.view.ui.product

import android.os.Bundle
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListPaymentAdapter
import id.sisi.postoko.adapter.QuantityWarehouseAdapter
import id.sisi.postoko.utils.DownloadFile
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_quantity_detail_product.*
import kotlinx.android.synthetic.main.pembayaran_fragment.*

class QuantityProductFragment: BaseFragment() {

    private lateinit var adapter: QuantityWarehouseAdapter

    override var tagName: String
        get() = "Kuantitas Warehouse"
        set(_) {}

    companion object {
        fun newInstance() = DetailProductFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quantity_detail_product, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        (activity as DetailProductActivity).product.observe(viewLifecycleOwner, Observer {
            adapter.updateData(it.warehouse)
        })
    }


    private fun setupUI() {
        setupRecycleView()

    }

    private fun setupRecycleView() {
        adapter = QuantityWarehouseAdapter()
        rv_qty_warehouse?.layoutManager = LinearLayoutManager(this.context)
        rv_qty_warehouse?.setHasFixedSize(false)
        rv_qty_warehouse?.adapter = adapter
    }
}