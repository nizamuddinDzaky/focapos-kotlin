package id.sisi.postoko.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import id.sisi.postoko.MyApp
import id.sisi.postoko.R
import id.sisi.postoko.network.ApiServices
import id.sisi.postoko.utils.extensions.exe
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.tryMe
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.txt_account)
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val headers = mutableMapOf("Forca-Token" to (MyApp.prefs.posToken ?: ""))
        ApiServices.getInstance()?.getProfile(headers)?.exe(
            onFailure = { call, throwable ->
                logE("gagal")
            },
            onResponse = { call, response ->
                logE("berhasil profile")
                tryMe {
                    logE("tes ${response.body()?.data?.user?.username}")
                }
            }
        )

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
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
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