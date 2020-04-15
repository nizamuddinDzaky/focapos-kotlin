package id.sisi.postoko.view

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.model.User
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.utils.helper.Prefs
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.login.BottomSheetForgetPasswordFragment
import id.sisi.postoko.view.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val progressBar = CustomProgressBar()
    private val prefs: Prefs by lazy {
        Prefs(MyApp.instance)
    }
    private lateinit var viewModel: LoginViewModel
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (MyApp.prefs.isLogin) {
            startActivity(Intent(this, HomeActivity::class.java))
            return
        }
        setContentView(R.layout.activity_main)
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.getDataLogin().observe(this, Observer {
            it?.token?.let { token ->
                viewModel.getUserProfile(token)
            }
        })
        viewModel.getUserProfile().observe(this, Observer { user ->
            viewModel.getDataLogin().value?.token?.let { token ->
                successLogin(token, user)
            }
        })
        viewModel.getIsExecute().observe(this, Observer {
            if (it && !progressBar.isShowing()) {
                progressBar.show(this, "Silakan tunggu...")
            } else {
                progressBar.dismiss()
            }
        })
        viewModel.getMessage().observe(this, Observer {
            logE("error $it")
            it?.let {
                toast.showErrorL(it)
            }
        })

        val mandatory = listOf<EditText>(et_username, et_password)

        prefs.usernameLogin?.let {
            et_username?.setText(it)
        }
        layout_et_password?.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        prefs.passwordLogin?.let {
            layout_et_password?.endIconMode =
                if (it.isEmpty()) TextInputLayout.END_ICON_PASSWORD_TOGGLE else TextInputLayout.END_ICON_NONE
            et_password?.setText(it)
        }
        checkbox_remember_me?.isChecked =
            !(et_username.text.isNullOrEmpty() && et_password.text.isNullOrEmpty())
        btn_login_forget_password?.setOnClickListener {
            BottomSheetForgetPasswordFragment.show(supportFragmentManager)
        }

        btn_login?.setOnClickListener {
            if (!mandatory.validation()) {
                return@setOnClickListener
            }

            val username = et_username?.text?.toStr() ?: "demo"
            val password = et_password?.text?.toStr() ?: "123456789"
            val body = mapOf("username" to username, "password" to password)
            viewModel.postLogin(body) {
                val isRememberMe = checkbox_remember_me?.isChecked == true
                prefs.usernameLogin = if (isRememberMe) username else ""
                prefs.passwordLogin = if (isRememberMe) password else ""
            }
        }

        btn_register?.setOnClickListener{
            loopCrash()
        }
    }

    fun loopCrash(){
        loopCrash()
    }

    private fun successLogin(token: String, user: User?) {
        prefs.posToken = token
        MyApp.prefs.isLogin = true
        MyApp.prefs.posRoleId = user?.group_id
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
