package id.sisi.postoko.view.ui.goodreveived

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AddGoodReceivedFactory(private val mParam: Int) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddGoodReceivedViewModel(mParam) as T
    }
}