package id.sisi.postoko.view.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.User
import id.sisi.postoko.utils.BASE_URL
import id.sisi.postoko.utils.LoadImageFromUrl
import id.sisi.postoko.utils.RC_PROFILE
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.BaseActivity
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.failed_load_data.*


class ProfileActivity : BaseActivity() {

    private lateinit var mViewModel: ProfileViewModel

    private lateinit var user: User
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        mViewModel.getIsExecute().observe(this, Observer {
            swipeRefreshLayout?.isRefreshing = it
        })

        mViewModel.getUser().observe(this, Observer {
            if (it != null) {
                showData(it)
                user = it
                layout_profile.visible()
                layout_status_progress?.gone()
                tv_status_progress?.text = "Gagal Memuat Data"
            }else{
                layout_profile.gone()
                layout_status_progress?.visible()
            }
        })

        swipeRefreshLayout?.setOnRefreshListener {
            mViewModel.getUserProfile()
        }

        mViewModel.getUserProfile()
        setAction()
    }

    private fun setAction() {
        tv_profile_change_personal?.setOnClickListener {
            BottomSheetEditProfileFragment.show(
                supportFragmentManager,
                BottomSheetEditProfileFragment.ProfileType.PERSONAL,
                user
            )
        }
        tv_profile_change_address?.setOnClickListener {
            BottomSheetEditProfileFragment.show(
                supportFragmentManager,
                BottomSheetEditProfileFragment.ProfileType.ADDRESS,
                user
            )
        }
        tv_profile_change_company?.setOnClickListener {
            BottomSheetEditProfileFragment.show(
                supportFragmentManager,
                BottomSheetEditProfileFragment.ProfileType.COMPANY,
                user
            )
        }
        tv_profile_change_account?.setOnClickListener {
            BottomSheetEditProfileFragment.show(
                supportFragmentManager,
                BottomSheetEditProfileFragment.ProfileType.ACCOUNT,
                user
            )
        }
        iv_header_avatar.setOnClickListener {
            BottomSheetUpdateAvatar.show(
                supportFragmentManager,
                user
            )
        }

        btn_change_password?.setOnClickListener {
            BottomSheetChangePassword.show(
                supportFragmentManager
            )
        }
    }

    private fun showData(user: User?) {
        user?.let {
            logE("profile : $it")

            if(!TextUtils.isEmpty(it.avatar)){
                val loadImage = LoadImageFromUrl(iv_header_avatar)
                loadImage.execute("$BASE_URL/assets/uploads/avatars/thumbs/${it.avatar}")
            }

            tv_user_company_name?.text = it.company
            tv_user_company_address?.text = it.address
            tv_profile_first_name?.text = it.first_name
            tv_profile_last_name?.text = it.last_name
            tv_profile_gender?.text = getTryString(GenderType.MALE.tryValue(it.gender)?.stringId)
            tv_profile_state?.text = it.country ?: it.companyData?.country
            tv_profile_city?.text = it.city ?: it.companyData?.city
            tv_profile_village?.text = it.state ?: it.companyData?.state
            tv_profile_address?.text = it.address
            tv_profile_postal_code?.text = it.companyData?.postal_code
            tv_profile_cf1?.text = it.companyData?.cf1
            tv_profile_cf6?.text = it.companyData?.cf6
            tv_profile_email?.text = it.email
            tv_profile_phone?.text = it.phone
        }
    }

    fun refreshData(isSuccessUpdate: Boolean?) {
        if (isSuccessUpdate == true) mViewModel.getUserProfile()
    }

    companion object {
        fun show(oldContext: FragmentActivity?) {
            val page = Intent(oldContext, ProfileActivity::class.java)
            oldContext?.startActivityForResult(page, RC_PROFILE)
        }
    }
}
