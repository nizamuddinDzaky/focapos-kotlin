package id.sisi.postoko.view.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AddPaymentFactory(private val mParam: Int) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddPaymentViewModel(mParam) as T
    }
}