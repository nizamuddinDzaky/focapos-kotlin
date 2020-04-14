package id.sisi.postoko.view.ui.pricegroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.model.Product
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_PRICE_GROUP
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class PriceGroupViewModel : ViewModel() {
    private val priceGroup = MutableLiveData<List<PriceGroup>?>()
    private val productPrice = MutableLiveData<List<Product>?>()
    private var isExecute = MutableLiveData<Boolean>()

    fun getListPriceGroup() {
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

    fun postAddPriceGroup(body: Map<String, Any?>, listener: (Map<String, Any?>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postAddPriceGroup(headers, body)?.exe(
            onFailure = { _, _ ->
                listener(
                    mapOf(
                        "networkRespone" to NetworkResponse.FAILURE,
                        "message" to "koneksi gagal"
                    )
                )
                isExecute.postValue(true)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.SUCCESS,
                            "message" to response.body()?.message
                        )
                    )
                } else {
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.ERROR,
                            "message" to response.body()?.message
                        )
                    )
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun postAddCustomerToPriceGroup(body: Map<String, Any?>, idPriceGroup: String, listener: (Map<String, Any>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PRICE_GROUP to idPriceGroup)
        ApiServices.getInstance()?.postAddCustomerToPriceGroup(headers, params, body)?.exe(
            onFailure = { _, _ ->
                listener(
                    mapOf(
                        "networkRespone" to NetworkResponse.FAILURE,
                        "message" to "koneksi gagal"
                    )
                )
                isExecute.postValue(true)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.SUCCESS,
                            "message" to response.message()
                        )
                    )
                } else {
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.ERROR,
                            "message" to response.message()
                        )
                    )
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun putEditPriceGroup(body: Map<String, Any?>, idPriceGroup: String, listener: (Map<String, Any>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PRICE_GROUP to idPriceGroup)
        ApiServices.getInstance()?.putEditPriceGroup(headers, params, body)?.exe(
            onFailure = { _, _ ->
                listener(
                    mapOf(
                        "networkRespone" to NetworkResponse.FAILURE,
                        "message" to "koneksi gagal"
                    )
                )
                isExecute.postValue(true)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.SUCCESS,
                            "message" to response.message()
                        )
                    )
                } else {
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.ERROR,
                            "message" to response.message()
                        )
                    )
                    isExecute.postValue(true)
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
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        productPrice.postValue(response.body()?.data?.group_product_price ?: listOf())
                    }
                } else {
                    productPrice.postValue(listOf())
                }
            }
        )
    }

    fun putEditProductPrice(body: Map<String, Any?>, idPriceGroup: String, listener: (Map<String, Any>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_PRICE_GROUP to idPriceGroup)
        ApiServices.getInstance()?.putEditProductPrice(headers, params, body)?.exe(
            onFailure = { _, _ ->
                listener(
                    mapOf(
                        "networkRespone" to NetworkResponse.FAILURE,
                        "message" to "koneksi gagal"
                    )
                )
                isExecute.postValue(true)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.SUCCESS,
                            "message" to response.message()
                        )
                    )
                } else {
                    listener(
                        mapOf(
                            "networkRespone" to NetworkResponse.ERROR,
                            "message" to response.message()
                        )
                    )
                    isExecute.postValue(true)
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
}
