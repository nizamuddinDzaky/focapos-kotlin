package id.sisi.postoko.view.ui.payment

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj
import okhttp3.MultipartBody

class AddPaymentViewModel(private var idSalesBooking: Int) : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()

    fun postAddPayment(body: Map<String, String>, file: MultipartBody.Part?, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to idSalesBooking.toString())
        ApiServices.getInstance()?.postAddPayment(headers, params, body)?.exe(
            onFailure = { _, _ ->
                message.postValue(TXT_CONNECTION_FAILED)
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        message.postValue(response.body()?.message)
                        if (file != null){
                            postUploadFile(file, response.body()?.data?.payment?.id ?: "0"){messageUpload ->
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

    fun putEditayment(body: Map<String, String>, id_payement: String, file: MultipartBody.Part?, listener: () -> Unit) {
        isExecute.postValue(true)

        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PAYMENT to id_payement)

        ApiServices.getInstance()?.putEditPayment(headers, params, body)?.exe(
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
                            postUploadFile(file, response.body()?.data?.payment?.id ?: "0"){messageUpload ->
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
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PAYMENT to idDelivery)
        ApiServices.getInstance()?.postUploadFilePayment(body, headers, params)?.exe(
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

    internal fun getMessage() = message
}
