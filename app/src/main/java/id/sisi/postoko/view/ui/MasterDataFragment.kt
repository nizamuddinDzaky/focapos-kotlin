package id.sisi.postoko.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import id.sisi.postoko.R
import id.sisi.postoko.model.User
import id.sisi.postoko.utils.RC_ADD_CUSTOMER
import id.sisi.postoko.utils.RC_ADD_CUSTOMER_GROUP
import id.sisi.postoko.utils.RC_ADD_PRICE_GROUP
import id.sisi.postoko.utils.extensions.gone
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.view.ui.product.AddProductActivity
import id.sisi.postoko.view.HomeActivity
import id.sisi.postoko.view.pager.HomePagerAdapter
import id.sisi.postoko.view.ui.profile.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class MasterDataFragment : Fragment() {
    private lateinit var user: User

    private lateinit var mViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Master Data"
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fb_add_transaction?.setOnClickListener {
            startActivity(Intent(this.context, AddProductActivity::class.java))
        }
        main_container?.gone()
        main_view_pager?.let {
            it.adapter = HomePagerAdapter(childFragmentManager)
            tabs_main_pagers?.setupWithViewPager(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ADD_CUSTOMER){
            main_view_pager.setCurrentItem(0)
        }else if(requestCode == RC_ADD_PRICE_GROUP){
            main_view_pager.setCurrentItem(2)
        }else if(requestCode == RC_ADD_CUSTOMER_GROUP){
            main_view_pager.setCurrentItem(1)
        }

        (main_view_pager?.adapter as? HomePagerAdapter)?.getCurrentFragment()
            ?.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val TAG: String = MasterDataFragment::class.java.simpleName
        fun newInstance() = MasterDataFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_syncron, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if(id == R.id.menu_action_syncron){
            (activity as? HomeActivity)?.syncMasterCustomer()
        }
        return super.onOptionsItemSelected(item)
    }
}