package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Warehouse(
    val address: String,
    val client_id: String,
    val code: String,
    val company_id: String,
    val device_id: String,
    val email: String,
    val flag: String,
    val id: String,
    val is_deleted: String,
    val map: String,
    val name: String,
    val phone: String,
    val price_group_id: String,
    val shipment_price_group_id: String,
    val uuid: String,
    val uuid_app: String
): Parcelable {
    @IgnoredOnParcel
    var isSelected = false
    var isDefault = false
}