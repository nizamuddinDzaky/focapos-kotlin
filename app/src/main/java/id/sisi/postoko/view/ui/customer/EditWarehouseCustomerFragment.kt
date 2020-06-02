package id.sisi.postoko.view.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.sisi.postoko.R
import id.sisi.postoko.view.BaseFragment

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
        return inflater.inflate(R.layout.fragment_edit_warehouse_customer, container, false)
    }
}
