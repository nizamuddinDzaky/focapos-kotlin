package id.sisi.postoko.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.AddProductActivity
import id.sisi.postoko.view.pager.HomePagerAdapter
import id.sisi.postoko.view.pager.SalesPagerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.fb_add_transaction
import kotlinx.android.synthetic.main.fragment_home.main_container
import kotlinx.android.synthetic.main.fragment_home.main_view_pager
import kotlinx.android.synthetic.main.fragment_home.tabs_main_pagers
import kotlinx.android.synthetic.main.fragment_root_sales.*


class MasterDataFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Master Data"
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            startActivity(Intent(this.context, AddProductActivity::class.java))
        }
        main_container?.gone()
        main_view_pager?.let {
            it.adapter = HomePagerAdapter(childFragmentManager)
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logE("masuk 1 ${(main_view_pager?.adapter as? HomePagerAdapter)?.getCurrentFragment()}")
        (main_view_pager?.adapter as? HomePagerAdapter)?.getCurrentFragment()
            ?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val TAG: String = MasterDataFragment::class.java.simpleName
        fun newInstance() = MasterDataFragment()
    }
}