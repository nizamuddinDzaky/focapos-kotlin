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



class AddProductSalesActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ListAddProductOnAddSalesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product_sales)
        setSupportActionBar(toolbar)
        setupUI()
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        viewModel.getListProducts().observe(this, Observer {
            logE("cek produk : $it")
            adapter.updateMasterData(it)
        })
    }

    private fun setupUI() {
        setupRecycleView()
    }

    private fun setupRecycleView() {
        adapter = ListAddProductOnAddSalesAdapter()
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


}
