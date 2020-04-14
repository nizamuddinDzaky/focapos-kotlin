package id.sisi.postoko.view.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.exe

class AddPaymentViewModel(private var idSalesBooking: Int) : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()

    fun postAddPayment(body: Map<String, String>, listener: (Map<String, Any>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to idSalesBooking.toString())
        ApiServices.getInstance()?.postAddPayment(headers, params, body)?.exe(
            onFailure = { _, _ ->
                listener(mapOf("networkRespone" to NetworkResponse.FAILURE, "message" to "koneksi gagal"))
                isExecute.postValue(true)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener(mapOf("networkRespone" to NetworkResponse.SUCCESS, "message" to response.message()))
                } else {
                    listener(mapOf("networkRespone" to NetworkResponse.ERROR, "message" to response.message()))
                    isExecute.postValue(true)
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        isExecute.postValue(true)
        return isExecute
    }
}
