package id.sisi.postoko.view.ui.customer

//import sun.awt.windows.ThemeReader.getPosition
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.utils.BASE_URL
import id.sisi.postoko.utils.LoadImageFromUrl
import id.sisi.postoko.utils.URL_AVATAR_PROFILE
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.activity_edit_customer.*
import kotlinx.android.synthetic.main.content_edit_customer.*


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
            val loadImage = LoadImageFromUrl(iv_logo, this, R.drawable.toko2)
            loadImage.execute("$URL_AVATAR_PROFILE${it?.logo}")
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
