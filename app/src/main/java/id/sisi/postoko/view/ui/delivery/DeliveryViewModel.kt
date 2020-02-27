package id.sisi.postoko.view.ui.delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Delivery
import id.sisi.postoko.model.Payment
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class DeliveryViewModel(var id_sales_booking: Int) : ViewModel() {
    private val deliveries = MutableLiveData<List<Delivery>?>()
    private val delivery = MutableLiveData<Delivery?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListDelivery()
    }

    fun requestDetailDelivery(idDelivery: Int) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_sales_delivery" to idDelivery.toString())
        ApiServices.getInstance()?.getDetailSaleDelivery(headers, params)?.exe(
            onFailure = { call, throwable ->
                logE("gagal")
                isExecute.postValue(true)
                delivery.postValue(null)
            },
            onResponse = { call, response ->
                logE("berhasil product")
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        delivery.postValue(response.body()?.data?.delivery)
                    }
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun getListDelivery() {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_sales_booking" to id_sales_booking.toString())
        ApiServices.getInstance()?.getListSaleDelivery(headers, params)?.exe(
            onFailure = { call, throwable ->
                logE("gagal")
                isExecute.postValue(true)
                deliveries.postValue(null)
            },
            onResponse = { call, response ->
                logE("berhasil product")
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

    internal fun getDetailDelivery() = delivery
}
