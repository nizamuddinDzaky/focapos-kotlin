package id.sisi.postoko.view.ui.gr

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AddGoodReceivedFactory(private val mParam: Int, private val fa: FragmentActivity? = null) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddGoodReceivedViewModel(mParam, fa) as T
    }
}