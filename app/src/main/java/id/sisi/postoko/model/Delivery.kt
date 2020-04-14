package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Delivery(
    val address: String,
//    val attachment: Any,
    val created_at: String,
    val created_by: String,
    val customer: String,
    val date: String,
    val delivered_by: String,
    val delivered_date: String,
    val delivering_date: String,
//    val device_id: Any,
    val do_reference_no: String,
//    val flag: Any,
    val id: String,
//    val is_approval: Any,
//    val is_confirm: Any,
//    val is_deleted: Any,
//    val is_reject: Any,
    val note: String,
//    val receive_status: Any,
    val received_by: String,
//    val return_reference_no: Any,
    val sale_id: String,
    val sale_reference_no: String,
//    val spj_file: Any,
    val status: String,
    val updated_at: String,
    val updated_by: String
//    val uuid: Any,
//    val uuid_app: Any
) : Parcelable {
    @IgnoredOnParcel
    var deliveryItems: List<DeliveryItem>? = arrayListOf()
}