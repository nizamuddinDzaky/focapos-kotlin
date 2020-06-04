package id.sisi.postoko.view.ui.profile

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.model.User
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_USER
import id.sisi.postoko.utils.TXT_CONNECTION_FAILED
import id.sisi.postoko.utils.TXT_URL_NOT_FOUND
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj
import okhttp3.MultipartBody

class ProfileViewModel : ViewModel() {
    private val user = MutableLiveData<User?>()
    private var isExecute = MutableLiveData<Boolean>()
    private var isSuccessUpdate = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()

//    init {
//        getUserProfile()
//    }

    fun postResetPassword(body: Map<String, Any?>, listener: () -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postResetPassword(headers, body)?.exe(
            onFailure = { _, _ ->
                message.postValue(TXT_CONNECTION_FAILED)
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    message.postValue(response.body()?.message)
                    listener()
                } else {
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

    fun getUserProfile() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getProfile(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                user.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        val newUser = response.body()?.data?.user
                        newUser?.companyData = response.body()?.data?.company
                        user.postValue(newUser)
                    }
                } else {

                }
            }
        )
    }

    fun putUserProfile(body: MutableMap<String, String>, idUser: String, listener: () -> Unit) {
        isExecute.postValue(true)
        if (body.isEmpty()) {
            logE("body is empty, force cancel call api.")
            return
        }
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_USER to idUser)
        ApiServices.getInstance()?.putProfile(headers, params, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                message.postValue(TXT_CONNECTION_FAILED)
                user.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        val newUser = response.body()?.data?.user
                        newUser?.companyData = response.body()?.data?.company
                        user.postValue(newUser)
                        isSuccessUpdate.postValue(true)
                        message.postValue(response.body()?.message)
                        listener()
                    }
                } else {
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

    fun putChangePassword(body: MutableMap<String, String>, listener: () -> Unit) {
        if (body.isEmpty()) {
            logE("body is empty, force cancel call api.")
            return
        }
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.putChangePassword(headers, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                user.postValue(null)
                listener()
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        val newUser = response.body()?.data?.user
                        newUser?.companyData = response.body()?.data?.company
                        user.postValue(newUser)
                        isSuccessUpdate.postValue(true)
                        message.postValue(TXT_CONNECTION_FAILED)
                        listener()
                    }
                } else {
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

    fun postUploadAvatarProfile(file: MultipartBody.Part?, listener: () -> Unit) {
        /*if (file.isEmpty()) {
            logE("body is empty, force cancel call api.")
            return
        }*/
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        if (file != null) {
            ApiServices.getInstance()?.postUploadAvatarProfile(file,headers)?.exe(
                onFailure = { _, t ->
                    logE("gagal : $t")
                    isExecute.postValue(false)
                    user.postValue(null)
                    listener()
                },
                onResponse = { _, response ->
                    isExecute.postValue(false)
                    if (response.isSuccessful) {
                        tryMe {
                            message.postValue(response.body()?.message)
                            listener()
                        }
                    } else {
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
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        return isExecute
    }

    internal fun getIsSuccessUpdate() = isSuccessUpdate

    internal fun getUser(): LiveData<User?> {
        return user
    }

    internal fun getMessage() = message
}
