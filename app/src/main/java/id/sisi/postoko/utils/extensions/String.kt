package id.sisi.postoko.utils.extensions

fun Double.format(digits: Int) = "%.${digits}f".format(this)