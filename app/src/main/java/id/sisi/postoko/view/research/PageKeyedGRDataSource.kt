package id.sisi.postoko.view.research

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_GR_STATUS
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus
import java.io.IOException

class PageKeyedGRDataSource(private val api: ApiServices, var filter: Map<String, String>) :
    PageKeyedDataSource<Int, GoodReceived>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GoodReceived>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GoodReceived>) {
        val page = params.key
        val numberOfItems = params.requestedLoadSize
        callAPI(page, page + 1, numberOfItems) { repos, next ->
            callback.onResult(repos, next)
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GoodReceived>
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
        callback: (repos: List<GoodReceived>, next: Int?) -> Unit
    ) {
        logE("networkState start")
        networkState.postValue(NetworkState.RUNNING)

        try {
            val countOffset = requestedPage * requestedLoadSize

            val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
            val params = mutableMapOf(
                "offset" to countOffset.toString(),
                "limit" to requestedLoadSize.toString()
            )
            if (filter.containsKey(KEY_GR_STATUS) && filter[KEY_GR_STATUS] != GoodReceiveStatus.ALL.name) {
                params[KEY_GR_STATUS] = filter.getValue(KEY_GR_STATUS)
            }
            filter.entries.forEach {
                if (it.key != KEY_GR_STATUS) {
                    params[it.key] = it.value
                }
            }
            val response = api.getListGoodReceived(headers, params).execute()

            response.body()?.let {
                callback(it.data?.list_goods_received ?: arrayListOf(), adjacentPage)
                networkState.postValue(NetworkState.SUCCESS)
            }
        } catch (e: IOException) {
            logE("error broh ${e.message}")
            networkState.postValue(NetworkState.FAILED)
        }

        //networkState.postValue(NetworkState.FAILED)
    }
}