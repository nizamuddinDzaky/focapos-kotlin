package id.sisi.postoko.view.ui.supplier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Supplier
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class SupplierViewModel : ViewModel() {
    private val suppliers = MutableLiveData<List<Supplier>?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListSupplier()
    }

    private fun getListSupplier() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListSupplier(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
                suppliers.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        suppliers.postValue(response.body()?.data?.list_suppliers)
                    }
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        isExecute.postValue(true)
        return isExecute
    }

    internal fun getListSuppliers(): LiveData<List<Supplier>?> {
        return suppliers
    }
}
