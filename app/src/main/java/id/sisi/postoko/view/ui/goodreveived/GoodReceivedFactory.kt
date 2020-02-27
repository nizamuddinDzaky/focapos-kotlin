package id.sisi.postoko.view.ui.goodreveived

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class GoodReceivedFactory(private val mParam: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GoodReceivedViewModel(mParam) as T
    }
}