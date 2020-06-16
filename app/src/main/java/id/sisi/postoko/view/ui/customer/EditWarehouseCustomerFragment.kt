package id.sisi.postoko.view.ui.customer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.sisi.postoko.R
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit_warehouse_customer.view.*

class EditWarehouseCustomerFragment : BaseFragment() {

    companion object {
        fun newInstance() = EditWarehouseCustomerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override var tagName: String
        get() = "Gudang"
        set(_) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_edit_warehouse_customer, container, false)

        view.add_warehouse.setOnClickListener {
            val intent = Intent(context, EditCustomerWarehouseActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
