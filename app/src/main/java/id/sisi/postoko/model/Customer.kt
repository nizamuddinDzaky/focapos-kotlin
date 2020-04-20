package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    var address: String? = null,
    var award_points: String? = null,
    var cf1: String? = null,
    var cf2: String? = null,
    var cf3: String? = null,
    var cf4: String? = null,
    var cf5: String? = null,
    var cf6: String? = null,
    var city: String? = null,
//    var client_id: Any,
    var company: String? = null,
    var company_id: String? = null,
    var country: String? = null,
//    var created_at: Any,
    var customer_group_id: String? = null,
    var customer_group_name: String? = null,
//    var deposit_amount: Any,
//    var device_id: Any,
    var email: String? = null,
//    var flag: Any,
//    var flag_bk: Any,
    var group_id: String? = null,
    var group_name: String? = null,
    var id: String? = null,
//    var invoice_footer: Any,
    var is_active: String? = null,
//    var is_deleted: Any,
    var latitude: String? = null,
    var logo: String? = null,
    var longitude: String? = null,
//    var manager_area: Any,
//    var mtid: Any,
    var name: String? = null,
    var payment_term: String? = null,
    var phone: String? = null,
    var postal_code: String? = null,
    var price_group_id: Int? = null,
    var price_group_name: String? = null,
    var region: String? = null,
    var state: String? = null,
    var updated_at: String? = null,
//    var uuid: Any,
//    var uuid_app: Any,
    var vat_no: String? = null,
//    var village: Any
    var customer_name: String? = null,
    var customer_id: String? = null,
    var customer_company: String? = null
) : Parcelable {
    @IgnoredOnParcel
    var isSelected = false
}