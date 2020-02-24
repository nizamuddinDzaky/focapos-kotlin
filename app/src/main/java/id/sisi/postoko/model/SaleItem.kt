package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SaleItem (
    val id : Int,
    val sale_id : Int,
    val product_id : Int,
    val product_code : String,
    val product_name : String,
    val product_type : String,
    val option_id : String,
    val net_unit_price : Double,
    val unit_price : Double,
    val quantity : Double,
    val warehouse_id : Int,
    val item_tax : Double,
    val tax_rate_id : Int,
    val tax : String,
    val discount : Int,
    val item_discount : Double,
    val subtotal : Double,
    val serial_no : String,
    val real_unit_price : Double,
    val sale_item_id : String,
    val product_unit_id : Int,
    val product_unit_code : String,
    val unit_quantity : Double,
    val sent_quantity : Double,
    val client_id : String,
    val flag : String,
    val is_deleted : String,
    val device_id : String,
    val updated_at : String,
    val uuid : String,
    val uuid_app : String
) : Parcelable