package id.sisi.postoko.view.ui.delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Delivery
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_DELIVERY
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class DeliveryDetailViewModel : ViewModel() {
    private val delivery = MutableLiveData<Delivery?>()
    private var isExecute = MutableLiveData<Boolean>()

    fun requestDetailDelivery(idDelivery: Int) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_delivery" to idDelivery.toString())
        ApiServices.getInstance()?.getDetailDeliveries(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                delivery.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        val newDeliv = response.body()?.data?.delivery
                        newDeliv?.deliveryItems = response.body()?.data?.delivery_items
                        delivery.postValue(newDeliv)
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

    internal fun getDetailDelivery(): MutableLiveData<Delivery?>{
        return delivery
    }
}