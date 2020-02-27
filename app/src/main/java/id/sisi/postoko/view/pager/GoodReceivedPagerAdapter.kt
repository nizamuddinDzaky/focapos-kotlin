package id.sisi.postoko.view.pager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.utils.extensions.tryValue
import id.sisi.postoko.view.ui.goodreveived.GoodReceiveStatus.DELIVERING
import id.sisi.postoko.view.ui.goodreveived.GoodReceiveStatus.RECEIVED
import id.sisi.postoko.view.ui.goodreveived.GoodReceivedFragment

class GoodReceivedPagerAdapter (fm: FragmentManager, var ctx: Context?) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pages = listOf(
        GoodReceivedFragment(DELIVERING),
        GoodReceivedFragment(RECEIVED)
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ctx?.getString(DELIVERING.tryValue(pages[position].tagName)?.stringId ?: 0)
    }
}