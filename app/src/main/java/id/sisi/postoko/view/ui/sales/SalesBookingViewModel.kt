package id.sisi.postoko.view.ui.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_SALE_STATUS
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class SalesBookingViewModel(var status: String) : ViewModel() {
    private val sales = MutableLiveData<List<Sales>?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListSale()
    }

    fun getListSale() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_SALE_STATUS to status)
        ApiServices.getInstance()?.getListSale(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                sales.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        sales.postValue(response.body()?.data?.list_sales_booking)
                    }
                } else {
                    sales.postValue(listOf())
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        return isExecute
    }

    internal fun getListSales(): LiveData<List<Sales>?> {
        return sales
    }
}
