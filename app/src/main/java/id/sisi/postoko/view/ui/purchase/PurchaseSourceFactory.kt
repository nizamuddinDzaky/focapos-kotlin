package id.sisi.postoko.view.ui.purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import id.sisi.postoko.model.Purchases
import id.sisi.postoko.network.ApiServices

class PurchaseSourceFactory(
    var api: ApiServices,
    var filter: Map<String, String>,
    var networkState: MutableLiveData<NetworkState>
) :
    DataSource.Factory<Int, Purchases>() {

    private var listPurchases = MutableLiveData<PageKeyedPurchaseDataSource>()

    internal fun getListSalesBooking(): LiveData<PageKeyedPurchaseDataSource> = listPurchases

    override fun create(): DataSource<Int, Purchases> {
        val source = PageKeyedPurchaseDataSource(api, filter, networkState)
        listPurchases = MutableLiveData()
        listPurchases.postValue(source)
        return source
    }
}