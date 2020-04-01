package id.sisi.postoko.view.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.model.User
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.TXT_CONNECTION_FAILED
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe
import id.sisi.postoko.utils.helper.json2obj

class LoginViewModel : ViewModel() {
    private var isExecute = MutableLiveData<Boolean>()
    private var message = MutableLiveData<String?>()
    private val dataLogin = MutableLiveData<DataLogin?>()
    private val userProfile = MutableLiveData<User?>()

    fun getUserProfile(token: String) {
        val headers = mutableMapOf(KEY_FORCA_TOKEN to token)
        ApiServices.getInstance()?.getProfile(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        userProfile.postValue(response.body()?.data?.user)
                    }
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun postLogin(body: Map<String, String>, listener: () -> Unit) {
        isExecute.postValue(true)
        ApiServices.getInstance()?.postLogin(body)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                message.postValue(TXT_CONNECTION_FAILED)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        listener()
                        val data = response.body()?.data
                        dataLogin.postValue(data)
                    }
                } else {
                    isExecute.postValue(false)
                    val errorResponse =
                        response.errorBody()?.string()?.json2obj<BaseResponse<DataLogin>>()
                    message.postValue(errorResponse?.message)
                }
            }
        )
    }

    internal fun getDataLogin() = dataLogin

    internal fun getMessage() = message

    internal fun getUserProfile() = userProfile

    internal fun getIsExecute() = isExecute
}
