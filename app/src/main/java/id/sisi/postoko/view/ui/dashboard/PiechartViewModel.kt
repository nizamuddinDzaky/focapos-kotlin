package id.sisi.postoko.view.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.DataPieChart
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.KEY_FORCA_TOKEN
import id.sisi.postoko.utils.KEY_PIECHART_DATE
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe

class PiechartViewModel() : ViewModel()  {
    private var isExecute = MutableLiveData<Boolean>()
    private var pieChartData = MutableLiveData<DataPieChart>()

    fun requestPieChartData(dates: String, warehouse_id: String) {
        val headers = mutableMapOf(KEY_FORCA_TOKEN to (MyApp.prefs.posToken ?: ""))
        val params = mutableMapOf(KEY_PIECHART_DATE to dates, "warehouse_id" to warehouse_id)
        ApiServices.getInstance()?.getPieChartData(headers, params)?.exe(
            onFailure = { _, _ ->
                isExecute.postValue(false)
//                customer.postValue(null)
            },
            onResponse = { _, response ->
                isExecute.postValue(false)
                if (response.isSuccessful) {
                    tryMe {
                        logE("dataPie: ${response.body()?.data}")
//                        pieChartDataClosed.postValue(response.body()?.data?.closed)
//                        pieChartDataPending.postValue(response.body()?.data?.pending)
                        pieChartData.postValue(response.body()?.data)
//                        pieChartData.postValue(mapOf("pending" to response.body()?.data?.pending))
//                        pieChartData.postValue(mapOf("closed" to response.body()?.data?.closed))
//                        pieChartData.postValue(mapOf("reserved" to response.body()?.data?.reserved))
                    }
                } else {
                    isExecute.postValue(true)
                }
            }
        )
    }

    internal fun getPieChartData()= pieChartData

}