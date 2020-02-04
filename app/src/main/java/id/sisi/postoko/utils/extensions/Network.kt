package id.sisi.postoko.utils.extensions

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.exe(
    onFailure: (Call<T>, Throwable) -> Unit,
    onResponse: (Call<T>, Response<T>) -> Unit,
    onFinish: () -> Unit = {}
) {
    if (this.isExecuted) {
        return
    }
    this.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailure(call, t)
            onFinish()
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            onResponse(call, response)
            onFinish()
        }
    })
}