package id.sisi.postoko.utils

import android.content.res.AssetManager
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TabWidget
import android.widget.TextView
import com.google.android.material.tabs.TabLayout

class TypeFace {
    fun typeFace(strFile: String, editText: TextView, asset: AssetManager){
        val typeface = Typeface.createFromAsset(asset, strFile)
        editText.typeface = typeface
    }

    fun fontTab(tab: TabLayout, strFile: String, asset: AssetManager) {
        val vg = tab.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildsCount = vgTab.childCount
            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) { //Put your font in assests folder
//assign name of the font here (Must be case sensitive)
                    typeFace(strFile, tabViewChild, asset)
                }
            }
        }
    }
}