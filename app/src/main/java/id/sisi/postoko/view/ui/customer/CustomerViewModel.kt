package id.sisi.postoko.view.ui.customer

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.model.DataSyncCustomerToBK
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_CUSTOMER
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.view.HomeActivity

class CustomerViewModel : ViewModel() {
    private val customers = MutableLiveData<List<Customer>?>()
    private val customersGroup = MutableLiveData<List<CustomerGroup>?>()
    private val priceGroup = MutableLiveData<List<PriceGroup>?>()
    private var isExecute = MutableLiveData<Boolean>()
    private var idCustomer: String? = null

    private var statusSyncCustomerToBK = MutableLiveData<DataSyncCustomerToBK>()

    fun getListCustomer() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListCustomer(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customers.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        customers.postValue(response.body()?.data?.list_customers)
                    }
                } else {
                    isExecute.postValue(false)
                    customers.postValue(listOf())
                }
            }
        )
    }

    fun regSyncCustomerToBK() {
        var body: Map<String, Any?> = mutableMapOf()

        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postSyncCustomerToBK(headers, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                statusSyncCustomerToBK.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    isExecute.postValue(true)
                    statusSyncCustomerToBK.postValue(response.body()?.data)
                } else {
                    isExecute.postValue(false)
                    statusSyncCustomerToBK.postValue(null)
                }
            }
        )
    }

    fun getListCustomerGroup(){
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListCustomerGroup(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customersGroup.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        customersGroup.postValue(response.body()?.data?.customer_groups)
                    }
                } else {
                    isExecute.postValue(false)
                    customersGroup.postValue(listOf())
                }
            }
        )
    }

    fun getListPriceGroup(){
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListPriceGroup(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                priceGroup.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        priceGroup.postValue(response.body()?.data?.price_groups)
                    }
                } else {
                    isExecute.postValue(false)
                    priceGroup.postValue(listOf())
                }
            }
        )
    }

    fun postAddCustomer(body: Map<String, Any?>, listener: (Map<String, Any?>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postCustomers(headers, body)?.exe(
            onFailure = { _, _ ->
                listener(mapOf("networkRespone" to NetworkResponse.FAILURE, "message" to "koneksi gagal"))
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    isExecute.postValue(true)
                    listener(mapOf("networkRespone" to NetworkResponse.SUCCESS, "message" to response.body()?.message))
                } else {
                    isExecute.postValue(false)
                    listener(mapOf("networkRespone" to NetworkResponse.ERROR, "message" to response.body()?.message))
                }
            }
        )
    }

    fun postEditSale(body: Map<String, Any?>, listener: (Map<String, Any>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customer" to idCustomer.toString())
        ApiServices.getInstance()?.putEditCustomers(headers, params, body)?.exe(
            onFailure = { _, _ ->
                listener(mapOf("networkRespone" to NetworkResponse.FAILURE, "message" to "koneksi gagal"))
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    isExecute.postValue(true)
                    listener(mapOf("networkRespone" to NetworkResponse.SUCCESS, "message" to response.message()))
                } else {
                    isExecute.postValue(false)
                    listener(mapOf("networkRespone" to NetworkResponse.ERROR, "message" to response.message()))
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
//        isExecute.postValue(true)
        return isExecute
    }

    internal fun getListCustomers(): LiveData<List<Customer>?> {
        return customers
    }

    internal fun getListCustomerGroups(): LiveData<List<CustomerGroup>?> {
        return customersGroup
    }

    internal fun getListPriceGroups(): LiveData<List<PriceGroup>?> {
        return priceGroup
    }

    fun setIdCustomer(idCustomer : String){
        this.idCustomer = idCustomer
    }

    internal fun getSyncCustomerToBK(): LiveData<DataSyncCustomerToBK>{
        return this.statusSyncCustomerToBK
    }
}
