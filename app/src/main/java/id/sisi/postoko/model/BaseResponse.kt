package id.sisi.postoko.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    var status: String?,
    var message: String?,
    var data: T?,
    var code: Int?
)

data class Response<T>(
    var delivery: T?,
    var payment: T?,
    var sale: T?
)

data class ResponseData(
    var id: String?,
    var reference_no: String?,
    var do_reference_no: String?,
    var so_reference_no: String?,
    var return_reference_no: String?
)

data class DataLogin(
    var id: String?,
    var user_id: String?,
    var company_id: String?,
    var token: String?
)

data class DataProfile(
    var user: User?,
    var company: Customer?
)

data class DataSyncCustomerToBK(
    var total_customer_data: String?
)

data class DataDaerah(
    var province_code: String?,
    var kabupaten_code: String?,
    var kecamatan_code: String?,
    var province_name: String?,
    var kabupaten_name: String?,
    var kecamatan_name: String?,
    var id_wilayah:String?
)

data class DataTermOfPayment(
    var description: String?,
    var duration: String?
)

data class DataResponseTOP(
    var term_of_payment: List<DataTermOfPayment>
)

data class DataCustomer(
    var total_supplier: Int?,
    var customer: Customer?,
    var list_customers: List<Customer>?
)

data class DetailProduct(
    var product: Product?,
    var warehouses: List<Warehouse>?
)

data class DataWarehouseCustomer(
    var warehouses_selected: List<Warehouse>?,
    var warehouses: List<Warehouse>?,
    var warehouses_default: List<Warehouse>?
)

data class DataCustomerSelected(
    var total_list_customer: Int?,
    var list_customer: List<Customer>?,
    var customer_selected: List<Customer>?
)

data class DataCustomerGroup(
    var total_supplier: Int?,
    var customer_groups: List<CustomerGroup>?
)

data class DataPriceGroup(
    var total_price_groups: Int?,
    var price_groups: List<PriceGroup>?
)

data class DataWarehouse(
    var total_warehouses: Int?,
    @SerializedName("detail_warehouses", alternate = ["detail_warehouse", "warehouse"])
    var warehouse: Warehouse?,
    var list_warehouses: List<Warehouse>?
)

data class DataSupplier(
    var total_suppliers: Int?,
    @SerializedName("detail_supplier", alternate = ["supplier"])
    var supplier: Supplier?,
    var list_suppliers: List<Supplier>?
)

data class DataProduct(
    var total_products: Int?,
    var list_products: List<Product>?
)

data class DataSales(
    var total_sales_booking: Int?,
    var list_sales_booking: List<Sales>?
)

data class DataSaleDetail(
    var sale: Sales?,
    var sale_items: List<SaleItem>?
)

data class DataGoodsReceived(
    var total_goods_received: Int?,
    var good_received: GoodReceived?,
    var good_received_items: List<PurchaseItem>?,
    var list_goods_received: List<GoodReceived>?
)

data class DataPayment(
    var total_payments: Int?,
    var list_payments: List<Payment>?
)

data class DataDelivery(
    var total_deliveries: Int?,
    var list_deliveries: List<Delivery>?,
    var delivery: Delivery?,
    var total_deliveries_booking: Int?,
    var list_deliveries_booking: List<Delivery>?
)

data class DataDeliveryDetail(
    var delivery: Delivery?,
    var delivery_items: List<DeliveryItem>
)

data class DataPieChart(
    var reserved: String?,
    var closed: String?,
    var pending: String?
)

data class DataProductPrice(
    var total_group_product_price: Int?,
    var group_product_price: List<Product>
)