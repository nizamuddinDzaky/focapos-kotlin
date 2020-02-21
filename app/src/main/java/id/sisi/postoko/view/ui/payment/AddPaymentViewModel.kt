package id.sisi.postoko.view.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Payment
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class AddPaymentViewModel(var id_sales_booking: Int) : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()

    fun postAddPayment(body: Map<String, String>, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_sales_booking" to id_sales_booking.toString())
        ApiServices.getInstance()?.postAddPayment(headers, params, body)?.exe(
            onFailure = { call, throwable ->
                logE("gagal")
                isExecute.postValue(true)
            },
            onResponse = { call, response ->
                logE("berhasil product")
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener()
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
}
