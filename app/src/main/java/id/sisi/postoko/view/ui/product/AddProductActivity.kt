package id.sisi.postoko.view.ui.product

import android.os.Bundle
import id.sisi.postoko.R
import id.sisi.postoko.view.BaseActivity

class AddProductActivity : BaseActivity() {

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
