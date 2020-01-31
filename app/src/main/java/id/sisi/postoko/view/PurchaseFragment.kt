package id.sisi.postoko.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListProductAdapter
import kotlinx.android.synthetic.main.fragment_purchase.*

class PurchaseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_purchase)
        val view = inflater.inflate(R.layout.fragment_purchase, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_list_product?.layoutManager = GridLayoutManager(this.context, 2)
        rv_list_product?.setHasFixedSize(false)
        rv_list_product?.adapter = ListProductAdapter()
    }

    companion object {
        val TAG: String = PurchaseFragment::class.java.simpleName
        fun newInstance() = PurchaseFragment()
    }
}