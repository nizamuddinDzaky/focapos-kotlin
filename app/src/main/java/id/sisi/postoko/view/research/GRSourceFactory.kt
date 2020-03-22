package id.sisi.postoko.view.research

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.network.ApiServices

class GRSourceFactory(
    var api: ApiServices,
    var filter: Map<String, String>,
    var networkState: MutableLiveData<NetworkState>
) :
    DataSource.Factory<Int, GoodReceived>() {

    private var listGoodReceived = MutableLiveData<PageKeyedGRDataSource>()

    internal fun getListGoodReceived(): LiveData<PageKeyedGRDataSource> = listGoodReceived

    override fun create(): DataSource<Int, GoodReceived> {
        val source = PageKeyedGRDataSource(api, filter, networkState)

        listGoodReceived = MutableLiveData()
        listGoodReceived.postValue(source)
        return source
    }
}