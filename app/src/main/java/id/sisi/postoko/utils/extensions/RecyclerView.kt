package id.sisi.postoko.utils.extensions

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addVerticalDivider() {
    addItemDecoration(
        DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )
    )
}