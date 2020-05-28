package id.sisi.postoko.view

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.model.MyResponse
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UploadFileViewModel() : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()

    fun postUploadFile(body: Map<String, RequestBody>, listener: () -> Unit) {
        isExecute.postValue(true)
       /* val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to idSalesBooking.toString())*/
        ApiServices.getInstance()?.postUploadFile(body)?.exe(
            onFailure = { _, t->
                logE("${t.message}")
                /*message.postValue(TXT_CONNECTION_FAILED)
                isExecute.postValue(false)*/
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        logE("pesan : ${response.body()?.message}")
                        message.postValue(response.body()?.message)
                        listener()
                    }
                } else {
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
//                    if (TextUtils.isEmpty(errorResponse?.message)){
//                        message.postValue(TXT_URL_NOT_FOUND)
//                    }else
//                        message.postValue(errorResponse?.message)
                }
            }
        )
    }

    fun cobaUpload(desc: Map<String, RequestBody>){

        ApiServices.getInstance()?.postUploadFile2(desc)?.exe(
            onFailure = { _, t->
                logE("${t.message}")
                /*message.postValue(TXT_CONNECTION_FAILED)
                isExecute.postValue(false)*/
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        logE("pesan : ${response.body()?.message}")
                        message.postValue(response.body()?.message)

                    }
                } else {
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
//                    if (TextUtils.isEmpty(errorResponse?.message)){
//                        message.postValue(TXT_URL_NOT_FOUND)
//                    }else
//                        message.postValue(errorResponse?.message)
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        return isExecute
    }

    internal fun getMessage() = message
}