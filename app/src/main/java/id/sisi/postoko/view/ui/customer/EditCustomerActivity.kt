package id.sisi.postoko.view.ui.customer

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.BASE_URL
import id.sisi.postoko.utils.DEFAULT_LOGO_CUSTOMER
import id.sisi.postoko.utils.LoadImageFromUrl
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

        intent.extras?.getParcelable<Customer>("customer").let{
            customer = it
            if(!TextUtils.isEmpty(it?.logo) && !it?.logo?.equals(DEFAULT_LOGO_CUSTOMER)!!){
                val loadImage = LoadImageFromUrl(iv_logo)
                loadImage.execute("$BASE_URL/assets/uploads/avatars/thumbs/${it.logo}")
            }
        }

        main_view_pager?.let {
            it.adapter = EditCustomerPagerAdapter(supportFragmentManager)
            tabs_main_pagers?.setupWithViewPager(it)
        }

        iv_logo.setOnClickListener {
            BottomSheetUpdateLogoCustomer.show(
                supportFragmentManager,
                customer?.id
            )

            BottomSheetUpdateLogoCustomer.listener = {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}
