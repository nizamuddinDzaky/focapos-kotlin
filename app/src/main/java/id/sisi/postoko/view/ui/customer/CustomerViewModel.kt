package id.sisi.postoko.view.ui.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Customer
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class CustomerViewModel : ViewModel() {
    private val customers = MutableLiveData<List<Customer>?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListCustomer()
    }

    private fun getListCustomer() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListCustomer(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
                customers.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        customers.postValue(response.body()?.data?.list_customers)
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

    internal fun getListCustomers(): LiveData<List<Customer>?> {
        return customers
    }
}
