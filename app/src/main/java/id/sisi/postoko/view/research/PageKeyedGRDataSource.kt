package id.sisi.postoko.view.research

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.logE
import java.io.IOException

class PageKeyedGRDataSource(private val api: ApiServices, private val status: String) :
    PageKeyedDataSource<Int, GoodReceived>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GoodReceived>) {
        // 今回は使ってない
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GoodReceived>) {
//        callAPI(params.key, params.requestedLoadSize) { repos, next ->
//            callback.onResult(repos, next)
//        }
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
//        callAPI(1, params.requestedLoadSize) { repos, next ->
//            callback.onResult(repos, null, next)
//        }
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
        networkState.postValue(NetworkState.RUNNING)

//        var state = NetworkState.RUNNING

        try {
            val countOffset = requestedPage * requestedLoadSize

            val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
            val params = mutableMapOf(
                "offset" to countOffset.toString(),
                "limit" to requestedLoadSize.toString()
            )
            params["goods_received_status"] = status
            val response = api.getListGoodReceivedPaging(headers, params).execute()

            response.body()?.let {
                callback(it.data?.list_goods_received ?: arrayListOf(), adjacentPage)
//                state = NetworkState.SUCCESS
                networkState.postValue(NetworkState.SUCCESS)
            }
        } catch (e: IOException) {
            logE("error broh ${e.message}")
//            state = NetworkState.FAILED
            networkState.postValue(NetworkState.FAILED)
        }

//        networkState.postValue(state)
    }
}