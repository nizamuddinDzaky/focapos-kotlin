package id.sisi.postoko.view.ui.pricegroup

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.*
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_PRICE_GROUP
import id.sisi.postoko.utils.TXT_URL_NOT_FOUND
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj

class PriceGroupViewModel : ViewModel() {
    private val priceGroup = MutableLiveData<List<PriceGroup>?>()
    private val customers = MutableLiveData<List<Customer>?>()
    private val productPrice = MutableLiveData<List<Product>?>()
    private var isExecute = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()

    fun getListCustomerPriceGroup(id_price_group: String, getSelected: Boolean = false) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PRICE_GROUP to ( id_price_group))
        ApiServices.getInstance()?.getListCustomerPriceGroup(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customers.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(false)
                        logE("${response.body()}")
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

    fun getListPriceGroup() {
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
                        isExecute.postValue(false)
                        priceGroup.postValue(response.body()?.data?.price_groups)
                    }
                } else {
                    isExecute.postValue(false)
                    priceGroup.postValue(listOf())
                }
            }
        )
    }

    fun postAddPriceGroup(body: Map<String, Any?>, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postAddPriceGroup(headers, body)?.exe(
            onFailure = { _, t ->
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

    fun postAddCustomerToPriceGroup(body: Map<String, Any?>, idPriceGroup: String, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PRICE_GROUP to idPriceGroup)
        ApiServices.getInstance()?.postAddCustomerToPriceGroup(headers, params, body)?.exe(
            onFailure = { _, t ->
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

    fun putEditPriceGroup(body: Map<String, Any?>, idPriceGroup: String, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PRICE_GROUP to idPriceGroup)
        ApiServices.getInstance()?.putEditPriceGroup(headers, params, body)?.exe(
            onFailure = { _,  t->
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

    fun getListProductPrice(idPriceGroup: String) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PRICE_GROUP to idPriceGroup)
        ApiServices.getInstance()?.getListProductPrice(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                productPrice.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        productPrice.postValue(response.body()?.data?.group_product_price ?: listOf())
                    }
                } else {
                    isExecute.postValue(false)
                    productPrice.postValue(listOf())
                }
            }
        )
    }

    fun putEditProductPrice(body: Map<String, Any?>, idPriceGroup: String, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PRICE_GROUP to idPriceGroup)
        ApiServices.getInstance()?.putEditProductPrice(headers, params, body)?.exe(
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

    internal fun getListPriceGroups(): LiveData<List<PriceGroup>?> {
        return priceGroup
    }

    internal fun getListProductPrice(): LiveData<List<Product>?> {
        return productPrice
    }

    internal fun getListCustomers(): LiveData<List<Customer>?> {
        return customers
    }

    internal fun getMessage() = message
}
