package id.sisi.postoko.view.ui.warehouse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class WarehouseViewModel : ViewModel() {
    private val warehouses = MutableLiveData<List<Warehouse>?>()
    private var isExecute = MutableLiveData<Boolean>()

    init {
        getListWarehouse()
    }

    fun getListWarehouse() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getListWarehouse(headers)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(true)
                warehouses.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        logE("tes ${response.body()?.data}")
                        warehouses.postValue(response.body()?.data?.list_warehouses)
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

    internal fun getListWarehouses(): LiveData<List<Warehouse>?> {
        return warehouses
    }
}
