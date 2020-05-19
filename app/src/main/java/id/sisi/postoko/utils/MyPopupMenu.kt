package id.sisi.postoko.utils

import android.graphics.Color
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu

class MyPopupMenu(
    view: View,
    listMenu: MutableList<String>,
    listAction: MutableList<() -> Unit?>,
    onDismiss: () -> Unit = {},
    firstInit: () -> Unit = {},
    var highlight: View? = null
) {
    private var popup: PopupMenu = PopupMenu(view.context, view)

    init {
        listMenu.forEachIndexed { index, s ->
            popup.menu.add(Menu.NONE, index, Menu.NONE, s)
        }

        popup.setOnMenuItemClickListener { item: MenuItem? ->
            if (listAction.isNotEmpty() && item?.itemId != null) {
                listAction[item.itemId]()
            }
            true
        }

        popup.setOnDismissListener {
            this.onDismiss()
            onDismiss()
        }

        highlight?.setBackgroundColor(Color.LTGRAY)
        firstInit()
    }

    private fun onDismiss() {
        val outValue = TypedValue()
        highlight?.context?.theme?.resolveAttribute(
            android.R.attr.selectableItemBackground,
            outValue,
            true
        )
        /*val background: ColorDrawable = highlight?.background as ColorDrawable*/

        highlight?.setBackgroundColor(Color.WHITE)
    }

    fun show() {
        popup.show()
    }
}