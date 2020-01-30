package id.sisi.postoko.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.sisi.postoko.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login?.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            //TODO - API Post Login
        }
    }
}
