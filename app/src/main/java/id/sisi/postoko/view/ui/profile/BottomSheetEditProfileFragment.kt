package id.sisi.postoko.view.ui.profile

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.User
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_account.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_address.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_company.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_personal.*


@Suppress("NON_EXHAUSTIVE_WHEN")
class BottomSheetEditProfileFragment : BottomSheetDialogFragment() {
    private lateinit var profileType: ProfileType
    private lateinit var mViewModel: ProfileViewModel
    private lateinit var mViewModelRegion: DaerahViewModel
    private var tempUser: User? = null
    private val progressBar = CustomProgressBar()
    private var provinceList: Array<String> = arrayOf()
    private var cityList: Array<String> = arrayOf()
    private var villageList: Array<String> = arrayOf()

    var countrySelected: String? = null
    var citySelected: String? = null
    var stateSelected: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.getParcelable<User>("user")?.let{
            tempUser = it
        }
        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val layoutId = when (profileType) {
            ProfileType.ADDRESS -> R.layout.fragment_bottom_sheet_edit_profile_address
            ProfileType.COMPANY -> R.layout.fragment_bottom_sheet_edit_profile_company
            ProfileType.ACCOUNT -> R.layout.fragment_bottom_sheet_edit_profile_account
            /*ProfileType.PASSWORD -> R.layout.fragment_bottom_sheet_edit_profile_password*/
            else -> R.layout.fragment_bottom_sheet_edit_profile_personal
        }

        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tempUser?.let { updateUI(it) }

        mViewModel.getMessage().observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
            if (it && !progressBar.isShowing()) {
                context?.let { c ->
                    progressBar.show(c, getString(R.string.txt_please_wait))
                }
            } else {
                progressBar.dialog.dismiss()
            }
        })

        view.findViewById<ImageView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
        view.findViewById<TextView>(R.id.btn_reset)?.setOnClickListener {
            submitForm()
        }
        view.findViewById<TextView>(R.id.btn_action_submit)?.setOnClickListener {
            view.findViewById<TextView>(R.id.btn_reset)?.performClick()
        }

    }

    private fun setUIProvince(user: User){
        spinner_profile_province.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.list_spinner,
            provinceList
        )
        logE("${user.country}")
        spinner_profile_province.setSelection(provinceList.indexOf(user.country))
    }

    private fun setUICity(user: User){
        spinner_profile_city.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.list_spinner,
            cityList
        )
        var index = 0
        if (cityList.indexOf(user.city) != -1){
            index = cityList.indexOf(user.city)
        }
        spinner_profile_city.setSelection(index)
    }

    private fun setUIStates(user: User){
        spinner_profile_village.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.list_spinner,
            villageList
        )

        var index = 0
        if (villageList.indexOf(user.state) != -1){
            index = villageList.indexOf(user.state)
        }

        spinner_profile_village.setSelection(index)
    }

    private fun updateUI(user: User) {
        when (profileType) {
            ProfileType.ADDRESS -> {
                et_profile_address?.setText(user.address)
                et_profile_postal_code?.setText(user.companyData?.postal_code)

                setUpSpinnerRegion(user)

            }
            ProfileType.COMPANY -> {
                et_profile_cf1?.setText(user.companyData?.cf1)
                et_profile_cf6?.setText(user.companyData?.cf6)
            }
            ProfileType.ACCOUNT -> {
                et_profile_email?.setText(user.email)
                et_profile_phone?.setText(user.phone)
            }
            else -> {
//                view?.findViewById<TextView>(R.id.et_profile_first_name)?.setText(user.first_name)
                et_profile_first_name?.setText(user.first_name)
                et_profile_last_name?.setText(user.last_name)
                rbtn_male?.isChecked = user.gender == "male"
                rbtn_female?.isChecked = user.gender == "female"
            }
        }
    }

    private fun setUpSpinnerRegion(user: User) {
        mViewModelRegion = ViewModelProvider(this).get(DaerahViewModel::class.java)

        mViewModelRegion.getAllProvince().observe(requireActivity(), Observer {
            it?.let {listDataRegion ->
                provinceList = listDataRegion.map {dataRegion ->
                    return@map dataRegion.province_name ?: ""
                }.toTypedArray()
            }
            setUIProvince(user)
        })

        mViewModelRegion.getAllCity().observe(requireActivity(), Observer {
            it?.let {listDataRegion ->
                cityList = listDataRegion.map {dataRegion ->
                    return@map dataRegion.kabupaten_name ?: ""
                }.toTypedArray()
            }
            setUICity(user)
        })

        mViewModelRegion.getAllStates().observe(requireActivity(), Observer {
            it?.let {listDataRegion ->
                villageList = listDataRegion.map {dataRegion ->
                    return@map dataRegion.kecamatan_name ?: ""
                }.toTypedArray()
            }
            setUIStates(user)
        })

        spinner_profile_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelRegion.getCity(provinceList[position])
                countrySelected = provinceList[position]
            }
        }

        spinner_profile_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelRegion.getStates(cityList[position])
                citySelected = cityList[position]
            }
        }

        spinner_profile_village.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                stateSelected = villageList[position]
            }
        }

        mViewModelRegion.getProvince()
    }

    private fun submitForm() {
        var body = mutableMapOf<String, String>()
        if (tempUser?.id?.toString().isNullOrEmpty()) {
            MyToast.make(context).showErrorL(getString(R.string.txt_error_try_again_later))
            return
        }

        when (profileType) {
            ProfileType.ADDRESS -> {
                body = mutableMapOf(
                    "province" to (countrySelected ?: ""),
                    "city" to (citySelected ?: ""),
                    "state" to (stateSelected ?: ""),
                    "address" to (et_profile_address?.text?.toString() ?: ""),
                    "postal_code" to (et_profile_postal_code?.text?.toString() ?: "")
                )
            }
            ProfileType.COMPANY -> {
                body = mutableMapOf(
                    "cf1" to (et_profile_cf1?.text?.toString() ?: ""),
                    "cf6" to (et_profile_cf6?.text?.toString() ?: "")
                )
            }
            ProfileType.ACCOUNT -> {
                body = mutableMapOf(
                    "email" to (et_profile_email?.text?.toString() ?: ""),
                    "phone" to (et_profile_phone?.text?.toString() ?: "")
                )
            }
            /*ProfileType.PASSWORD -> {
                body = mutableMapOf(
                    "email" to (et_profile_email?.text?.toString() ?: ""),
                    "phone" to (et_profile_phone?.text?.toString() ?: "")
                )
            }*/
            else -> {
                var gender: String? = null
                if (rbtn_male?.isChecked == true) gender = "male"
                if (rbtn_female?.isChecked == true) gender = "female"

                body = mutableMapOf(
                    "first_name" to (et_profile_first_name?.text?.toString() ?: ""),
                    "last_name" to (et_profile_last_name?.text?.toString() ?: ""),
                    "gender" to (gender ?: "")
                )
            }
        }
        mViewModel.putUserProfile(body, tempUser?.id?.toString()!!){
            (activity as? ProfileActivity)?.refreshData(true)
            this.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? ProfileActivity)?.refreshData(mViewModel.getIsSuccessUpdate().value)
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            profileType: ProfileType,
            user: User
        ) {
            val bottomSheetFragment = BottomSheetEditProfileFragment()
            bottomSheetFragment.profileType = profileType
            val bundle = Bundle()
            bundle.putParcelable("user", user)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }

    enum class ProfileType {
        PERSONAL,
        ADDRESS,
        COMPANY,
        ACCOUNT
    }
}