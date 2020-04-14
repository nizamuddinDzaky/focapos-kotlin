package id.sisi.postoko.view.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.ApiServices

class SBSourceFactory(
    var api: ApiServices,
    var filter: Map<String, String>,
    var networkState: MutableLiveData<NetworkState>
) :
    DataSource.Factory<Int, Sales>() {

    private var listGoodReceived = MutableLiveData<PageKeyedSBDataSource>()

    internal fun getListGoodReceived(): LiveData<PageKeyedSBDataSource> = listGoodReceived

    override fun create(): DataSource<Int, Sales> {
        val source = PageKeyedSBDataSource(api, filter, networkState)

        listGoodReceived = MutableLiveData()
        listGoodReceived.postValue(source)
        return source
    }
}