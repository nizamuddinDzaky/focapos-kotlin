package id.sisi.postoko.network

import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.DataLogin
import id.sisi.postoko.model.DataProfile
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

interface ApiServices {
    @POST("api/v1/retailer/auth/login")
    fun postLogin(@Body body: Map<String, String>): Call<BaseResponse<DataLogin>>

    @GET("api/v1/retailer/auth/profile")
    fun getProfile(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataProfile>>

    companion object {
        private var retrofit : Retrofit? = null

        private const val BASE_URL: String = "https://qp.forca.id/"

        fun getInstance(): ApiServices? {
            retrofit ?: synchronized(this) {
                retrofit = retrofit ?: buildRetrofit()
            }

            return retrofit?.create(ApiServices::class.java)
        }

        private fun getUnsafeOkHttpClient(): OkHttpClient {
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_0)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA
                )
                .build()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder().connectionSpecs(arrayListOf(spec))
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            builder.addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)

            return builder.build()
        }

        private fun buildRetrofit() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()
    }
}