package id.sisi.postoko.view.ui.delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.TXT_CONNECTION_FAILED
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj

class AddDeliveryViewModel(private var idSalesBooking: Int) : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()

    fun postAddDelivery(body: Map<String, Any?>, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to idSalesBooking.toString())
        ApiServices.getInstance()?.postAddDelivery(headers, params, body)?.exe(
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
        return isExecute
    }

    internal fun getMessage() = message
}
