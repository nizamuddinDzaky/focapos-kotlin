package id.sisi.postoko.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.sisi.postoko.R
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login?.setOnClickListener {
            //startActivity(Intent(this, HomeActivity::class.java))
            val headers = mutableMapOf<String, String>()
            headers["Authorization"] = ""
            ApiServices.getInstance()
                //?.postLogin(headers, mapOf("username" to "123456789", "password" to "Indonesia1"))
                ?.postLogin(mapOf("username" to "123456789", "password" to "Indonesia1"))
                ?.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        logE("gagal")
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        logE("berhasil")
                    }

                })
        }
    }
}
