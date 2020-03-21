package id.sisi.postoko.view.research

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class GRFactory(private val mParam: HashMap<String, String>) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GRViewModel(mParam) as T
    }
}