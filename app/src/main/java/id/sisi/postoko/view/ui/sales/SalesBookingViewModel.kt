package id.sisi.postoko.view.ui.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Product
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class SalesBookingViewModel : ViewModel() {
    private val sales = MutableLiveData<List<Sales>?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListSale()
    }

    private fun getListSale() {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListSale(headers)?.exe(
            onFailure = { call, throwable ->
                logE("gagal")
                isExecute.postValue(true)
                sales.postValue(null)
            },
            onResponse = { call, response ->
                logE("berhasil product")
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        sales.postValue(response.body()?.data?.list_sales_booking)
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

    internal fun getListSales(): LiveData<List<Sales>?> {
        return sales
    }
}
