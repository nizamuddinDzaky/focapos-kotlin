package id.sisi.postoko.view.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Customer
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class MasterDetailViewModel : ViewModel() {
    private val customer = MutableLiveData<Customer>()
    private var isExecute = MutableLiveData<Boolean>()

    internal fun requestDetailCustomer(idCustomer: Int) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customers" to idCustomer.toString())
        ApiServices.getInstance()?.getDetailCustomer(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
                customer.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        customer.postValue(response.body()?.data?.customer)
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

    internal fun getDetailCustomer(): LiveData<Customer?> {
        return customer
    }
}
