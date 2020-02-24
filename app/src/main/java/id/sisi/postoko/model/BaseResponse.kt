package id.sisi.postoko.model

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
    var user: User?
)

data class DataCustomer(
    var total_supplier: Int?,
    var list_customers: List<Customer>?
)

data class DataWarehouse(
    var total_warehouses: Int?,
    var list_warehouses: List<Warehouse>?
)

data class DataSupplier(
    var total_suppliers: Int?,
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
    var list_goods_received: List<GoodReceived>?
)

data class DataPayment(
    var total_payments: Int?,
    var list_payments: List<Payment>?
)

data class DataDelivery(
    var total_deliveries: Int?,
    var list_deliveries: List<Delivery>?
)