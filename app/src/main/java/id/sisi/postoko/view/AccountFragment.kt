package id.sisi.postoko.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.extensions.logE
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

        val menus = arrayOf(
            "Perbarui Profil",
            "Daftar Alamat",
            "Salesrespon",
            "Ganti Password",
            "Petunjuk Penggunaan",
            "Syarat dan Ketentuan",
            "Keluar"
        )

//        val adapter = StableArrayAdapter(
//            this.context,
//            android.R.layout.simple_list_item_1, menus.asList()
//        )

        //val datas = arrayListOf<Map<String, String>>()
        val datas = menus.map {
            val map = mutableMapOf<String, String>()
            map.put("data1", it)
            map.put("data2", it)
            return@map map
        }
        val adapter = SimpleAdapter(
            this.context, datas, R.layout.list_item_menu_account, arrayOf("data1", "data2"), arrayOf(R.id.tv_menu_account_name).toIntArray()
        )

        lv_menu_account.adapter = adapter
        lv_menu_account?.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                logE("click $i")
            }
    }

    companion object {
        val TAG: String = AccountFragment::class.java.simpleName
        fun newInstance() = AccountFragment()
    }

    private class StableArrayAdapter(
        context: Context?, textViewResourceId: Int,
        objects: List<String>
    ) : ArrayAdapter<String?>(context!!, textViewResourceId, objects) {
        var mIdMap = HashMap<String, Int>()

        override fun getItemId(position: Int): Long {
            val item = getItem(position)
            return mIdMap[item]?.toLong() ?: 0
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        init {
            for (i in objects.indices) {
                mIdMap[objects[i]] = i
            }
        }
    }

}