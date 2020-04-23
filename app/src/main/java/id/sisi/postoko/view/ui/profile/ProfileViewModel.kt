package id.sisi.postoko.view.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.User
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_USER
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class ProfileViewModel : ViewModel() {
    private val user = MutableLiveData<User?>()
    private var isExecute = MutableLiveData<Boolean>()
    private var isSuccessUpdate = MutableLiveData<Boolean>()

//    init {
//        getUserProfile()
//    }

    fun postResetPassword(body: Map<String, Any?>, listener: (Map<String, Any?>) -> Unit) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.postResetPassword(headers, body)?.exe(
            onFailure = { _, _ ->
                listener(mapOf("networkRespone" to NetworkResponse.FAILURE, "message" to "koneksi gagal"))
                isExecute.postValue(true)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    listener(mapOf("networkRespone" to NetworkResponse.SUCCESS, "message" to response.body()?.message))
                } else {
                    listener(mapOf("networkRespone" to NetworkResponse.ERROR, "message" to response.body()?.message))
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun getUserProfile() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getProfile(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
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
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun putUserProfile(body: MutableMap<String, String>, idUser: String, listener: (Map<String, Any>) -> Unit) {
        if (body.isEmpty()) {
            logE("body is empty, force cancel call api.")
            return
        }
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_USER to idUser)
        ApiServices.getInstance()?.putProfile(headers, params, body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
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
                        listener(mapOf("networkRespone" to NetworkResponse.SUCCESS, "message" to response.message()))
                    }
                } else {
                    isExecute.postValue(true)
                    listener(mapOf("networkRespone" to NetworkResponse.ERROR, "message" to response.message()))
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        isExecute.postValue(true)
        return isExecute
    }

    internal fun getIsSuccessUpdatee() = isSuccessUpdate

    internal fun getUser(): LiveData<User?> {
        return user
    }
}
