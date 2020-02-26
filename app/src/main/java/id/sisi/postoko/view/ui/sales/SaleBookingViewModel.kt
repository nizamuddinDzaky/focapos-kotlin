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
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class SaleBookingViewModel(var id: Int) : ViewModel() {
    private val sale = MutableLiveData<Sales?>()
    private val customer = MutableLiveData<Customer?>()
    private val warehouse = MutableLiveData<Warehouse?>()
    private val supplier = MutableLiveData<Supplier?>()
    private var isExecute = MutableLiveData<Boolean>()

    fun requestDetailCustomer(idCustomer: Int) {
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_customers" to idCustomer.toString())
        ApiServices.getInstance()?.getDetailCustomer(headers, params)?.exe(
            onFailure = { call, throwable ->
                isExecute.postValue(true)
                customer.postValue(null)
            },
            onResponse = { call, response ->
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
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_warehouses" to idWarehouse.toString())
    }

    fun requestDetailSupplier(idSupplier: Int) {
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_suppliers" to idSupplier.toString())
    }

    fun requestDetailSale() {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("id_sales_booking" to id.toString())
        ApiServices.getInstance()?.getDetailSale(headers, params)?.exe(
            onFailure = { call, throwable ->
                logE("gagal")
                isExecute.postValue(true)
                sale.postValue(null)
            },
            onResponse = { call, response ->
                logE("berhasil product")
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
