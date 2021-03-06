package id.sisi.postoko.model

data class PurchaseItem(
    val client_id: Any,
    val purchase_id: Int,
    val deliveries_smig_id: String,
    val device_id: Any,
    val discount: Any,
    val flag: Any,
    val id: String,
    val is_deleted: Any,
    val item_discount: String,
    val item_tax: String,
    val net_unit_price: String,
    val product_code: String,
    val product_id: String,
    val product_name: String,
    val product_type: String,
    val product_unit_code: String,
    val product_unit_id: String,
    val quantity: String,
    val real_unit_cost: String,
    val serial_no: Any,
    val subtotal: String,
    val tax: String,
    val tax_rate_id: String,
    val unit_price: String,
    val unit_quantity: String,
    val uuid: Any,
    val uuid_app: Any,
    val warehouse_id: String
)