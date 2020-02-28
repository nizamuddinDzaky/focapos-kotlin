package id.sisi.postoko.view.ui.sales

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListAddProductOnAddSalesAdapter
import id.sisi.postoko.adapter.ListMasterAdapter
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.product.ProductViewModel

import kotlinx.android.synthetic.main.activity_add_product_sales.*
import kotlinx.android.synthetic.main.content_add_product_sales.*
import kotlinx.android.synthetic.main.master_data_fragment.*
import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import id.sisi.postoko.model.Customer
import kotlinx.android.synthetic.main.fragment_search_customer.*


class AddProductSalesActivity : AppCompatActivity() {

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
//            adapter.updateMasterData(it)
            if (it != null) {
                listProduct = it
                setupUI(it)
            }
        })
        tv_search_product_add_sales.setOnClickListener{
            tv_search_product_add_sales.visibility = View.GONE
            sv_search_product_add_sales.visibility = View.VISIBLE
            ll_search_product_add_sales.visibility = View.GONE
            sv_search_product_add_sales?.onActionViewExpanded()
        }

        sv_search_product_add_sales.setOnCloseListener(object :androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                tv_search_product_add_sales.visibility = View.VISIBLE
                sv_search_product_add_sales.visibility = View.GONE
                ll_search_product_add_sales.visibility = View.VISIBLE
                return true
            }
        })

        sv_search_product_add_sales.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (!newText.isNullOrEmpty() && newText.length > 2) {
                    startSearchData(newText)
                }else{
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
            returnIntent.putExtra("product_result", it)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        rv_list_product_add_sale?.layoutManager = LinearLayoutManager(this)
        rv_list_product_add_sale?.setHasFixedSize(false)
        rv_list_product_add_sale?.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
