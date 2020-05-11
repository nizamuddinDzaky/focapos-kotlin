package id.sisi.postoko.view

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.model.User
import id.sisi.postoko.utils.TypeFace
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.showErrorL
import id.sisi.postoko.utils.extensions.toStr
import id.sisi.postoko.utils.extensions.validation
import id.sisi.postoko.utils.helper.Prefs
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.login.BottomSheetForgetPasswordFragment
import id.sisi.postoko.view.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val progressBar = CustomProgressBar()
    private val typeface = TypeFace()
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

        typeface.typeFace("robot_font/Roboto-Bold.ttf",tv_email, assets)
        typeface.typeFace("robot_font/Roboto-Bold.ttf",tv_password, assets)
        typeface.typeFace("robot_font/Roboto-Bold.ttf",btn_login, assets)
        typeface.typeFace("robot_font/Roboto-Bold.ttf",tv_sign_up, assets)
        typeface.typeFace("robot_font/Roboto-Regular.ttf",cb_show_password, assets)
        typeface.typeFace("robot_font/Roboto-Regular.ttf",checkbox_remember_me, assets)
        typeface.typeFace("robot_font/Roboto-Regular.ttf",btn_login_forget_password, assets)
        typeface.typeFace("robot_font/Roboto-Regular.ttf",tv_belum_punya_akun, assets)

        cb_show_password.setOnClickListener {
            if (cb_show_password.isChecked) {
                et_password.transformationMethod = HideReturnsTransformationMethod.getInstance();
            } else {
                et_password.transformationMethod = PasswordTransformationMethod.getInstance();
            }
        }

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
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        val mandatory = listOf<EditText>(et_username, et_password)

        prefs.usernameLogin?.let {
            et_username?.setText(it)
        }
//        layout_et_password?.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
//        prefs.passwordLogin?.let {
//            layout_et_password?.endIconMode =
//                if (it.isEmpty()) TextInputLayout.END_ICON_PASSWORD_TOGGLE else TextInputLayout.END_ICON_NONE
//            et_password?.setText(it)
//        }

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
    }

    private fun successLogin(token: String, user: User?) {
        prefs.posToken = token
        MyApp.prefs.isLogin = true
        MyApp.prefs.posRoleId = user?.group_id
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
