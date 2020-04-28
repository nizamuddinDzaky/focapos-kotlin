package id.sisi.postoko.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    var status: String?,
    var message: String?,
    var data: T?,
    var code: Int?
)

data class DataLogin(
    var user_id: String?,
    var company_id: String?,
    var token: String?
)

data class DataProfile(
    var user: User?,
    var company: Customer?
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

data class DataCustomer(
    var total_supplier: Int?,
    var customer: Customer?,
    var list_customers: List<Customer>?
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