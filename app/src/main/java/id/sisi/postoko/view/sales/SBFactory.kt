package id.sisi.postoko.view.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SBFactory(private val mParam: HashMap<String, String>, private var isAksestoko: Boolean) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SBViewModel(mParam, isAksestoko) as T
    }
}