package id.sisi.postoko.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login?.setOnClickListener {
            //TODO - API Post Login
        }
    }
}
