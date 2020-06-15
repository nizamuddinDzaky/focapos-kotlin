package id.sisi.postoko.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.utils.SPLASH_TIME_OUT

class SplashScreenActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            if (MyApp.prefs.isLogin) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            startActivity(Intent(this,MainActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}