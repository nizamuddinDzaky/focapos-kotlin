package id.sisi.postoko.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.sisi.postoko.R
import id.sisi.postoko.view.ui.addproduct.AddProductFragment

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddProductFragment.newInstance())
                .commitNow()
        }
    }

}