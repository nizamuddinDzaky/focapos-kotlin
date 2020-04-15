package id.sisi.postoko.view.ui.customergroup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.CustomerGroup
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.KEY_CUSTOMER_GROUP
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.utils.RC_ADD_CUSTOMER_GROUP
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.custom.CustomProgressBar

import kotlinx.android.synthetic.main.activity_edit_customer_group.*
import kotlinx.android.synthetic.main.content_edit_customer_group.*

class EditCustomerGroupActivity : BaseActivity() {
    private var customerGroup: CustomerGroup = CustomerGroup(id = "0", name = "ForcaPoS")
    private val progressBar = CustomProgressBar()
    private val numberSparator = NumberSeparator()
    private lateinit var mViewModel: CustomerGroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_customer_group)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        displayHomeEnable()
        
        mViewModel = ViewModelProvider(this).get(CustomerGroupViewModel::class.java)

        et_customer_group_kredit_limit.addTextChangedListener(numberSparator.onTextChangedListener(et_customer_group_kredit_limit))
        intent?.getParcelableExtra<CustomerGroup>(KEY_CUSTOMER_GROUP)?.let {
            customerGroup = it
            toolbar_subtitle.text = customerGroup.name
            et_customer_group_name?.setText(customerGroup.name)
            et_customer_group_percentage?.setText(customerGroup.percent)
            et_customer_group_kredit_limit?.setText(String.format("%.0f",customerGroup.kredit_limit))
        }

        btn_action_submit.setOnClickListener {
            actionEditCustomerGroup()
        }

    }

    private fun actionEditCustomerGroup() {
        val numbersMap = validationFormEditCustomerGroup()
        if (numbersMap["type"] as Boolean){
            this.let { progressBar.show(it, "Silakan tunggu...") }
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_customer_group_name?.text.toString()),
                "percentage" to (et_customer_group_percentage?.text.toString()),
                "credit_limit" to (et_customer_group_kredit_limit?.tag.toString())
            )

            mViewModel.putEditCustomerGroup(body, customerGroup.id){
                Toast.makeText(this, "" + it["message"], Toast.LENGTH_SHORT).show()
                if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                    progressBar.dialog.dismiss()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }else{
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    private fun validationFormEditCustomerGroup(): Map<String, Any?> {
        var message = ""
        var cek = true
        if (TextUtils.isEmpty(et_customer_group_name.text)){
            message += "- Nama Customer Group Tidak Boleh Kosong\n"
            cek = false
        }

        if (TextUtils.isEmpty(et_customer_group_kredit_limit.text)){
            message += "- Kredit Limit Tidak Boleh Kosong\n"
            cek = false
        }

        return mapOf("message" to message, "type" to cek)
    }

    companion object {
        fun show(
            fragmentActivity: FragmentActivity,
            customerGroup: CustomerGroup
        ) {
            val page = Intent(fragmentActivity, EditCustomerGroupActivity::class.java)
            page.putExtra(KEY_CUSTOMER_GROUP, customerGroup)
            fragmentActivity.startActivityForResult(page, RC_ADD_CUSTOMER_GROUP)
        }
    }
}
