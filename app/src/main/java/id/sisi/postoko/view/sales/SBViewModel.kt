package id.sisi.postoko.view.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.sisi.postoko.model.Sales
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.logE

enum class NetworkState {
    RUNNING,
    SUCCESS,
    FAILED
}

class SBViewModel(var filter: HashMap<String, String>) : ViewModel() {
    companion object {
        private const val PAGE_SIZE = 10
    }

    var repos: LiveData<PagedList<Sales>>
    var networkState = MutableLiveData<NetworkState>()
    private var tempFilter = MutableLiveData<HashMap<String, String>>()

    private val factory = SBSourceFactory(ApiServices.getInstance()!!, filter, networkState)

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setPrefetchDistance(5)
            .setEnablePlaceholders(false)
            .build()

        tempFilter.postValue(filter)
        repos = LivePagedListBuilder(factory, config).build()
    }

    fun requestRefreshNoFilter() {
        logE("requestRefreshNoFilter()")
        factory.getListGoodReceived().value?.invalidate()
    }

    fun requestRefreshNewFilter(newFilter: HashMap<String, String>) {
        factory.filter = newFilter
        tempFilter.postValue(newFilter)
        factory.getListGoodReceived().value?.invalidate()
    }

    internal fun getFilter() = tempFilter
}
