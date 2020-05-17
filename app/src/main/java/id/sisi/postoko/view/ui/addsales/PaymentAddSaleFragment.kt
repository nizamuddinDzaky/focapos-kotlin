package id.sisi.postoko.view.ui.addsales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.sisi.postoko.R

class PaymentAddSaleFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = null
        (activity as AddSaleActivity?)?.currentFragmentTag = TAG
        return inflater.inflate(R.layout.payment_add_sale_fragment, container, false)
    }

    companion object {
        val TAG: String = PaymentAddSaleFragment::class.java.simpleName
        fun newInstance() =
            PaymentAddSaleFragment()
    }
}