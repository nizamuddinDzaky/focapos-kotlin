package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerGroup (
    val id: String,
    val name: String,
    val percent: String,
    val client_id: String,
    val flag: String,
    val is_deleted: String,
    val device_id: String,
    val uuid: String,
    val uui_app: String,
    val company_id: String,
    val kredit_limit: String
) : Parcelable