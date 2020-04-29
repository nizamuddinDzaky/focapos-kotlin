package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SaleItem (
    val id : Int? = 0,
    val sale_id : Int? = 0,
    var product_id : Int? = 0,
    var product_code : String? = null,
    var product_name : String? = null,
    val product_type : String? = null,
    val option_id : String? = null,
    var net_unit_price : Double? = 0.0,
    var unit_price : Double? = 0.0,
    var quantity : Double? = 0.0,
    val warehouse_id : Int? = 0,
    val item_tax : Double? = 0.0,
    val tax_rate_id : Int? = 0,
    val tax : String? = null,
    var discount : Int? = 0,
    val item_discount : Double? = 0.0,
    var subtotal : Double? = 0.0,
    val serial_no : String? = null,
    val real_unit_price : Double? = 0.0,
    val sale_item_id : String? = null,
    val product_unit_id : Int? = 0,
    val product_unit_code : String? = null,
    val unit_quantity : Double? = 0.0,
    var sent_quantity : Double = 0.0,
    val client_id : String? = null,
    val flag : String? = null,
    val is_deleted : String? = null,
    val device_id : String? = null,
    val updated_at : String? = null,
    val uuid : String? = null,
    val uuid_app : String? = null
) : Parcelable