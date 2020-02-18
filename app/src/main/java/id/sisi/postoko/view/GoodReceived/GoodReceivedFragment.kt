package id.sisi.postoko.view.GoodReceived

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.view.AddProductActivity
import id.sisi.postoko.view.pager.GoodReceivedPagerAdapter
import kotlinx.android.synthetic.main.good_received.*

class GoodReceivedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_purchase)
        val view = inflater.inflate(R.layout.good_received, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            startActivity(Intent(this.context, AddProductActivity::class.java))
        }
        main_container?.gone()
        main_view_pager?.let {
            it.adapter = GoodReceivedPagerAdapter(childFragmentManager)
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }

    companion object {
        val TAG: String = GoodReceivedFragment::class.java.simpleName
        fun newInstance() =
            GoodReceivedFragment()
    }   
}