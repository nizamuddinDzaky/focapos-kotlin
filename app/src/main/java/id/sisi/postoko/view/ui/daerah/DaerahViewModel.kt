package id.sisi.postoko.view.ui.daerah

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.DataDaerah
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class DaerahViewModel : ViewModel() {
    private val daerahProvince = MutableLiveData<List<DataDaerah>?>()
    private val daerahCity = MutableLiveData<List<DataDaerah>?>()
    private val daerahStates = MutableLiveData<List<DataDaerah>?>()

    private var isExecute = MutableLiveData<Boolean>()
    private var isSuccessUpdate = MutableLiveData<Boolean>()

    fun getProvince() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getProvince(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                daerahProvince.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        daerahProvince.postValue(response.body()?.data)
                    }
                } else {
                    isExecute.postValue(false)
                }
            }
        )
    }

    fun getCity(provinceName : String) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("province" to provinceName)
        ApiServices.getInstance()?.getCity(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                daerahCity.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        daerahCity.postValue(response.body()?.data)
                    }
                } else {
                    isExecute.postValue(false)
                }
            }
        )
    }

    fun getStates(cityName : String) {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("city" to cityName)
        ApiServices.getInstance()?.getStates(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                daerahStates.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(true)
                        daerahStates.postValue(response.body()?.data)
                    }
                } else {
                    isExecute.postValue(false)
                }
            }
        )
    }

    internal fun getAllProvince(): LiveData<List<DataDaerah>?> {
        return daerahProvince
    }

    internal fun getAllCity(): LiveData<List<DataDaerah>?> {
        return daerahCity
    }

    internal fun getAllStates(): LiveData<List<DataDaerah>?> {
        return daerahStates
    }
}