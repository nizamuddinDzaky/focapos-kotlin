package id.sisi.postoko.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.utils.helper.Prefs
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val progressBar = CustomProgressBar()
    private val prefs: Prefs by lazy {
        Prefs(MyApp.instance)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mandatory = listOf<EditText>(et_username, et_password)

        prefs.usernameLogin?.let {
            et_username?.setText(it)
        }
        prefs.passwordLogin?.let {
            et_password?.setText(it)
        }
        checkbox_remember_me?.isChecked =
            !(et_username.text.isNullOrEmpty() && et_password.text.isNullOrEmpty())

        btn_login?.setOnClickListener {
            if (!mandatory.validation()) {
                return@setOnClickListener
            }
            progressBar.show(this, "Silakan tunggu...")
            val username = et_username?.text?.toStr() ?: "123456789"
            val password = et_password?.text?.toStr() ?: "Indonesia1"
            val body = mapOf(
                "username" to username,
                "password" to password
            )
            ApiServices.getInstance()?.postLogin(body)?.exe(
                onFailure = { call, throwable ->
                    logE("gagal")
                },
                onResponse = { call, response ->
                    logE("berhasil")
                    tryMe {
                        if (response.body()?.code == 200) {
                            if (checkbox_remember_me?.isChecked == true) {
                                prefs.usernameLogin = username
                                prefs.passwordLogin = password
                            } else {
                                prefs.usernameLogin = ""
                                prefs.passwordLogin = ""
                            }
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                    }
                }) {
                progressBar.dialog.dismiss()
            }
        }
    }
}
