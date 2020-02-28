package id.sisi.postoko.view.ui.gr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_GOODS_RECEIVED
import id.sisi.postoko.utils.extensions.exe

class AddGoodReceivedViewModel(private var idGoodsReceived: Int) : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()
    private var goodReceived = MutableLiveData<GoodReceived?>()

    fun postAddGoodReceived(body: Map<String, String> = mapOf(), listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_GOODS_RECEIVED to idGoodsReceived.toString())
        ApiServices.getInstance()?.postAddGoodReceived(headers, params, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener()
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun requestDetailGoodReceived(listener: () -> Unit = {}) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_GOODS_RECEIVED to idGoodsReceived.toString())
        ApiServices.getInstance()?.getDetailGoodReceived(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
                goodReceived.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener()
                    val newGoodReceived = response.body()?.data?.good_received
                    newGoodReceived?.goodReceivedItems =
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
