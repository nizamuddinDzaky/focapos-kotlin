package id.sisi.postoko.view.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SaleBookingFactory(private val mParam: Int) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SaleBookingViewModel(mParam) as T
    }
}