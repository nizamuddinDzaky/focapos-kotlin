package id.sisi.postoko.view.ui.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.TXT_CONNECTION_FAILED
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj

class AddSalesViewModel : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()
    private var idSalesBooking: Int = 0
    private var message = MutableLiveData<String?>()

    fun postAddSales(body: Map<String, Any?>, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postAddSales(headers, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                message.postValue(TXT_CONNECTION_FAILED)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        message.postValue(response.body()?.message)
                        listener()
                    }
                } else {
                    isExecute.postValue(false)
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    message.postValue(errorResponse?.message)
                    listener()
                }
            }
        )
    }

    fun postEditSale(body: Map<String, Any?>, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to idSalesBooking.toString())
        ApiServices.getInstance()?.putEditSales(headers, params, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                message.postValue(TXT_CONNECTION_FAILED)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        message.postValue(response.body()?.message)
                        listener()
                    }
                } else {
                    isExecute.postValue(false)
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    message.postValue(errorResponse?.message)
                    listener()
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        return isExecute
    }

    fun setIdSalesBooking(idSalesBooking : Int){
        this.idSalesBooking = idSalesBooking
    }

    internal fun getMessage() = message
}
