package id.sisi.postoko.view.ui.goodreveived

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class GoodReceivedViewModel(var status: String) : ViewModel() {
    private val goodsReceived = MutableLiveData<List<GoodReceived>?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListGoodReceived()
    }

    private fun getListGoodReceived() {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("gr_status" to status)
        ApiServices.getInstance()?.getListGoodReceived(headers, params)?.exe(
            onFailure = { call, throwable ->
                logE("gagal list good received")
                isExecute.postValue(true)
                goodsReceived.postValue(null)
            },
            onResponse = { call, response ->
                logE("berhasil list good received")
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        goodsReceived.postValue(response.body()?.data?.list_goods_received)
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

    internal fun getListGoodsReceived(): LiveData<List<GoodReceived>?> {
        return goodsReceived
    }
}
