package id.sisi.postoko.view.ui.goodreveived

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListDetailProductGoodReceivedAdapter
import kotlinx.android.synthetic.main.detail_good_received_fragment.*

class DetailGoodReceivedFragment : Fragment() {
    companion object {
        fun newInstance() =
            DetailGoodReceivedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = getString(R.string.txt_title_detail_good_receive)
        return inflater.inflate(R.layout.detail_good_received_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_list_product_detail_good_received?.layoutManager = LinearLayoutManager(this.context)
        rv_list_product_detail_good_received?.setHasFixedSize(false)
        rv_list_product_detail_good_received?.adapter = ListDetailProductGoodReceivedAdapter()
    }
}