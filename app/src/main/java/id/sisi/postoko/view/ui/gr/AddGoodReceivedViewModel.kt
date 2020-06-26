package id.sisi.postoko.view.ui.gr

import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_GOODS_RECEIVED
import id.sisi.postoko.utils.TXT_URL_NOT_FOUND
import id.sisi.postoko.utils.extensions.copyText
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.helper.json2obj

class AddGoodReceivedViewModel(private var idGoodsReceived: Int, private val fa: FragmentActivity? = null) : ViewModel() {
    private var _isExecute = MutableLiveData<Boolean>()
    private var _goodReceived = MutableLiveData<GoodReceived?>()
    private var message = MutableLiveData<String?>()

    val gr: LiveData<GoodReceived?> = _goodReceived

    fun postAddGoodReceived(body: Map<String, Any?> = mapOf(), listener: () -> Unit) {
        _isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_GOODS_RECEIVED to idGoodsReceived.toString())
        ApiServices.getInstance()?.postAddGoodReceived(headers, params, body)?.exe(
            onFailure = { _, t ->
                message.postValue(t.toString())
                _isExecute.postValue(false)
            },
            onResponse = { _, response ->
                _isExecute.postValue(false)
                if (response.isSuccessful) {
                    message.postValue(response.body()?.message)
                    listener()
                } else {
                    _isExecute.postValue(false)
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

    fun requestDetailGoodReceived(listener: () -> Unit = {}) {
        _isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_GOODS_RECEIVED to idGoodsReceived.toString())
        ApiServices.getInstance()?.getDetailGoodReceived(headers, params)?.exe(
            onFailure = { _, _ ->
                _isExecute.postValue(true)
                _goodReceived.postValue(null)
            },
            onResponse = { _, response ->
                _isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener()
                    val newGoodReceived = response.body()?.data?.good_received
                    newGoodReceived?.goodReceivedItems =
                        response.body()?.data?.good_received_items
                    _goodReceived.postValue(newGoodReceived)
                } else {
                    _isExecute.postValue(true)
                }
            }
        )
    }

    fun startReceived(fragmentManager: FragmentManager) {
        BottomSheetAddGoodReceivedFragment.showBottomSheet(fragmentManager, _goodReceived.value) {
            requestDetailGoodReceived()
        }
    }

    fun copyString(string: String){
        string.copyText(fa)
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        /*_isExecute.postValue(true)*/
        return _isExecute
    }

    fun getDetailGoodReceived(): LiveData<GoodReceived?> = _goodReceived

    internal fun getMessage() = message
}
