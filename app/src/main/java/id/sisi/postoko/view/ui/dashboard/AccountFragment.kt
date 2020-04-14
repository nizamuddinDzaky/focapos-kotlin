package id.sisi.postoko.view.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.AccountViewModel
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment() {
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_account)
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it) {
                logE("progress")
            } else {
                logE("done")
            }
        })
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            tv_user_company?.text = it?.company ?: "~"
            tv_user_company_code?.text = it?.address ?: "~"
        })

        val menus = arrayOf(
            "Perbarui Profil",
            "Daftar Alamat",
            "Salesrespon",
            "Ganti Password",
            "Petunjuk Penggunaan",
            "Syarat dan Ketentuan",
            "Keluar"
        )

        val datas = menus.map {
            return@map mutableMapOf("data1" to it, "data2" to it)
        }
        val adapter = SimpleAdapter(
            this.context, datas, R.layout.list_item_menu_account, arrayOf("data1", "data2"), arrayOf(R.id.tv_menu_account_name).toIntArray()
        )

        lv_menu_account.adapter = adapter
        lv_menu_account?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
                logE("click $i")
                if (i == menus.size - 1) {
                    MyApp.prefs.isLogin = false
                }
            }
    }

    companion object {
        val TAG: String = AccountFragment::class.java.simpleName
        fun newInstance() = AccountFragment()
    }
}