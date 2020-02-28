package id.sisi.postoko.view.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Product
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class ProductViewModel : ViewModel() {
    private val suppliers = MutableLiveData<List<Product>?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListProduct()
    }

    private fun getListProduct() {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListProduct(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
                suppliers.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        suppliers.postValue(response.body()?.data?.list_products)
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

    internal fun getListProducts(): LiveData<List<Product>?> {
        return suppliers
    }
}
