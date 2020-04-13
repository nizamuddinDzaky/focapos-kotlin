package id.sisi.postoko.view.ui.pricegroup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListProductPriceGroup
import id.sisi.postoko.model.BaseResponse
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.DataCustomer
import id.sisi.postoko.model.PriceGroup
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.helper.fromJson
import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.detail_price_group.*

class DetailPriceGroupActivity : BaseActivity() {
    private var priceGroup: PriceGroup? = PriceGroup(name = "ForcaPoS")
    private lateinit var adapter: ListProductPriceGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_price_group)
        displayHomeEnable()
        disableElevation()
        supportActionBar?.title = getString(R.string.txt_title_detail_price_group)
        intent?.getParcelableExtra<PriceGroup>(KEY_PRICE_GROUP)?.let {
            priceGroup = it
        }
        setupRecycleView()
        val jsonHelper =
            Gson().fromJson<BaseResponse<DataCustomer>>(this, "DummyListCustomer.json")
        val firstListCustomer = (jsonHelper.data?.list_customers) ?: listOf()
        adapter.updateSalesData(firstListCustomer)
    }

    private fun setupRecycleView() {
        adapter = ListProductPriceGroup()
        rv_list_product?.layoutManager = LinearLayoutManager(this)
        rv_list_product?.setHasFixedSize(false)
        rv_list_product?.adapter = adapter
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity,
            customerGroup: PriceGroup
        ) {
            val page = Intent(fragmentActivity, DetailPriceGroupActivity::class.java)
            page.putExtra(KEY_PRICE_GROUP, customerGroup)
            fragmentActivity.startActivity(page)
        }
    }
}