package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeliveryItem (
    var id : Int? = 0,
    var delivery_id: Int? = 0,
    var sale_id : Int? = 0,
    var product_id: Int? = 0,
    var product_code: String? = null,
    var product_name: String? = null,
    var product_type: String? = null,
    var quantity_ordered: Double? = 0.0,
    var quantity_sent: Double? = 0.0,
    var good_quantity: Double? = 0.0,
    var bad_quantity: Double? = 0.0,
    var product_unit_id: Int? = 0,
    var product_unit_code: String? = null,
    var client_id: String? = null,
    var flag: Int? = 0,
    var is_deleted: Int? = 0,
    var device_id: String? = null,
    var uuid: String? = null,
    var uuid_app: String? = null,
    var updated_at : String,
    var created_at : String,
    var delivery_items_id: Int? = 0,
    var all_sent_qty: Double? = 0.0,
    var warehouse_id: Int? = 0
) : Parcelable