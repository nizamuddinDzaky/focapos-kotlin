package id.sisi.postoko.view.ui.purchase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseFragment
import id.sisi.postoko.view.pager.PurchasePagerAdapter
import id.sisi.postoko.view.pager.SalesBookingPagerAdapter
import id.sisi.postoko.view.ui.addsales.AddSaleActivity
import kotlinx.android.synthetic.main.fragment_root_purchase.*
import kotlinx.android.synthetic.main.fragment_root_purchase.fb_add_transaction
import kotlinx.android.synthetic.main.fragment_root_purchase.main_view_pager
import kotlinx.android.synthetic.main.fragment_root_purchase.tabs_main_pagers
import kotlinx.android.synthetic.main.fragment_root_sales_booking.*


class PurchasesRootFragment: BaseFragment() {
    override var tagName: String
        get() = "Pemesanan"
        set(_) {}

    companion object {
        val TAG: String = PurchasesRootFragment::class.java.simpleName
        fun newInstance() =
            PurchasesRootFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_root_purchase, container, false)
    }

    private val pages = listOf(
        PurchaseFragment(PurchaseStatus.PENDING),
        PurchaseFragment(PurchaseStatus.RECEIVED),
        PurchaseFragment(PurchaseStatus.RETURNED)
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        main_view_pager?.let {
            it.adapter = PurchasePagerAdapter(childFragmentManager, pages)
            tabs_main_pagers?.setupWithViewPager(it)
        }

        fb_add_transaction.setOnClickListener {
            startActivityForResult(Intent(this.context, AddPurchaseActivity::class.java), 2030)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2030) {
            val saleStatus = data?.getStringExtra("purchase_status")
            var activeFragment = 0
            if(saleStatus == "received"){
                activeFragment = 1
            }
            main_view_pager.currentItem = activeFragment
            (main_view_pager?.adapter as? PurchasePagerAdapter)?.getItem(activeFragment)
                ?.onActivityResult(requestCode, resultCode, data)
        }
    }
}