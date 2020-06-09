package id.sisi.postoko.view.ui.customergroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_CUSTOMER_GROUP
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class CustomerGroupViewModel: ViewModel() {
    private val customersGroup = MutableLiveData<List<CustomerGroup>?>()
    private var isExecute = MutableLiveData<Boolean>()
    private val customers = MutableLiveData<List<Customer>?>()

    fun getListCustomerCustomerGroup(id_customer_group: String, getSelected: Boolean = false) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_CUSTOMER_GROUP to ( id_customer_group))
        ApiServices.getInstance()?.getListCustomerCustomerGroup(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customers.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(false)
                        if(!getSelected) {
                            customers.postValue(response.body()?.data?.list_customer)
                        } else{
                            customers.postValue(response.body()?.data?.customer_selected)
                        }
                    }
                } else {
                    isExecute.postValue(false)
                    customers.postValue(listOf())
                }
            }
        )
    }

    fun getListCustomerGroup() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListCustomerGroup(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customersGroup.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    isExecute.postValue(false)
                    tryMe {
                        customersGroup.postValue(response.body()?.data?.customer_groups)
                    }
                } else {
                    isExecute.postValue(false)
                    customersGroup.postValue(listOf())
                }
            }
        )
    }

    fun postAddCustomerToCustoemrGroup(body: Map<String, Any?>,idCustomerGroup: String, listener: (Map<String, Any?>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_CUSTOMER_GROUP to idCustomerGroup)
        ApiServices.getInstance()?.postAddCustomerToCustoemrGroup(headers, params, body)?.exe(
            onFailure = { _, _ ->
                listener(
                    mapOf(
                        "networkRespone" to NetworkResponse.FAILURE,
                        "message" to "koneksi gagal"
                    )
                )
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    isExecute.postValue(false)
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.SUCCESS,
                            "message" to response.body()?.message
                        )
                    )
                } else {
                    isExecute.postValue(false)
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.ERROR,
                            "message" to response.body()?.message
                        )
                    )
                }
            }
        )
    }

    fun postAddCustomerGroup(body: Map<String, Any?>, listener: (Map<String, Any?>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postAddCustomerGroup(headers, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                listener(
                    mapOf(
                        "networkRespone" to NetworkResponse.FAILURE,
                        "message" to "koneksi gagal"
                    )
                )
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    isExecute.postValue(false)
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.SUCCESS,
                            "message" to response.body()?.message
                        )
                    )
                } else {
                    isExecute.postValue(false)
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.ERROR,
                            "message" to response.body()?.message
                        )
                    )
                }
            }
        )
    }

    fun putEditCustomerGroup(body: Map<String, Any?>, idCustomerGroup: String, listener: (Map<String, Any>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_CUSTOMER_GROUP to idCustomerGroup)
        ApiServices.getInstance()?.putEditCustomerGroup(headers, params, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                listener(
                    mapOf(
                        "networkRespone" to NetworkResponse.FAILURE,
                        "message" to "koneksi gagal"
                    )
                )
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    isExecute.postValue(true)
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.SUCCESS,
                            "message" to response.message()
                        )
                    )
                } else {
                    isExecute.postValue(false)
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.ERROR,
                            "message" to response.message()
                        )
                    )
                }
            }
        )
    }

    internal fun getListCustomerGroups(): LiveData<List<CustomerGroup>?> {
        return customersGroup
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        return isExecute
    }

    internal fun getListCustomers(): LiveData<List<Customer>?> {
        return customers
    }
}