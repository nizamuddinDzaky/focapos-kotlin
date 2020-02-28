package id.sisi.postoko.view.ui.gr

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
import kotlinx.android.synthetic.main.fragment_root_good_received.*

class GoodReceivedRootFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_purchase)
        return inflater.inflate(R.layout.fragment_root_good_received, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            startActivity(Intent(this.context, AddProductActivity::class.java))
        }
        main_container?.gone()
        main_view_pager?.let {
            it.adapter = GoodReceivedPagerAdapter(childFragmentManager, context)
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }

    companion object {
        val TAG: String = GoodReceivedRootFragment::class.java.simpleName
        fun newInstance() =
            GoodReceivedRootFragment()
    }   
}