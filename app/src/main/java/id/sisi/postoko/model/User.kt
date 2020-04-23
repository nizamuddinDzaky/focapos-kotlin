package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val id : Int?,
    val last_ip_address : String?,
    val ip_address : String?,
    val auth_provider : String?,
    val username : String?,
    val password : String?,
    val salt : String?,
    val email : String?,
    val activation_code : String?,
    val forgotten_password_code : String?,
    val forgotten_password_time : String?,
    val remember_code : String?,
    val created_on : String?,
    val last_login : String?,
    val active : Int?,
    val first_name : String?,
    val last_name : String?,
    val company : String?,
    val phone : String?,
    val avatar : String?,
    val gender : String?,
    val group_id : Int?,
    val warehouse_id : Int?,
    val biller_id : Int?,
    val company_id : Int?,
    val show_cost : Int?,
    val show_price : Int?,
    val award_point : Int?,
    val view_right : Int?,
    val edit_right : Int?,
    val allow_discount : Int?,
    val device_id : Int?,
    val customer_group_id : String?,
    val supplier_id : String?,
    val sales_person_id : String?,
    val sales_person_ref : String?,
    val issupplier : String?,
    val manager_area : String?,
    val group_calluser : String?,
    val country : String?,
    val city : String?,
    val state : String?,
    val address : String?,
    val phone_is_verified : Int?,
    val phone_otp : Int?,
    val activated_at : String?,
    val registered_by : String?
): Parcelable {
    @IgnoredOnParcel
    var companyData: Customer? = null
}