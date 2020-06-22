package id.sisi.postoko.view.ui.customer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import id.sisi.postoko.R
import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.activity_customer_edit_warehouse.toolbar

class EditCustomerWarehouseActivity : BaseActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_edit_warehouse)
        setSupportActionBar(toolbar)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_blue_ic, menu)
        menu?.findItem(R.id.menu_action_search)
        return super.onCreateOptionsMenu(menu)
    }
}