package id.sisi.postoko.view.research

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus

class GRSourceFactory(var api: ApiServices, var filter: Map<String, String>) :
    DataSource.Factory<Int, GoodReceived>() {

    var listGoodReceived = MutableLiveData<PageKeyedGRDataSource>()

    internal fun getListGoodReceived(): LiveData<PageKeyedGRDataSource> = listGoodReceived

    override fun create(): DataSource<Int, GoodReceived> {
        val source = PageKeyedGRDataSource(api, filter)

        listGoodReceived = MutableLiveData()
        listGoodReceived.postValue(source)
        return source
    }
}