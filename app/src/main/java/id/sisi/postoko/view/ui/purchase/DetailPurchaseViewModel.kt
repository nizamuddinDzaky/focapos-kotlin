package id.sisi.postoko.view.ui.purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.*
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.KEY_ID_SUPPLIER
import id.sisi.postoko.utils.TXT_CONNECTION_FAILED
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj

class DetailPurchaseViewModel: ViewModel() {

    private var isExecute = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()
    private val purchase = MutableLiveData<Purchases?>()
    private val customer = MutableLiveData<Customer?>()
    private val supplier = MutableLiveData<Supplier?>()

    fun postAddPurchase(body: Map<String, Any?>, listener: (idSale: String?) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postAddPurchase(headers, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                message.postValue(TXT_CONNECTION_FAILED)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(false)
                        message.postValue(response.body()?.message)
                        listener(response.body()?.data?.sale?.id)
                    }
                } else {
                    isExecute.postValue(false)
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    message.postValue(errorResponse?.message)
                    listener("0")
                }
            }
        )
    }

    fun requestDetailPurchase(idPurchase: Int){
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_purchases" to idPurchase.toString())
        ApiServices.getInstance()?.getDetailPurchase(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                purchase.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        val purchases = response.body()?.data?.purchase
                        purchases?.purchaseItems = response.body()?.data?.purchase_items
                        purchase.postValue(purchases)
                    }
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun requestDetailCustomer(idCustomer: Int) {
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customers" to idCustomer.toString())
        ApiServices.getInstance()?.getDetailCustomer(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
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

    fun requestDetailSupplier(idSupplier: Int) {
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SUPPLIER to idSupplier.toString())
        ApiServices.getInstance()?.getDetailSupplier(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                supplier.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        supplier.postValue(response.body()?.data?.supplier)
                    }
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        return isExecute
    }

    internal fun getMessage() = message

    internal fun getDetailPurchase() = purchase

    internal fun getDetailCustomer() = customer

    internal fun getDetailSupplier() = supplier
}