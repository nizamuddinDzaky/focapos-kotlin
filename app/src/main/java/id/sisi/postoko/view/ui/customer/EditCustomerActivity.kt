package id.sisi.postoko.view.ui.customer

//import sun.awt.windows.ThemeReader.getPosition
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.Customer
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.LoadImageFromUrl
import id.sisi.postoko.utils.MyDialog
import id.sisi.postoko.utils.URL_AVATAR_PROFILE
import id.sisi.postoko.utils.extensions.validation
import id.sisi.postoko.view.custom.CustomProgressBar
import kotlinx.android.synthetic.main.activity_edit_customer.*
import kotlinx.android.synthetic.main.content_edit_customer.*


class EditCustomerActivity : AppCompatActivity() {

    var customer: Customer? = null
    private val progressBar = CustomProgressBar()
    private val pages = listOf(
        EditDataCustomerFragment(),
        EditWarehouseCustomerFragment()
    )
    var listWarehouse: List<Warehouse>? = arrayListOf()
    lateinit var viewModelCustomer: CustomerViewModel
    private val myDialog = MyDialog()

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

        viewModelCustomer = ViewModelProvider(this).get(CustomerViewModel::class.java)
        viewModelCustomer.getMessage().observe(this, Observer {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModelCustomer.getIsExecute().observe(this, Observer {
            if (it && !progressBar.isShowing()) {
                progressBar.show(this, getString(R.string.txt_please_wait))
            } else {
                progressBar.dialog.dismiss()
            }
        })

        main_view_pager?.let {
            it.adapter = CustomerPagerAdapter(supportFragmentManager, pages)
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

        btn_action_submit.setOnClickListener {
            val mandatory = listOf<EditText>(
                findViewById(R.id.et_name_edit_customer),
                findViewById(R.id.et_email_edit_customer),
                findViewById(R.id.et_phone_edit_customer),
                findViewById(R.id.et_address_edit_customer)
            )
            if (!mandatory.validation()) {
                return@setOnClickListener
            }

            actionEditCustomer()
        }
        
    }

    private fun actionEditCustomer() {
        val validation =  validationFormEditCustomer()
        val listIdSelected: ArrayList<String> = arrayListOf()
        var defaultWarehouse = 0
        listWarehouse?.forEach {wh ->
            if (wh.isSelected)
                listIdSelected.add(wh.id)
            if (wh.isDefault)
                defaultWarehouse = wh.id.toInt()
        }

        if (validation["type"] as Boolean){
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (findViewById<EditText>(R.id.et_name_edit_customer)?.text?.toString() ?: ""),
                "email" to (findViewById<EditText>(R.id.et_email_edit_customer)?.text?.toString() ?: ""),
                "customer_group_id" to (findViewById<Spinner>(R.id.sp_customer_group_edit_customer)?.selectedItem?.toString() ?: ""),
                "price_group_id" to (findViewById<Spinner>(R.id.sp_price_group_edit_customer)?.selectedItem?.toString() ?: ""),
                "company" to (findViewById<EditText>(R.id.et_company_name_edit_customer)?.text?.toString() ?: ""),
                "address" to (findViewById<EditText>(R.id.et_address_edit_customer)?.text?.toString() ?: ""),
                "vat_no" to (findViewById<EditText>(R.id.et_npwp_edit_customer)?.text?.toString() ?: ""),
                "postal_code" to (findViewById<EditText>(R.id.et_postal_code_edit_customer)?.text?.toString() ?: ""),
                "phone" to (findViewById<EditText>(R.id.et_phone_edit_customer)?.text?.toString() ?: ""),
                "province" to (findViewById<Spinner>(R.id.sp_provinsi_group_edit_customer)?.selectedItem?.toString() ?: ""),
                "city" to (findViewById<Spinner>(R.id.sp_district_group_edit_customer)?.selectedItem?.toString() ?: ""),
                "state" to (findViewById<Spinner>(R.id.sp_city_group_edit_customer)?.selectedItem?.toString() ?: ""),
                "warehouses" to listIdSelected,
                "default" to defaultWarehouse
            )

            viewModelCustomer.postEditCustomer(body, customer?.id.toString()){
                setResult(Activity.RESULT_OK)
                finish()
            }

        }else{
            myDialog.alert(validation["message"] as String, this)
        }


    }

    private fun validationFormEditCustomer(): Map<String, Any?> {
        var message = ""
        var cek = true

        if (findViewById<Spinner>(R.id.sp_customer_group_edit_customer)?.selectedItem.toString() == ""){
            message += "- Customer Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (findViewById<Spinner>(R.id.sp_provinsi_group_edit_customer)?.selectedItem.toString() == ""){
            message += "- Provinsi Tidak Boleh Kosong\n"
            cek = false
        }

        if (findViewById<Spinner>(R.id.sp_district_group_edit_customer)?.selectedItem.toString() == ""){
            message += "- Kabupaten/Kota Tidak Boleh Kosong\n"
            cek = false
        }

        if (findViewById<Spinner>(R.id.sp_city_group_edit_customer)?.selectedItem.toString() == ""){
            message += "- Kecamatan Tidak Boleh Kosong\n"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }
}
