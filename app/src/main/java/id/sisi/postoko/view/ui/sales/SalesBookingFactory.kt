package id.sisi.postoko.view.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SalesBookingFactory(private val mParam: String) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SalesBookingViewModel(mParam) as T
    }
}