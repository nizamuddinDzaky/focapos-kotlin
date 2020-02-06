package id.sisi.postoko.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.User
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class AccountViewModel : ViewModel() {
    private val user = MutableLiveData<User>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getProfile(headers)?.exe(
            onFailure = { call, throwable ->
                logE("gagal")
                isExecute.postValue(true)
                user.postValue(null)
            },
            onResponse = { call, response ->
                logE("berhasil profile")
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        logE("tes ${response.body()?.data?.user?.username}")
                        user.postValue(response.body()?.data?.user)
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

    internal fun getUser(): LiveData<User> {
        return user
    }
}
