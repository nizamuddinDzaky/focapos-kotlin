package id.sisi.postoko.view.ui.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Sales
import id.sisi.postoko.model.Supplier
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.KEY_ID_SUPPLIER
import id.sisi.postoko.utils.KEY_ID_WAREHOUSE
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.tryMe

class SaleBookingViewModel(var id: Int) : ViewModel() {
    private val sale = MutableLiveData<Sales?>()
    private val customer = MutableLiveData<Customer?>()
    private val warehouse = MutableLiveData<Warehouse?>()
    private val supplier = MutableLiveData<Supplier?>()
    private var isExecute = MutableLiveData<Boolean>()

    fun requestDetailCustomer(idCustomer: Int) {
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customers" to idCustomer.toString())
        ApiServices.getInstance()?.getDetailCustomer(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                customer.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        customer.postValue(response.body()?.data?.customer)
                    }
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun requestDetailWarehouse(idWarehouse: Int) {
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_WAREHOUSE to idWarehouse.toString())
        ApiServices.getInstance()?.getDetailWarehouse(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                warehouse.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        warehouse.postValue(response.body()?.data?.warehouse)
                    }
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    fun requestDetailSupplier(idSupplier: Int) {
        mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        mutableMapOf(KEY_ID_SUPPLIERS to idSupplier.toString())
    }

    fun requestDetailSale() {
        isExecute.postValue(true)
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_ID_SALES_BOOKING to id.toString())
        ApiServices.getInstance()?.getDetailSale(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                sale.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        val newSale = response.body()?.data?.sale
                        newSale?.saleItems = response.body()?.data?.sale_items
                        sale.postValue(newSale)
                    }
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        isExecute.postValue(true)
        return isExecute
    }

    internal fun getDetailSale() = sale

    internal fun getDetailCustomer() = customer

    internal fun getDetailWarehouse() = warehouse

    internal fun getDetailSupplier() = supplier
}
