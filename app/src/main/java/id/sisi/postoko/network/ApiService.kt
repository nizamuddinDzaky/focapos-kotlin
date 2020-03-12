package id.sisi.postoko.network

import android.annotation.SuppressLint
import id.sisi.postoko.model.*
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@JvmSuppressWildcards
interface ApiServices {
    @POST("auth/login")
    fun postLogin(@Body body: Map<String, String>): Call<BaseResponse<DataLogin>>

    @POST("sales_booking/add_payments")
    fun postAddPayment(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, String>
    ): Call<BaseResponse<DataLogin>>

    @POST("sales_booking/add_sales_booking")
    fun postAddSales(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @POST("purchases/add_gr_to_po")
    fun postAddGoodReceived(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, String>
    ): Call<BaseResponse<DataLogin>>

    @POST("sales_booking/add_deliveries_booking")
    fun postAddDelivery(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("auth/profile")
    fun getProfile(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataProfile>>

    @GET("warehouses/list_warehouses")
    fun getListWarehouse(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataWarehouse>>

    @GET("warehouses/detail_warehouses")
    fun getDetailWarehouse(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataWarehouse>>

    @GET("suppliers/list_suppliers")
    fun getListSupplier(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataSupplier>>

    @GET("customers/list_customers")
    fun getListCustomer(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataCustomer>>

    @GET("customers/detail_customers")
    fun getDetailCustomer(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataCustomer>>

    @GET("products/list_products")
    fun getListProduct(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataProduct>>

    @GET("sales_booking/list_sales_booking")
    fun getListSale(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataSales>>

    @GET("sales_booking/list_payments")
    fun getListSalePayment(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataPayment>>

    @GET("sales_booking/list_deliveries_booking")
    fun getListSaleDelivery(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataDelivery>>

    @GET("sales_booking/detail_sales_booking")
    fun getDetailSale(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataSaleDetail>>

    @GET("purchases/list_goods_received")
    fun getListGoodReceived(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataGoodsReceived>>

    @GET("purchases/detail_goods_received")
    fun getDetailGoodReceived(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataGoodsReceived>>

    companion object {
        private var retrofit: Retrofit? = null

//        private const val BASE_URL: String = "https://qp.forca.id/api/v1/distributor/"
        //private const val BASE_URL: String = "http://10.37.11.119:8282/api/v1/distributor/"
        private const val BASE_URL: String = "http://10.15.4.102:9090/api/v1/distributor/"

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
                        @SuppressLint("TrustAllX509TrustManager")
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
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
            val builder =
                OkHttpClient.Builder().connectionSpecs(arrayListOf(spec, ConnectionSpec.CLEARTEXT))
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
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