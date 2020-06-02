package id.sisi.postoko.view.ui.customer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import kotlinx.android.synthetic.main.activity_edit_customer.*
import kotlinx.android.synthetic.main.content_edit_customer.*
//import sun.awt.windows.ThemeReader.getPosition
import java.util.*


class EditCustomerActivity : AppCompatActivity() {

    var customer: Customer? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_customer)
        setSupportActionBar(toolbar_edit_customer)
        supportActionBar?.title = null

        customer = intent.extras?.getParcelable("customer")!!

        main_view_pager?.let {
            it.adapter = EditCustomerPagerAdapter(supportFragmentManager)
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }
}
