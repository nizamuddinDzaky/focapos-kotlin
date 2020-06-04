package id.sisi.postoko.view.ui.delivery

import android.text.TextUtils
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
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DeliveryDetailViewModel : ViewModel() {
    private val delivery = MutableLiveData<Delivery?>()
    private var isExecute = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()

    fun requestDetailDelivery(idDelivery: Int) {
        logE("masuk1")
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

    fun putEditDeliv(body: Map<String, Any?>,  idDelivery: String, file: MultipartBody.Part?, listener: () -> Unit) {
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
                        if (file != null){
                            postUploadFile(file, response.body()?.data?.delivery?.id ?: "0"){messageUpload ->
                                message.postValue(messageUpload)
                                isExecute.postValue(false)
                                listener()
                            }
                        }else{
                            isExecute.postValue(false)
                            listener()
                        }
                    }
                } else {
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    if (TextUtils.isEmpty(errorResponse?.message)){
                        message.postValue(TXT_URL_NOT_FOUND)
                    }else
                        message.postValue(errorResponse?.message)
                }
            }
        )
    }

    fun postReturnDeliv(body: MutableMap<String, Any?>, idDelivery: String, file: MultipartBody.Part?, listener: () -> Unit) {
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
                        if (file != null){
                            postUploadFile(file, response.body()?.data?.delivery?.id ?: "0"){messageUpload ->
                                message.postValue(messageUpload)
                                isExecute.postValue(false)
                                listener()
                            }
                        }else{
                            isExecute.postValue(false)
                            listener()
                        }
                    }
                } else {
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    if (TextUtils.isEmpty(errorResponse?.message)){
                        message.postValue(TXT_URL_NOT_FOUND)
                    }else
                        message.postValue(errorResponse?.message)
                }
            }
        )
    }

    fun postAddDelivery(idSalesBooking: Int, body: Map<String, Any?>, file: MultipartBody.Part?, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to idSalesBooking.toString())
        ApiServices.getInstance()?.postAddDelivery(headers, params, body)?.exe(
            onFailure = { _, _ ->
                message.postValue(TXT_CONNECTION_FAILED)
                isExecute.postValue(false)
            },
            onResponse = { _, response ->

                if (response.isSuccessful) {
                    tryMe {
                        message.postValue(response.body()?.message)
                        if (file != null){
                            logE("${response.body()?.data}")
                            postUploadFile(file, response.body()?.data?.delivery?.id ?: "0"){messageUpload ->
                                message.postValue(messageUpload)
                                isExecute.postValue(false)
                                listener()
                            }
                        }else{
                            isExecute.postValue(false)
                            listener()
                        }
                        /*listener()*/
                    }
                } else {
                    isExecute.postValue(false)
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    if (TextUtils.isEmpty(errorResponse?.message)){
                        message.postValue(TXT_URL_NOT_FOUND)
                    }else
                        message.postValue(errorResponse?.message)
                }
            }
        )
    }

    private fun postUploadFile(body: MultipartBody.Part, idDelivery: String, listener: (message: String) -> Unit) {
        isExecute.postValue(true)
         val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
         val params = mutableMapOf(KEY_ID_DELIVERY to idDelivery)
        ApiServices.getInstance()?.postUploadFileDelivery(body, headers, params)?.exe(
            onFailure = { _, t->
                logE("gagal: $t")
                listener(t.toString())
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        logE("berhasil: ${response.body()?.message}")
                        message.postValue(response.body()?.message)
                        listener(response.body()?.message.toString())
                    }
                } else {
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    if (TextUtils.isEmpty(errorResponse?.message)){
                        listener(TXT_URL_NOT_FOUND)
                    }else
                        listener(errorResponse?.message.toString())
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        return isExecute
    }

    internal fun getDetailDelivery(): MutableLiveData<Delivery?>{
        return delivery
    }

    internal fun getMessage() = message
}