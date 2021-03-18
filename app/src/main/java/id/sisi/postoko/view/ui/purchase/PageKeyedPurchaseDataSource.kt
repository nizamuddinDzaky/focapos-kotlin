package id.sisi.postoko.view.ui.purchase

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Purchases
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_SALE_STATUS
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.sales.SaleStatus
import java.io.IOException

class PageKeyedPurchaseDataSource(
    private val api: ApiServices,
    var filter: Map<String, String>,
    var networkState: MutableLiveData<NetworkState>
):
    PageKeyedDataSource<Int, Purchases>() {
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Purchases>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Purchases>) {
        val page = params.key
        val numberOfItems = params.requestedLoadSize
        callAPI(page, page + 1, numberOfItems) { repos, next ->
            callback.onResult(repos, next)
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Purchases>
    ) {
        val numberOfItems = params.requestedLoadSize
        callAPI(0, 1, numberOfItems) { repos, next ->
            callback.onResult(repos, null, next)
        }
    }

    private fun callAPI(
        requestedPage: Int,
        adjacentPage: Int,
        requestedLoadSize: Int,
        callback: (repos: List<Purchases>, next: Int?) -> Unit
    ) {
        networkState.postValue(NetworkState.RUNNING)

        try {
            val countOffset = requestedPage * requestedLoadSize

            val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
            var params = mutableMapOf(
                "offset" to countOffset.toString(),
                "limit" to requestedLoadSize.toString()
            )

            if (filter.containsKey(KEY_SALE_STATUS) && filter[KEY_SALE_STATUS] != SaleStatus.ALL.name) {
                params[KEY_SALE_STATUS] = filter.getValue(KEY_SALE_STATUS)
            }
            filter.entries.forEach {
                if (it.key != KEY_SALE_STATUS) {
                    params[it.key] = it.value
                }
            }
            val response = api.getListPurchases(headers, params).execute()

            response.body()?.let {
                callback(it.data?.list_purchases ?: arrayListOf(), adjacentPage)
                networkState.postValue(NetworkState.SUCCESS)
            }
            response.errorBody()?.let {
                networkState.postValue(NetworkState.FAILED)
            }
        } catch (e: IOException) {
            logE("error broh ${e.message}")
            networkState.postValue(NetworkState.FAILED)
        }
    }
}