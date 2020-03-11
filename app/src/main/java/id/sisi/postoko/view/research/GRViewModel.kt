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

    val repos: LiveData<PagedList<GoodReceived>>

    val networkState: LiveData<NetworkState>

    init {
        val factory = GRSourceFactory(ApiServices.getInstance()!!, status)
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setPrefetchDistance(5)
            .setEnablePlaceholders(false)
            .build()

        repos = LivePagedListBuilder(factory, config).build()
        networkState = factory.source.networkState
    }
}
