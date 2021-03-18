package id.sisi.postoko.view.ui.purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.sisi.postoko.model.Purchases
import id.sisi.postoko.network.ApiServices

enum class NetworkState {
    RUNNING,
    SUCCESS,
    FAILED
}
class PurchasesViewModel(var filter: HashMap<String, String>): ViewModel() {
    companion object {
        private const val PAGE_SIZE = 10
    }

    var repos: LiveData<PagedList<Purchases>>
    var networkState = MutableLiveData<NetworkState>()
    private var tempFilter = MutableLiveData<HashMap<String, String>>()

    private val factory = PurchaseSourceFactory(ApiServices.getInstance()!!, filter, networkState)

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
        factory.getListSalesBooking().value?.invalidate()
    }

    fun requestRefreshNewFilter(newFilter: HashMap<String, String>) {
        factory.filter = newFilter
        tempFilter.postValue(newFilter)
        factory.getListSalesBooking().value?.invalidate()
    }

    internal fun getFilter() = tempFilter
}