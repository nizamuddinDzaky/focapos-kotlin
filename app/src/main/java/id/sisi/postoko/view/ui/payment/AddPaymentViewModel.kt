package id.sisi.postoko.view.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_PAYMENT
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.TXT_CONNECTION_FAILED
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.helper.json2obj

class AddPaymentViewModel(private var idSalesBooking: Int) : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()

    fun postAddPayment(body: Map<String, String>, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to idSalesBooking.toString())
        ApiServices.getInstance()?.postAddPayment(headers, params, body)?.exe(
            onFailure = { _, _ ->
                message.postValue(TXT_CONNECTION_FAILED)
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    message.postValue(response.body()?.message)
                    listener()
                } else {
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    message.postValue(errorResponse?.message)
                    listener()
                }
            }
        )
    }

    fun putEditayment(body: Map<String, String>, id_payement: String, listener: () -> Unit) {
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
                    message.postValue(response.body()?.message)
                    listener()
                } else {
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

    internal fun getMessage() = message
}
