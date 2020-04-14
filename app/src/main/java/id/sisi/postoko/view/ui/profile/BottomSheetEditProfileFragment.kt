package id.sisi.postoko.view.ui.profile

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.User
import id.sisi.postoko.utils.extensions.MyToast
import id.sisi.postoko.utils.extensions.putIfDataNotNull
import id.sisi.postoko.utils.extensions.showErrorL
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_account.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_address.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_company.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_personal.*


class BottomSheetEditProfileFragment : BottomSheetDialogFragment() {
    private lateinit var profileType: ProfileType
    private lateinit var mViewModel: ProfileViewModel
    private var tempUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mViewModel.getUser().observe(viewLifecycleOwner, Observer {
            tempUser = it
            it?.let { updateUI(it) }
        })
        mViewModel.getIsSuccessUpdatee().observe(viewLifecycleOwner, Observer {
            if (it) dismiss()
        })
        val layoutId = when (profileType) {
            ProfileType.ADDRESS -> R.layout.fragment_bottom_sheet_edit_profile_address
            ProfileType.COMPANY -> R.layout.fragment_bottom_sheet_edit_profile_company
            ProfileType.ACCOUNT -> R.layout.fragment_bottom_sheet_edit_profile_account
            ProfileType.PASSWORD -> R.layout.fragment_bottom_sheet_edit_profile_password
            else -> R.layout.fragment_bottom_sheet_edit_profile_personal
        }
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
        view.findViewById<TextView>(R.id.btn_reset)?.setOnClickListener {
            submitForm()
        }
        view.findViewById<TextView>(R.id.btn_action_submit)?.setOnClickListener {
            view.findViewById<TextView>(R.id.btn_reset)?.performClick()
        }
    }

    private fun updateUI(user: User) {
        when (profileType) {
            ProfileType.ADDRESS -> {
                et_profile_address?.setText(user.address)
                et_profile_postal_code?.setText(user.companyData?.postal_code)
            }
            ProfileType.COMPANY -> {
                et_profile_cf1?.setText(user.companyData?.cf1)
                et_profile_cf6?.setText(user.companyData?.cf6)
            }
            ProfileType.ACCOUNT -> {
                et_profile_email?.setText(user.email)
                et_profile_phone?.setText(user.phone)
            }
            ProfileType.PASSWORD -> {

            }
            else -> {
                et_profile_first_name?.setText(user.first_name)
                et_profile_last_name?.setText(user.last_name)
                rbtn_male?.isChecked = user.gender == "male"
                rbtn_female?.isChecked = user.gender == "female"
            }
        }
    }

    private fun submitForm() {
        val body = mutableMapOf<String, String>()
        if (tempUser?.id?.toString().isNullOrEmpty()) {
            MyToast.make(context).showErrorL(getString(R.string.txt_error_try_again_later))
            return
        }
        when (profileType) {
            ProfileType.ADDRESS -> {
//                et_profile_postal_code?.setText(user.address)
                body.putIfDataNotNull(
                    "address",
                    et_profile_address?.text?.toString(),
                    oldValue = tempUser?.address
                )
            }
            ProfileType.COMPANY -> {
//                et_profile_cf1?.setText(user.address)
//                et_profile_cf6?.setText(user.address)
            }
            ProfileType.ACCOUNT -> {
                body.putIfDataNotNull(
                    "email",
                    et_profile_email?.text?.toString(),
                    oldValue = tempUser?.email
                )
                body.putIfDataNotNull(
                    "phone",
                    et_profile_phone?.text?.toString(),
                    oldValue = tempUser?.phone
                )
            }
            ProfileType.PASSWORD -> {

            }
            else -> {
                body.putIfDataNotNull(
                    "first_name",
                    et_profile_first_name?.text?.toString(),
                    oldValue = tempUser?.first_name
                )
                body.putIfDataNotNull(
                    "last_name",
                    et_profile_last_name?.text?.toString(),
                    oldValue = tempUser?.last_name
                )
                var gender: String? = null
                if (rbtn_male?.isChecked == true) gender = "male"
                if (rbtn_female?.isChecked == true) gender = "female"
                body.putIfDataNotNull("gender", gender, oldValue = tempUser?.gender)
            }
        }
        mViewModel.putUserProfile(body, tempUser?.id?.toString()!!)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? ProfileActivity)?.refreshData(mViewModel.getIsSuccessUpdatee().value)
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            profileType: ProfileType
        ) {
            val bottomSheetFragment = BottomSheetEditProfileFragment()
            bottomSheetFragment.profileType = profileType
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }

    enum class ProfileType {
        PERSONAL,
        ADDRESS,
        COMPANY,
        ACCOUNT,
        PASSWORD,
    }
}