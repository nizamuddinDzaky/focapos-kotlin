package id.sisi.postoko.view.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.ApiServices

class SBSourceFactory(
    var api: ApiServices,
    var filter: Map<String, String>,
    var networkState: MutableLiveData<NetworkState>,
    var isAksestoko: Boolean
) :
    DataSource.Factory<Int, Sales>() {

    private var listSalesBooking = MutableLiveData<PageKeyedSBDataSource>()

    internal fun getListSalesBooking(): LiveData<PageKeyedSBDataSource> = listSalesBooking

    override fun create(): DataSource<Int, Sales> {
        val source = PageKeyedSBDataSource(api, filter, networkState, isAksestoko)
        listSalesBooking = MutableLiveData()
        listSalesBooking.postValue(source)
        return source
    }
}