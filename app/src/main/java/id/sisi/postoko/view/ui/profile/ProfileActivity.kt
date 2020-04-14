package id.sisi.postoko.view.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.sisi.postoko.R
import id.sisi.postoko.model.User
import id.sisi.postoko.utils.RC_PROFILE
import id.sisi.postoko.utils.extensions.getTryString
import id.sisi.postoko.utils.extensions.tryValue
import id.sisi.postoko.view.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : BaseActivity() {

    private lateinit var mViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mViewModel.getUser().observe(this, Observer {
            showData(it)
        })

        setAction()
    }

    private fun setAction() {
        tv_profile_change_personal?.setOnClickListener {
            BottomSheetEditProfileFragment.show(
                supportFragmentManager,
                BottomSheetEditProfileFragment.ProfileType.PERSONAL
            )
        }
        tv_profile_change_address?.setOnClickListener {
            BottomSheetEditProfileFragment.show(
                supportFragmentManager,
                BottomSheetEditProfileFragment.ProfileType.ADDRESS
            )
        }
        tv_profile_change_company?.setOnClickListener {
            BottomSheetEditProfileFragment.show(
                supportFragmentManager,
                BottomSheetEditProfileFragment.ProfileType.COMPANY
            )
        }
        tv_profile_change_account?.setOnClickListener {
            BottomSheetEditProfileFragment.show(
                supportFragmentManager,
                BottomSheetEditProfileFragment.ProfileType.ACCOUNT
            )
        }
        btn_change_password?.setOnClickListener {
            BottomSheetEditProfileFragment.show(
                supportFragmentManager,
                BottomSheetEditProfileFragment.ProfileType.PASSWORD
            )
        }
    }

    private fun showData(user: User?) {
        user?.let {
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
