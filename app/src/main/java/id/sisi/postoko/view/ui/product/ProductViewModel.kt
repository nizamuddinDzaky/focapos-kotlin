package id.sisi.postoko.view.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.Product
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_PRODUCT_ID
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class ProductViewModel : ViewModel() {
    private val products = MutableLiveData<List<Product>?>()
    private val detailProduct = MutableLiveData<Product>()
    private var isExecute = MutableLiveData<Boolean>()

    /*init {
        getListProduct()
    }*/

    fun getListProduct(warehouseId: Int? = 0) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("warehouse_id" to warehouseId.toString())
        ApiServices.getInstance()?.getListProduct(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                products.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(false)
                        logE("asdsad : ${response.body()?.data?.list_products}")
                        products.postValue(response.body()?.data?.list_products)
                    }
                } else {
                    isExecute.postValue(false)
                    products.postValue(listOf())
                }
            }
        )
    }

    fun getListProductSales(idCustomer: Int) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf("customer_id" to idCustomer.toString())
        ApiServices.getInstance()?.getListProductSales(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                products.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(false)
                        products.postValue(response.body()?.data?.list_products)
                        
                    }
                } else {
                    isExecute.postValue(false)
                    products.postValue(listOf())
                }
            }
        )
    }

    fun getDetailProduct(productId: Int) {
        isExecute.postValue(true)
        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_PRODUCT_ID to productId.toString())
        ApiServices.getInstance()?.getDetailProduct(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
                products.postValue(null)
            },
            onResponse = { _, response ->
                if (response.isSuccessful) {
                    tryMe {
                        isExecute.postValue(false)
                        val newProduct = response.body()?.data?.product
                        newProduct?.warehouse = response.body()?.data?.warehouses
                        detailProduct.postValue(newProduct)
                        /*products.postValue(response.body()?.data?.list_products)*/
                    }
                } else {
                    isExecute.postValue(false)
                    products.postValue(listOf())
                }
            }
        )
    }

    internal fun getIsExecute(): LiveData<Boolean> {
        return isExecute
    }

    internal fun getDetailProducts(): LiveData<Product> {
        return detailProduct
    }

    internal fun getListProducts(): LiveData<List<Product>?> {
        return products
    }
}
