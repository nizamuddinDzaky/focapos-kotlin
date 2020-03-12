package id.sisi.postoko.view.research

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.network.ApiServices

enum class NetworkState {
    RUNNING,
    SUCCESS,
    FAILED
}

class GRViewModel(var status: String) : ViewModel() {
    companion object {
        private const val PAGE_SIZE = 10
    }

    var repos: LiveData<PagedList<GoodReceived>>

    var networkState: LiveData<NetworkState>?

    private val factory = GRSourceFactory(ApiServices.getInstance()!!, status)

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setPrefetchDistance(5)
            .setEnablePlaceholders(false)
            .build()

        repos = LivePagedListBuilder(factory, config).build()
        networkState = factory.getListGoodReceived().value?.networkState
    }

    fun requestRefreshListGR() {
        factory.listGoodReceived.value?.invalidate()
    }
}
