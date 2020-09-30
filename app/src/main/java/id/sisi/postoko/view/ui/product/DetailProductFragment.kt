package id.sisi.postoko.view.ui.product

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import id.sisi.postoko.R
import id.sisi.postoko.model.Product
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.toCurrencyID
import id.sisi.postoko.utils.extensions.visible
import id.sisi.postoko.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_detail_product.*

class DetailProductFragment: BaseFragment() {

    var product: Product? = null


    override var tagName: String
        get() = "Detail Produk"
        set(_) {}

    companion object {
        fun newInstance() = DetailProductFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_product, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as DetailProductActivity).product.observe(viewLifecycleOwner, Observer {
            setupUI(it)
        })
    }

    private fun setupUI(it: Product?) {
        val strSupplier: String = ""

        tv_product_type.text = it?.type
        tv_product_code.text = it?.code
        tv_product_brand.text = it?.brand_name
        tv_product_category.text = it?.category_name
        tv_product_unit.text = it?.unit_name
        tv_supplier.text = it?.supplier

        if(!TextUtils.isEmpty(it?.price.toString())){
            tv_product_price.text = it?.price?.toCurrencyID()
            layout_price.visible()
            line_price.visible()
        }

        if(!TextUtils.isEmpty(it?.cost.toString())){
            tv_product_cost.text = it?.cost?.toCurrencyID()
            layout_cost.visible()
            line_cost.visible()
        }

        logE("${it?.price}")
    }
}