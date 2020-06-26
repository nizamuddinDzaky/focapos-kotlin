package id.sisi.postoko.view.ui.customer

import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.*
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_CUSTOMER
import id.sisi.postoko.utils.TXT_URL_NOT_FOUND
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj
import id.sisi.postoko.view.HomeActivity
import okhttp3.MultipartBody

class CustomerViewModel : ViewModel() {
    private val customers = MutableLiveData<List<Customer>?>()
    private val customersGroup = MutableLiveData<List<CustomerGroup>?>()
    private val priceGroup = MutableLiveData<List<PriceGroup>?>()
    private var isExecute = MutableLiveData<Boolean>()
    private var idCustomer: String? = null
    private var message = MutableLiveData<String?>()
    private val selectedWarehouse = MutableLiveData<List<Warehouse>>()
    private val defaultWarehouse = MutableLiveData<List<Warehouse>>()
    private val warehouse = MutableLiveData<List<Warehouse>>()

    private var statusSyncCustomerToBK = MutableLiveData<DataSyncCustomerToBK>()

    internal fun requestSelectedWarehouse(idCustomer: Int) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customers" to idCustomer.toString())
        ApiServices.getInstance()?.getSelectedWarehouse(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customers.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        logE("warehouse : ${response.body()?.data?.warehouses_default}")
                        val defaultWarehouseResponse = response.body()?.data?.warehouses_default
                        val selectedWarehouseResponse = response.body()?.data?.warehouses_selected
                        val warehouseRespone = response.body()?.data?.warehouses
                        warehouseRespone?.forEach {wh->
                            if (wh.id == defaultWarehouseResponse?.get(0)?.id){
                                wh.isDefault = true
                            }
                            selectedWarehouseResponse?.forEach {whSelcted->
                                if (wh.id == whSelcted.id){
                                    wh.isSelected = true
                                }

                            }

                        }

                        defaultWarehouse.postValue(defaultWarehouseResponse)
                        warehouse.postValue(warehouseRespone)
                        selectedWarehouse.postValue(selectedWarehouseResponse)
                    }
                } else {
                    isExecute.postValue(false)
                }
            }
        )
    }

    fun getListCustomer() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListCustomer(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customers.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        customers.postValue(response.body()?.data?.list_customers)
                    }
                } else {
                    customers.postValue(listOf())
                }
            }
        )
    }

    fun regSyncCustomerToBK() {
        val body: Map<String, Any?> = mutableMapOf()

        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postSyncCustomerToBK(headers, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                statusSyncCustomerToBK.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)

                if (response.isSuccessful) {
                    statusSyncCustomerToBK.postValue(response.body()?.data)
                } else {
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
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        customersGroup.postValue(response.body()?.data?.customer_groups)
                    }
                } else {
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
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        priceGroup.postValue(response.body()?.data?.price_groups)
                    }
                } else {
                    priceGroup.postValue(listOf())
                }
            }
        )
    }

    fun postAddCustomer(body: Map<String, Any?>, logo: MultipartBody.Part?, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postCustomers(headers, body)?.exe(
            onFailure = { _, t ->
                message.postValue(t.toString())
                isExecute.postValue(false)
            },
            onResponse = { _, response ->

                if (response.isSuccessful) {
                    tryMe {
                        message.postValue(response.body()?.message)
                        if (logo != null){
                            logE("asdsadsa => ${response.body()?.data?.id}")

                            postUploadLogoCustomer(logo, response.body()?.data?.id ?: "0"){
                                isExecute.postValue(false)
                                listener()
                            }
                        }else{
                            isExecute.postValue(false)
                            listener()
                        }


                    }
                } else {
                    isExecute.postValue(false)
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    if (TextUtils.isEmpty(errorResponse?.message)){
                        message.postValue(TXT_URL_NOT_FOUND)
                    }else
                        message.postValue(errorResponse?.message)
                }
            }
        )
    }

    fun postUploadLogoCustomer(logo: MultipartBody.Part, idCustomer: String, listener: () -> Unit) {
        isExecute.postValue(true)
       /* if (isUpdate){
            isExecute.postValue(true)
        }*/
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customer" to idCustomer)
        ApiServices.getInstance()?.postUploadLogoCustomer(logo, headers, params)?.exe(
            onFailure = { _, t ->
                logE("gagal : $t")
                message.postValue(t.toString())
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                logE("masuk1")
                if (response.isSuccessful) {
                    logE("masuk2")
                        message.postValue(response.body()?.message)
                        listener()

                } else {
                    logE("masuk3")
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    if (TextUtils.isEmpty(errorResponse?.message)){
                        message.postValue(TXT_URL_NOT_FOUND)
                    }else
                        message.postValue(errorResponse?.message)
                }
            }
        )
    }

    fun postEditCustomer(body: Map<String, Any?>, idCustomer: String, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customer" to idCustomer)
        ApiServices.getInstance()?.putEditCustomers(headers, params, body)?.exe(
            onFailure = { _, t->
                message.postValue(t.toString())
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    isExecute.postValue(false)
                    message.postValue(response.body()?.message)
                    listener()
                } else {
                    isExecute.postValue(false)
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    if (TextUtils.isEmpty(errorResponse?.message)){
                        message.postValue(TXT_URL_NOT_FOUND)
                    }else
                        message.postValue(errorResponse?.message)
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

    internal fun getSyncCustomerToBK(): LiveData<DataSyncCustomerToBK>{
        return this.statusSyncCustomerToBK
    }

    internal fun getSelectedWarehouse(): LiveData<List<Warehouse>?> {
        return selectedWarehouse
    }
    internal fun getDefaultWarehouse():  LiveData<List<Warehouse>?> {
        return defaultWarehouse
    }

    internal fun getListWarehouse():  LiveData<List<Warehouse>?> {
        return warehouse
    }

    internal fun getMessage() = message
}
