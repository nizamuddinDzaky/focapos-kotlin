package id.sisi.postoko.network

import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import id.sisi.postoko.BuildConfig
import id.sisi.postoko.MyApp
import id.sisi.postoko.model.*
import id.sisi.postoko.model.Response
import id.sisi.postoko.utils.BASE_URL
import id.sisi.postoko.utils.BASE_URL_DIRTY
import okhttp3.*
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
    @POST("api/v1/distributor/auth/login")
    fun postLogin(@Body body: Map<String, String>): Call<BaseResponse<DataLogin>>

    @POST("api/v1/distributor/sales_booking/add_payments")
    fun postAddPayment(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, String>
    ): Call<BaseResponse<Response<ResponseData>>>

    @PUT("api/v1/distributor/sales_booking/edit_payments")
    fun putEditPayment(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, String>
    ): Call<BaseResponse<Response<ResponseData>>>

    @Multipart
    @POST("api/v1/distributor/sales_booking/upload_file_payment")
    fun postUploadFilePayment(
        @Part file: MultipartBody.Part,
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, Any>
    ): Call<BaseResponse<DataLogin>>

    @POST("api/v1/distributor/sales_booking/add_sales_booking")
    fun postAddSales(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<Response<ResponseData>>>

    @POST("api/v1/distributor/sales_booking/close_sales_booking")
    fun postCloseSale(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<Response<ResponseData>>>

    @POST("api/v1/distributor/purchases/add_gr_to_po")
    fun postAddGoodReceived(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @POST("api/v1/distributor/sales_booking/add_deliveries_booking")
    fun postAddDelivery(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<Response<ResponseData>>>

    @POST("api/v1/distributor/sales_booking/add_return_deliveries")
    fun postReturnDeliv(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<Response<ResponseData>>>

    @PUT("api/v1/distributor/sales_booking/edit_deliveries_booking")
    fun putEditDeliv(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, Any>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<Response<ResponseData>>>

    @Multipart
    @POST("api/v1/distributor/sales_booking/upload_file_delivery")
    fun postUploadFileDelivery(
        @Part file: MultipartBody.Part,
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, Any>
    ): Call<BaseResponse<DataLogin>>

    @GET("api/Local/list_province")
    fun getProvince(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<List<DataDaerah>>>

    @GET("api/Local/list_city")
    fun getCity(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<List<DataDaerah>>>

    @GET("api/Local/list_states")
    fun getStates(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<List<DataDaerah>>>

    @GET("api/v1/distributor/auth/profile")
    fun getProfile(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataProfile>>

    @PUT("api/v1/distributor/auth/update_profile")
    fun putProfile(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, Any>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataProfile>>

    @PUT("api/v1/distributor/auth/update_password")
    fun putChangePassword(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataProfile>>

    @Multipart
    @POST("api/v1/distributor/auth/update_avatar")
    fun postUploadAvatarProfile(
        @Part file: MultipartBody.Part,
        @HeaderMap headerMap: Map<String, String>
    ): Call<BaseResponse<DataProfile>>

    @POST("api/v1/distributor/auth/forgot_password")
    fun postResetPassword(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("api/v1/distributor/warehouses/list_warehouses")
    fun getListWarehouse(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataWarehouse>>

    @GET("api/v1/distributor/warehouses/detail_warehouses")
    fun getDetailWarehouse(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataWarehouse>>

    @GET("api/v1/distributor/suppliers/list_suppliers")
    fun getListSupplier(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataSupplier>>

    @GET("api/v1/distributor/suppliers/detail_suppliers")
    fun getDetailSupplier(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataSupplier>>

    @GET("api/v1/distributor/customers/list_customers")
    fun getListCustomer(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataCustomer>>

    @GET("api/v1/distributor/customers/customers_groups")
    fun getListCustomerGroup(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataCustomerGroup>>

    @GET("api/v1/distributor/customers/price_groups")
    fun getListPriceGroup(@HeaderMap headerMap: Map<String, String>): Call<BaseResponse<DataPriceGroup>>

    @GET("api/v1/distributor/customers/detail_customers")
    fun getDetailCustomer(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataCustomer>>

    @GET("api/v1/distributor/Customers/list_customer_warehouse")
    fun getSelectedWarehouse(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataWarehouseCustomer>>

    @POST("api/v1/distributor/customers/add_customers")
    fun postCustomers(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @PUT("api/v1/distributor/customers/update_customers")
    fun putEditCustomers(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @Multipart
    @POST("api/v1/distributor/customers/upload_file_customer")
    fun postUploadLogoCustomer(
        @Part file: MultipartBody.Part,
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, Any>
    ): Call<BaseResponse<DataLogin>>

    @GET("api/v1/distributor/products/list_products")
    fun getListProduct(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, Any>
    ): Call<BaseResponse<DataProduct>>

    @GET("api/v1/distributor/Sales_booking/list_transaction_sales_booking")
    fun getPieChartData(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any?> = mapOf()
    ): Call<BaseResponse<DataPieChart>>

    @GET("api/v1/distributor/sales_booking/list_sales_booking")
    fun getListSale(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataSales>>

    @GET("api/v1/distributor/sales_booking/list_payments")
    fun getListSalePayment(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataPayment>>

    @GET("api/v1/distributor/sales_booking/list_deliveries_booking")
    fun getListSaleDelivery(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataDelivery>>

    @GET("api/v1/distributor/sales_booking/detail_sales_booking")
    fun getDetailSale(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataSaleDetail>>

    @GET("api/v1/distributor/purchases/list_goods_received")
    fun getListGoodReceived(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataGoodsReceived>>

    @GET("api/v1/distributor/purchases/list_goods_received_paging")
    fun getListGoodReceivedPaging(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataGoodsReceived>>

    @GET("api/v1/distributor/purchases/detail_goods_received")
    fun getDetailGoodReceived(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataGoodsReceived>>

    @PUT("api/v1/distributor/sales_booking/edit_sales_booking")
    fun putEditSales(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("api/v1/distributor/sales_booking/detail_deliveries")
    fun getDetailDeliveries(
        @HeaderMap headerMap: Map<String, Any>,
        @QueryMap params: Map<String, Any> = mapOf()
    ): Call<BaseResponse<DataDeliveryDetail>>

    @POST("api/v1/distributor/customers/add_price_group")
    fun postAddPriceGroup(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @POST("api/v1/distributor/customers/add_or_edit_customer_to_price_group")
    fun postAddCustomerToPriceGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @PUT("api/v1/distributor/customers/update_price_group")
    fun putEditPriceGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @PUT("api/v1/distributor/customers/update_product_in_price_group")
    fun putEditProductPrice(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("api/v1/distributor/customers/list_customer_member_of_price_group")
    fun getListCustomerPriceGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataCustomerSelected>>

    @GET("api/v1/distributor/customers/group_product_in_prices_group")
    fun getListProductPrice(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataProductPrice>>

    @POST("api/v1/distributor/customers/add_customer_group")
    fun postAddCustomerGroup(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @POST("api/v1/distributor/customers/add_or_edit_customer_to_customer_group")
    fun postAddCustomerToCustoemrGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @POST("api/v1/distributor/Customers/sync_customer_to_bk")
    fun postSyncCustomerToBK(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataSyncCustomerToBK>>

    @PUT("api/v1/distributor/customers/update_customer_group")
    fun putEditCustomerGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf(),
        @Body body: Map<String, Any?>
    ): Call<BaseResponse<DataLogin>>

    @GET("api/v1/distributor/customers/list_customer_member_of_customer_group")
    fun getListCustomerCustomerGroup(
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = mapOf()
    ): Call<BaseResponse<DataCustomerSelected>>

    @GET("api/v1/distributor/products/list_product_sales")
    fun getListProductSales(
        @HeaderMap headers: Map<String, String>,
        @QueryMap params: Map<String, String>
    ): Call<BaseResponse<DataProduct>>

    @GET("api/v1/distributor/products/detail_products")
    fun getDetailProduct(
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, String>): Call<BaseResponse<DetailProduct>>

    companion object {
        private var retrofit: Retrofit? = null

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

        private fun buildRetrofit() : Retrofit{
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val url = if (BuildConfig.DEBUG) BASE_URL_DIRTY else BASE_URL
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient())
                .build()
        }

    }

}