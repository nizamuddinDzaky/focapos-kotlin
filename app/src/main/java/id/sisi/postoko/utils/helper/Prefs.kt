package id.sisi.postoko.utils.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Prefs(context: Context) {
    companion object {
        private const val PREFS_FILENAME = "pos_toko_prefs"

        private const val KEY_TOKEN_LOGIN = "token_login"
        private const val KEY_USERNAME_LOGIN = "username_login"
        private const val KEY_PASSWORD_LOGIN = "password_login"
        private const val KEY_ISLOGIN = "islogin"
        private const val KEY_ROLE_ID = "roleIdAccess"
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    private fun String.putStringPref(value: String?) {
        val key = this
        sharedPrefs.edit { putString(key, value) }
    }

    private fun String.putIntPref(value: Int) {
        val key = this
        sharedPrefs.edit { putInt(key, value) }
    }

    private fun String.putBooleanPref(value: Boolean) {
        val key = this
        sharedPrefs.edit { putBoolean(key, value) }
    }

    var posToken: String?
        get() = sharedPrefs.getString(KEY_TOKEN_LOGIN, null)
        set(value) = KEY_TOKEN_LOGIN.putStringPref(value)

    //SuperAdmin (2), Admin Gudang (8), Kasir (5)
    var posRoleId: Int?
        get() = sharedPrefs.getInt(KEY_ROLE_ID, 0)
        set(value) = KEY_ROLE_ID.putIntPref(value ?: 0)

    var usernameLogin: String?
        get() = sharedPrefs.getString(KEY_USERNAME_LOGIN, null)
        set(value) = KEY_USERNAME_LOGIN.putStringPref(value)

    var passwordLogin: String?
        get() = sharedPrefs.getString(KEY_PASSWORD_LOGIN, null)
        set(value) = KEY_PASSWORD_LOGIN.putStringPref(value)

    var isLogin: Boolean
        get() = sharedPrefs.getBoolean(KEY_ISLOGIN, false)
        set(value) = KEY_ISLOGIN.putBooleanPref(value)
}