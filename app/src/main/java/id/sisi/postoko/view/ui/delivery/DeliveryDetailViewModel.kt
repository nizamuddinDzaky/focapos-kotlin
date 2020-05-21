package id.sisi.postoko.view.ui.delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.model.Delivery
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj

class DeliveryDetailViewModel : ViewModel() {
    private val delivery = MutableLiveData<Delivery?>()
    private var isExecute = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()

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

    fun putEditDeliv(body: Map<String, Any?>,  idDelivery: String, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_DELIVERY_BOOKING to idDelivery)
        ApiServices.getInstance()?.putEditDeliv(headers, params, body)?.exe(
            onFailure = { _, _ ->
                message.postValue(TXT_CONNECTION_FAILED)
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        message.postValue(response.body()?.message)
                        listener()
                    }
                } else {
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    message.postValue(errorResponse?.message)
                }
            }
        )
    }

    fun postReturnDeliv(body: MutableMap<String, Any?>, idDelivery: String, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_DELIVERY_BOOKING to idDelivery)
        ApiServices.getInstance()?.postReturnDeliv(headers, params, body)?.exe(
            onFailure = { _, _ ->
                message.postValue(TXT_CONNECTION_FAILED)
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        message.postValue(response.body()?.message)
                        listener()
                    }
                } else {
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    message.postValue(errorResponse?.message)
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

    internal fun getMessage() = message
}