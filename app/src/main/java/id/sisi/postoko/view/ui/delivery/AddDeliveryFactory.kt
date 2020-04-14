package id.sisi.postoko.view.ui.delivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AddDeliveryFactory(private val mParam: Int) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddDeliveryViewModel(mParam) as T
    }
}