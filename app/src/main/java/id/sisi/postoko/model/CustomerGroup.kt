package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerGroup (
    var id: String,
    var name: String,
    var percent: String? = null,
    var client_id: String? = null,
    var flag: String? = null,
    var is_deleted: String? = null,
    var device_id: String? = null,
    var uuid: String? = null,
    var uui_app: String? = null,
    var company_id: String? = null,
    var kredit_limit: Double? = 0.0
) : Parcelable