package id.sisi.postoko.model

data class MessageEvent(
    var isTokenExpired: Boolean = false,
    var message: String? = null
)