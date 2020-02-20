package id.sisi.postoko.view.ui.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class SaleBookingViewModel(var id: Int) : ViewModel() {
    private val sale = MutableLiveData<Sales?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getDetailSale()
    }

    private fun getDetailSale() {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_sales_booking" to id.toString())
        ApiServices.getInstance()?.getDetailSale(headers, params)?.exe(
            onFailure = { call, throwable ->
                logE("gagal")
                isExecute.postValue(true)
                sale.postValue(null)
            },
            onResponse = { call, response ->
                logE("berhasil product")
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        val newSale = response.body()?.data?.sale
                        newSale?.saleItems = response.body()?.data?.sale_items
                        sale.postValue(newSale)
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

    internal fun getDetail(): LiveData<Sales?> {
        return sale
    }
}
