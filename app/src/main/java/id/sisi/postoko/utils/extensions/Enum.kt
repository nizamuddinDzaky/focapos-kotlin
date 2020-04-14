package id.sisi.postoko.utils.extensions

inline fun <reified T: Enum<T>> T.tryValue(value: String?): T? {
    try {
        value?.let { return enumValueOf<T>(value.toUpperCase()) }
    } catch (e: Exception) {

    }
    return null
}