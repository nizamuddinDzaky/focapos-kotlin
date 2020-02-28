package id.sisi.postoko.view.ui.delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Delivery
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class DeliveryViewModel(private var idSalesBooking: Int) : ViewModel() {
    private val deliveries = MutableLiveData<List<Delivery>?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListDelivery()
    }

    fun getListDelivery() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to idSalesBooking.toString())
        ApiServices.getInstance()?.getListSaleDelivery(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
                deliveries.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        deliveries.postValue(response.body()?.data?.list_deliveries_booking)
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

    internal fun getListDeliveries(): LiveData<List<Delivery>?> {
        return deliveries
    }

}
