package id.sisi.postoko.view.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.logE

class SBSourceFactory(
    var api: ApiServices,
    var filter: Map<String, String>,
    var networkState: MutableLiveData<NetworkState>,
    var isAksestoko: Boolean
) :
    DataSource.Factory<Int, Sales>() {

    private var listGoodReceived = MutableLiveData<PageKeyedSBDataSource>()

    internal fun getListGoodReceived(): LiveData<PageKeyedSBDataSource> = listGoodReceived

    override fun create(): DataSource<Int, Sales> {
        val source = PageKeyedSBDataSource(api, filter, networkState, isAksestoko)
        listGoodReceived = MutableLiveData()
        listGoodReceived.postValue(source)
        return source
    }
}