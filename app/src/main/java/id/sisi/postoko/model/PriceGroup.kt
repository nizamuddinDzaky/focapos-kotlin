package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceGroup (
    var id: Int,
    var name: String,
    var client_id: String,
    var flag: Int,
    var is_deleted: Int,
    var device_id: String,
    var uuid: String,
    var uui_app: String,
    var company_id: Int,
    var warehouse_id: Int
) : Parcelable