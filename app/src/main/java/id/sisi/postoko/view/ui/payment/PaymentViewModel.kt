package id.sisi.postoko.view.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Payment
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class PaymentViewModel(private var idSalesBooking: Int) : ViewModel() {
    private val payments = MutableLiveData<List<Payment>?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListPayment()
    }

    fun getListPayment() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to idSalesBooking.toString())
        ApiServices.getInstance()?.getListSalePayment(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                payments.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        payments.postValue(response.body()?.data?.list_payments)
                    }
                } else {
                    isExecute.postValue(false)
                    payments.postValue(listOf())
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
//        isExecute.postValue(true)
        return isExecute
    }

    internal fun getListPayments(): LiveData<List<Payment>?> {
        return payments
    }
}
