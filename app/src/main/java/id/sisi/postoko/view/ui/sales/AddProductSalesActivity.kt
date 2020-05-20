package id.sisi.postoko.view.ui.sales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListAddProductOnAddSalesAdapter
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.KEY_SALE_ITEM
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.ui.product.ProductViewModel
import kotlinx.android.synthetic.main.activity_add_product_sales.*
import kotlinx.android.synthetic.main.content_add_product_sales.*


class AddProductSalesActivity : BaseActivity() {

    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ListAddProductOnAddSalesAdapter
    var listProduct: List<Product>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product_sales)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        viewModel.getListProducts().observe(this, Observer {
            if (it != null) {
                listProduct = it
                setupUI(it)
            }
        })
        sv_search_product_add_sales.setOnClickListener {
            sv_search_product_add_sales?.onActionViewExpanded()
        }

        sv_search_product_add_sales.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.length > 2) {
                    startSearchData(newText)
                } else {
                    listProduct?.let { setupUI(it) }
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

        })
    }

    private fun startSearchData(query: String) {
        listProduct?.let {
            val listSearchResult = listProduct!!.filter {
                it.name.contains(query, true) or it.code.contains(query, true)
            }
            setupUI(listSearchResult)
        }
    }

    private fun setupUI(it: List<Product>) {
        setupRecycleView(it)
    }

    private fun setupRecycleView(it: List<Product>) {
        adapter = ListAddProductOnAddSalesAdapter()
        adapter.updateMasterData(it)
        adapter.listenerProductAdapter = {
            val returnIntent = Intent()
            returnIntent.putExtra(KEY_SALE_ITEM, it)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        rv_list_product_add_sale?.layoutManager = LinearLayoutManager(this)
        rv_list_product_add_sale?.setHasFixedSize(false)
        rv_list_product_add_sale?.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
