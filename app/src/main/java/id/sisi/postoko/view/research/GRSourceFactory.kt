package id.sisi.postoko.view.research

import androidx.paging.DataSource
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.network.ApiServices

class GRSourceFactory(api: ApiServices, status: String) : DataSource.Factory<Int, GoodReceived>() {

    val source = PageKeyedGRDataSource(api, status)

    override fun create(): DataSource<Int, GoodReceived> {
        return source
    }
}