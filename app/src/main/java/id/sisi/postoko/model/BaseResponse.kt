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

data class DataSupplier(
    var total_supplier: Int?,
    var list_supplier: List<Supplier>?
)

data class DataProduct(
    var total_products: Int?,
    var list_products: List<Product>?
)