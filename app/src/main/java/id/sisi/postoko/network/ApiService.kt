package id.sisi.postoko.network

import android.annotation.SuppressLint
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.*
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
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

    @PUT("sales_booking/edit_payments")
    fun putEditPayment(
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

    @PUT("auth/update_profile")
    fun putProfile(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, Any>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataProfile>>

    @PUT("auth/update_password")
    fun putChangePassword(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataProfile>>

    @POST("auth/forgot_password")
    fun postResetPassword(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("warehouses/list_warehouses")
    fun getListWarehouse(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataWarehouse>>

    @GET("warehouses/detail_warehouses")
    fun getDetailWarehouse(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataWarehouse>>

    @GET("suppliers/list_suppliers")
    fun getListSupplier(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataSupplier>>

    @GET("suppliers/detail_suppliers")
    fun getDetailSupplier(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataSupplier>>

    @GET("customers/list_customers")
    fun getListCustomer(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataCustomer>>

    @GET("customers/customers_groups")
    fun getListCustomerGroup(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataCustomerGroup>>

    @GET("customers/price_groups")
    fun getListPriceGroup(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataPriceGroup>>

    @GET("customers/detail_customers")
    fun getDetailCustomer(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataCustomer>>

    @POST("customers/add_customers")
    fun postCustomers(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @PUT("customers/update_customers")
    fun putEditCustomers(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("products/list_products")
    fun getListProduct(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataProduct>>

    @GET("Sales_booking/list_transaction_sales_booking")
    fun getPieChartData(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any?> = mapOf()
    ): Call<BaseResponse<DataPieChart>>

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

    @GET("purchases/list_goods_received_paging")
    fun getListGoodReceivedPaging(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataGoodsReceived>>

    @GET("purchases/detail_goods_received")
    fun getDetailGoodReceived(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataGoodsReceived>>

    @PUT("sales_booking/edit_sales_booking")
    fun putEditSales(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("sales_booking/detail_deliveries")
    fun getDetailDeliveries(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataDeliveryDetail>>

    @POST("customers/add_price_group")
    fun postAddPriceGroup(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @POST("customers/add_or_edit_customer_to_price_group")
    fun postAddCustomerToPriceGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @PUT("customers/update_price_group")
    fun putEditPriceGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @PUT("customers/update_product_in_price_group")
    fun putEditProductPrice(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("customers/list_customer_member_of_price_group")
    fun getListCustomerPriceGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataCustomerSelected>>

    @GET("customers/group_product_in_prices_group")
    fun getListProductPrice(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataProductPrice>>

    @POST("customers/add_customer_group")
    fun postAddCustomerGroup(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @POST("customers/add_or_edit_customer_to_customer_group")
    fun postAddCustomerToCustoemrGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @PUT("customers/update_customer_group")
    fun putEditCustomerGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("customers/list_customer_member_of_customer_group")
    fun getListCustomerCustomerGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataCustomerSelected>>

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
                .addInterceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    if (response.code() == 401) {
                        MyApp.prefs.deleteLogout()
                        EventBus.getDefault().post(MessageEvent(isTokenExpired = true))
                    }
                    return@addInterceptor response
                }
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