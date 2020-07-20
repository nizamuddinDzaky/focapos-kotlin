package id.sisi.postoko.view.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sisi.postoko.R
import id.sisi.postoko.view.BaseFragment

class DetailProductFragment: BaseFragment() {
    override var tagName: String
        get() = "Detail Product"
        set(_) {}

    companion object {
        fun newInstance() = DetailProductFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_product, container, false)
        return view
    }
}