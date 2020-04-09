package id.sisi.postoko.view.ui.customergroup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import id.sisi.postoko.R
import id.sisi.postoko.utils.NumberSeparator
import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.activity_customer_group_add.*
import kotlinx.android.synthetic.main.activity_price_group_add.btn_action_submit

class AddCustomerGroupActivity : BaseActivity() {
    private val numberSparator = NumberSeparator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_group_add)
        displayHomeEnable()

        et_customer_group_kredit_limit.addTextChangedListener(numberSparator.onTextChangedListener(et_customer_group_kredit_limit))
        btn_action_submit.setOnClickListener {
            actionAddCustomerGroup()
        }
    }

    private fun actionAddCustomerGroup() {
        val numbersMap = validationFormAddCustomerGroup()
        if (numbersMap["type"] as Boolean){
            val body: MutableMap<String, Any> = mutableMapOf(
                "name" to (et_customer_group_name?.text.toString()),
                "percentage" to (et_customer_group_percentage?.text.toString()),
                "kredit_limit" to (et_customer_group_kredit_limit?.tag.toString())
            )
        }else{
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage(numbersMap["message"] as String)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                }
                .show()
        }
    }

    private fun validationFormAddCustomerGroup(): Map<String, Any?> {
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
            fragmentActivity: FragmentActivity
        ) {
            val page = Intent(fragmentActivity, AddCustomerGroupActivity::class.java)
            fragmentActivity.startActivity(page)
        }
    }
}