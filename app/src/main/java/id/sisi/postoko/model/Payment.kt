package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Payment(
    val amount: Double,
//    val amount_dist: Any,
//    val approval_code: Any,
    val attachment: String,
//    val billing_id: Any,
//    val c_payment_id: Any,
    val cc_holder: String,
    val cc_month: String,
    val cc_no: String,
    val cc_type: String,
    val cc_year: String,
    val cheque_no: String,
//    val client_id: Any,
    val company_id: String,
//    val consignment_id: Any,
    val created_by: String,
//    val currency: Any,
    val date: String,
//    val date_dist: Any,
//    val device_id: Any,
//    val flag: Any,
    val id: String,
//    val id_temp: Any,
//    val is_deleted: Any,
    val note: String,
    val paid_by: String,
    val pos_balance: String,
    val pos_paid: String,
//    val pos_payment_request_id: Any,
//    val purchase_id: Any,
    val reference_dist: String,
    val reference_no: String,
//    val return_id: Any,
    val sale_id: String,
//    val sales_order_id: Any,
//    val transaction_id: Any,
    val type: String,
    val url_image: String
//    val uuid: Any,
//    val uuid_app: Any
) : Parcelable