package id.sisi.postoko.view.ui.goodreveived

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.model.Payment
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class AddGoodReceivedViewModel(var id_goods_received: Int) : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()
    private var goodReceived = MutableLiveData<GoodReceived?>()

    fun postAddGoodReceived(body: Map<String, String> = mapOf(), listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_goods_received" to id_goods_received.toString())
        ApiServices.getInstance()?.postAddGoodReceived(headers, params, body)?.exe(
            onFailure = { call, throwable ->
                isExecute.postValue(true)
            },
            onResponse = { call, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener()
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun requestDetailGoodReceived(body: Map<String, String> = mapOf(), listener: () -> Unit = {}) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_goods_received" to id_goods_received.toString())
        ApiServices.getInstance()?.getDetailGoodReceived(headers, params)?.exe(
            onFailure = { call, throwable ->
                isExecute.postValue(true)
                goodReceived.postValue(null)
            },
            onResponse = { call, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener()
                    val newGoodReceived = response.body()?.data?.good_received
                    newGoodReceived?.good_received_items =
                        response.body()?.data?.good_received_items
                    goodReceived.postValue(newGoodReceived)
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

    internal fun getDetailGoodReceived() = goodReceived
}
