package id.sisi.postoko.view.ui.customer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.KEY_ID_CUSTOMER
import id.sisi.postoko.utils.KEY_ID_SALES_BOOKING
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.MasterDetailViewModel
import id.sisi.postoko.view.ui.sales.EditSaleActivity
import kotlinx.android.synthetic.main.activity_customer_detail.*
import kotlinx.android.synthetic.main.content_detail_customer.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_delivery.*


@Suppress("UNREACHABLE_CODE")
class DetailCustomerActivity : AppCompatActivity() {
    var idCustomer: Int? = 0
    var customer: Customer? = null
    private lateinit var viewModelCustomer: MasterDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_detail)
        setSupportActionBar(toolbar_detail_customer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idCustomer = intent.getIntExtra(KEY_ID_CUSTOMER, 0)
        viewModelCustomer = ViewModelProvider(this).get(MasterDetailViewModel::class.java)
        viewModelCustomer.getDetailCustomer().observe(this, Observer {
            if (it != null) {
                customer = it
            }
            tv_customer_name_header.text = it?.company
            tv_customer_name.text = it?.company
            tv_customer_customer_group.text = it?.group_name
            tv_customer_npwp.text = it?.vat_no
            tv_customer_point.text = it?.award_points
            tv_customer_email.text = it?.email
            tv_customer_phone.text = it?.phone
            tv_customer_address.text = it?.address
            tv_customer_district.text = it?.state
            tv_customer_province.text = it?.country
            tv_customer_cf1.text = it?.cf1
            tv_customer_cf2.text = it?.cf2
            tv_customer_cf3.text = it?.cf3
            tv_customer_cf4.text = it?.cf4
            tv_customer_cf5.text = it?.cf5
        })
        viewModelCustomer.requestDetailCustomer(idCustomer ?: 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_sale, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            R.id.menu_edit_sale -> {

                val intent = Intent(this, EditCustomerActivity::class.java)
                intent.putExtra("customer", customer)

                startActivityForResult(intent, 1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}