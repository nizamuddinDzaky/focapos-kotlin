package id.sisi.postoko.view.ui.product

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.loadImage
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.customer.CustomerPagerAdapter
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.activity_detail_product.iv_logo
import kotlinx.android.synthetic.main.activity_detail_product.main_view_pager
import kotlinx.android.synthetic.main.activity_detail_product.tabs_main_pagers
import kotlinx.android.synthetic.main.content_detail_customer.*
import kotlinx.android.synthetic.main.content_edit_customer.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_form_payment.*
import kotlinx.android.synthetic.main.fragment_home.*

class DetailProductActivity: AppCompatActivity() {
    var product = MutableLiveData<Product>()
    private lateinit var mViewModelProduct: ProductViewModel
    private val typeface = TypeFace()
    private val pages = listOf(
        DetailProductFragment(),
        QuantityProductFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_product)
        setSupportActionBar(toolbar_add_customer)
        supportActionBar?.title = null


        mViewModelProduct = ViewModelProvider(this).get(ProductViewModel::class.java)

        mViewModelProduct.getDetailProducts().observe(this, Observer {
            product.postValue(it)
            tv_product_name.text = it.name
            /*val loadImage = LoadImageFromUrl(iv_logo, this, R.drawable.toko2)
            loadImage.execute("${it?.image}")*/
            iv_logo.loadImage("${it?.image}", R.drawable.toko2)
        })

        mViewModelProduct.getDetailProduct(intent.getStringExtra(KEY_PRODUCT_ID)?.toInt() ?: 0)
        tabs_main_pagers.setSelectedTabIndicatorColor(Color.parseColor("#004C97"))
        main_view_pager?.let {
            it.adapter = CustomerPagerAdapter(supportFragmentManager, pages)
            tabs_main_pagers?.setupWithViewPager(it)
        }
        assets?.let { typeface.fontTab(tabs_main_pagers, "robot_font/Roboto-Regular.ttf", it) }
    }
}