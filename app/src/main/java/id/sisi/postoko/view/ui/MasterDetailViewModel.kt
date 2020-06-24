package id.sisi.postoko.view.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class MasterDetailViewModel : ViewModel() {
    private val customer = MutableLiveData<Customer>()
    private val selectedWarehouse = MutableLiveData<List<Warehouse>>()
    private val defaultWarehouse = MutableLiveData<List<Warehouse>>()
    private var isExecute = MutableLiveData<Boolean>()

    internal fun requestSelectedWarehouse(idCustomer: Int) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customers" to idCustomer.toString())
        ApiServices.getInstance()?.getSelectedWarehouse(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customer.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        selectedWarehouse.postValue(response.body()?.data?.warehouses_selected)
                        defaultWarehouse.postValue(response.body()?.data?.warehouses_default)
                    }
                } else {
                    isExecute.postValue(false)
                }
            }
        )
    }

    internal fun requestDetailCustomer(idCustomer: Int) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customers" to idCustomer.toString())
        ApiServices.getInstance()?.getDetailCustomer(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customer.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        customer.postValue(response.body()?.data?.customer)
                    }
                } else {
                    isExecute.postValue(false)
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
    internal fun getSelectedWarehouse(): LiveData<List<Warehouse>?> {
        return selectedWarehouse
    }
    internal fun getDefaultWarehouse():  LiveData<List<Warehouse>?> {
        return defaultWarehouse
    }
}
