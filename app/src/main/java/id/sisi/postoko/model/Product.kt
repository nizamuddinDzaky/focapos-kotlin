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
    var code: String,
//    val company_id: String,
    val cost: String? = null,
//    val credit_price: String,
//    val details: String,
//    val e_minqty: String,
//    val end_date: Any,
//    val `file`: Any,
    val id: String,
    val image: String? = null,
//    val is_deleted: Any,
//    val is_retail: String,
//    val item_id: String,
//    val mtid: Any,
    val name: String,
    val type: String? = null,
    var price: Int? = null,
//
//    val price_public: String,
//    val product_details: String,
//    val promo_price: Any,
//    val promotion: Any,
//    val `public`: Any,
//    val purchase_unit: String,
    var quantity: Double? = 0.0,
    var quantity_booking: Double? = 0.0,
//    val sale_unit: String,
//    val start_date: Any,
//    val subcategory_id: Any,
//    val supplier1: String? = null,
//    val supplier1_part_no: String,
//    val supplier1price: Any,
//    val supplier2: String? = null,
//    val supplier2_part_no: String,
//    val supplier2price: Any,
//    val supplier3: String? = null,
//    val supplier3_part_no: String,
//    val supplier3price: Any,
//    val supplier4: String? = null,
//    val supplier4_part_no: String,
//    val supplier4price: Any,
//    val supplier5: String? = null,
//    val supplier5_part_no: String,
//    val supplier5price: Any,
    var tax_method: Int = 0,
    var tax_rate: Int = 0,
//    val thumb_image: String? = null,
//    val track_quantity: String,
//    val type: String,
    var unit: String? = null,
    var orderQty: Int = 0,
    var discount : Int = 0,
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
    var category_name: String? = null,
    var categori_name: String? = null,
    var brand_name: String? = null,
    var is_multiple: Int? = null,
    var priceGroup_id: Int? = null,
    var supplier: String? = null,
    /*=======*/

    var product_tax: Double? = null,
    var expiry: String? = null
) : Parcelable {
    @IgnoredOnParcel
    var isCollapse = false
    @IgnoredOnParcel
    var isSelected = false
    var warehouse: List<Warehouse>? = arrayListOf()
}