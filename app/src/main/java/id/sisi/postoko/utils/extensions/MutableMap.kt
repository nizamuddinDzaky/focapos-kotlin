package id.sisi.postoko.utils.extensions


fun MutableMap<String, String>.putIfDataNotNull(
    key: String,
    value: String?,
    clearIfContainsButNull: Boolean = false,
    oldValue: String? = null
) {
    value?.let {
        if (it != oldValue) this[key] = it
    }
    if (clearIfContainsButNull && this.containsKey(key)) {
        this.remove(key)
    }
}