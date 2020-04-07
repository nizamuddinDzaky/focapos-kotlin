package id.sisi.postoko.view.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.view.ui.sales.SaleBookingViewModel

class PiechartFactory(private val mParam: String) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PiechartViewModel() as T
    }
}