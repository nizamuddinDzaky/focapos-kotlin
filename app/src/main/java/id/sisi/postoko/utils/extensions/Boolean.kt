package id.sisi.postoko.utils.extensions

import android.view.View

fun Boolean.visibleOrGone(view: View) {
    if (this) view.visible() else view.gone()
}