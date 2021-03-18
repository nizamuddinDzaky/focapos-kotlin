package id.sisi.postoko.view.ui.purchase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.pager.PurchasePagerAdapter
import id.sisi.postoko.view.ui.gr.GoodReceivedRootFragment
import kotlinx.android.synthetic.main.fragment_root_sales_booking.*

class PurchaseRootFragment: Fragment() {
    private val pages = listOf(
        GoodReceivedRootFragment.newInstance(),
        PurchasesRootFragment.newInstance()
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.title = getString(R.string.txt_good_receive)
        return inflater.inflate(R.layout.fragment_root_sales, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        main_container?.gone()
        main_view_pager?.let {
            it.adapter = PurchasePagerAdapter(childFragmentManager, pages)
            tabs_main_pagers?.setupWithViewPager(it)
        }

        main_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> activity?.title = getString(R.string.txt_good_receive)
                    1 -> activity?.title = getString(R.string.txt_purchase)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        logE("requestCode : $requestCode, $resultCode")
    }

    companion object {
        val TAG: String = PurchaseRootFragment::class.java.simpleName
        fun newInstance() =
            PurchaseRootFragment()
    }
}