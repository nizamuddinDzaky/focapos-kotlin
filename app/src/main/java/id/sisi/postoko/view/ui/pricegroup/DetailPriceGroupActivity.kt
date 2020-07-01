package id.sisi.postoko.view.ui.pricegroup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListProductPriceGroupAdapter
import id.sisi.postoko.model.*
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_PRICE_GROUP
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.detail_price_group.*

class DetailPriceGroupActivity : BaseActivity() {
    private var priceGroup: PriceGroup? = PriceGroup(name = "ForcaPoS")
    private lateinit var adapter: ListProductPriceGroupAdapter
    private lateinit var vmPriceGroup: PriceGroupViewModel
    private val progressBar = CustomProgressBar()
    private val myDialog = MyDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_price_group)
        displayHomeEnable()
        disableElevation()
        intent?.getParcelableExtra<PriceGroup>(KEY_PRICE_GROUP)?.let {
            priceGroup = it
        }

        supportActionBar?.title = getString(R.string.txt_title_detail_price_group)
        supportActionBar?.subtitle = priceGroup?.name

        setupRecycleView()

        vmPriceGroup = ViewModelProvider(this).get(PriceGroupViewModel::class.java)

        vmPriceGroup.getMessage().observe(this, Observer {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
        vmPriceGroup.getIsExecute().observe(this, Observer {
            if (it && !progressBar.isShowing()) {
                progressBar.show(this, getString(R.string.txt_please_wait))
            } else {
                progressBar.dialog.dismiss()
            }
        })

        vmPriceGroup.getListProductPrice().observe(this, Observer {
            val firstListCustomer = it
            progressBar.dialog.dismiss()
            adapter.updateSalesData(firstListCustomer)
        })

        vmPriceGroup.getListProductPrice(priceGroup?.id.toString())
    }

    private fun setupRecycleView() {
        adapter = ListProductPriceGroupAdapter()
        adapter.listener ={
            actionUpdateProductPrice(it)
        }
        rv_list_product?.layoutManager = LinearLayoutManager(this)
        rv_list_product?.setHasFixedSize(false)
        rv_list_product?.adapter = adapter
    }

    private fun actionUpdateProductPrice(product: Product) {
        val body: MutableMap<String, Any?> = mutableMapOf(
            "product_id" to (product.id),
            "price" to (product.price),
            "price_credit" to (product.price_kredit),
            "min_order" to (product.min_order),
            "is_multiple" to (product.is_multiple)
        )

        vmPriceGroup.putEditProductPrice(body, priceGroup?.id.toString()){
            vmPriceGroup.getListProductPrice(priceGroup?.id.toString())
        }
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