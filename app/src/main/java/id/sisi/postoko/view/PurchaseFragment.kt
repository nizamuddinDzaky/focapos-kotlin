package id.sisi.postoko.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.sisi.postoko.R

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

    companion object {
        val TAG: String = PurchaseFragment::class.java.simpleName
        fun newInstance() = PurchaseFragment()
    }
}