package id.sisi.postoko.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.sisi.postoko.view.GoodReceived

class GoodReceivedPagerAdapter (fm: FragmentManager):
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val pages = listOf(
            GoodReceived(),
            GoodReceived()
        )

        override fun getItem(position: Int): Fragment {
            return pages[position]
        }

        override fun getCount(): Int {
            return pages.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                1 -> "PENGIRIMAN"
                else -> "DITERIMA"
            }
        }
}