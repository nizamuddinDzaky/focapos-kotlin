package id.sisi.postoko.view.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.sisi.postoko.R
import id.sisi.postoko.view.BaseFragment

class EditDataCustomer : BaseFragment() {

    companion object {
        fun newInstance() = EditDataCustomer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override var tagName: String
        get() = "Pelanggan"
        set(_) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_data_customer, container, false)
    }
}
