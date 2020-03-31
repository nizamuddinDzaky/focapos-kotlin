package id.sisi.postoko.view.ui.profile

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
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_account.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_address.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_company.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_personal.*


class BottomSheetEditProfileFragment : BottomSheetDialogFragment() {
    private lateinit var profileType: ProfileType
    private lateinit var mViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val layoutId = when(profileType) {
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
        mViewModel.getUser().observe(viewLifecycleOwner, Observer {
            updateUI(it as User)
        })
        view.findViewById<TextView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
    }

    private fun updateUI(user: User) {
        when(profileType) {
            ProfileType.ADDRESS -> {
                et_profile_address?.setText(user.address)
                et_profile_postal_code?.setText(user.address)
            }
            ProfileType.COMPANY -> {
                et_profile_cf1?.setText(user.address)
                et_profile_cf6?.setText(user.address)
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