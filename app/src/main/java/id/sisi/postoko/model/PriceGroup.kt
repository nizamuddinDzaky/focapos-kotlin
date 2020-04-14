package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceGroup(
    var id: Int? = 0,
    var name: String? = "~",
    var client_id: String? = null,
    var flag: Int? = null,
    var is_deleted: Int? = null,
    var device_id: String? = null,
    var uuid: String? = null,
    var uui_app: String? = null,
    var company_id: Int? = null,
    var warehouse_id: Int? = 0,
    var warehouse_name: String? = null
) : Parcelable