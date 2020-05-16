package id.sisi.postoko.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
//    val alert_quantity: String,
//    val barcode_symbology: String,
//    val brand: String,
//    val category_id: String,
//    val cf1: String,
//    val cf2: String,
//    val cf3: String,
//    val cf4: String,
//    val cf5: String,
//    val cf6: String,
    val code: String,
//    val company_id: String,
//    val cost: String,
//    val credit_price: String,
//    val details: String,
//    val e_minqty: String,
//    val end_date: Any,
//    val `file`: Any,
    val id: String,
//    val image: String,
//    val is_deleted: Any,
//    val is_retail: String,
//    val item_id: String,
//    val mtid: Any,
    val name: String,
    val price: Int,
//
//    val price_public: String,
//    val product_details: String,
//    val promo_price: Any,
//    val promotion: Any,
//    val `public`: Any,
//    val purchase_unit: String,
//    val quantity: String,
//    val quantity_booking: Any,
//    val sale_unit: String,
//    val start_date: Any,
//    val subcategory_id: Any,
//    val supplier1: String,
//    val supplier1_part_no: String,
//    val supplier1price: Any,
//    val supplier2: String,
//    val supplier2_part_no: String,
//    val supplier2price: Any,
//    val supplier3: String,
//    val supplier3_part_no: String,
//    val supplier3price: Any,
//    val supplier4: String,
//    val supplier4_part_no: String,
//    val supplier4price: Any,
//    val supplier5: String,
//    val supplier5_part_no: String,
//    val supplier5price: Any,
//    val tax_method: Any,
//    val tax_rate: Any,
//    val thumb_image: String,
//    val track_quantity: String,
//    val type: String,
    var unit: String? = null,
    var sale_qty: Int = 0,
//    val uuid: Any,
//    val uuid_app: Any,
//    val warehouse: Any,
//    val weight: String,
    /*Product Price Group*/
    var product_name: String? = null,
    var product_code: String? = null,
    var price_kredit: Int? = 0,
    var min_order: Int = 0,
    var unit_name: String? = null,
    var is_multiple: Int? = null,
    var priceGroup_id: Int? = null
    /*=======*/
) : Parcelable {
    @IgnoredOnParcel
    var isCollapse = false
    @IgnoredOnParcel
    var isSelected = false
}