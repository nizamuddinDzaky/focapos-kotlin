package id.sisi.postoko.view.ui.gr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AddGoodReceivedFactory(private val mParam: Int) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddGoodReceivedViewModel(mParam) as T
    }
}