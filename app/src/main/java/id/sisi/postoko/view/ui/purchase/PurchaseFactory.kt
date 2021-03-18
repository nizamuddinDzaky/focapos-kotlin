package id.sisi.postoko.view.ui.purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.view.sales.SBViewModel

class PurchaseFactory(private val mParam: HashMap<String, String> ): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PurchasesViewModel(mParam) as T
    }
}