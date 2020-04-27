package id.sisi.postoko.view.ui.profile

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter;
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.User
import id.sisi.postoko.network.NetworkResponse
import id.sisi.postoko.utils.extensions.MyToast
import id.sisi.postoko.utils.extensions.logE
import id.sisi.postoko.utils.extensions.putIfDataNotNull
import id.sisi.postoko.utils.extensions.showErrorL
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.daerah.DaerahViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_account.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_address.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_company.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_edit_profile_personal.*


class BottomSheetEditProfileFragment : BottomSheetDialogFragment() {
    private lateinit var profileType: ProfileType
    private lateinit var mViewModel: ProfileViewModel
    private lateinit var mViewModelDaerah: DaerahViewModel
    private var tempUser: User? = null
    private val progressBar = CustomProgressBar()

    private var provinceList: Array<String> = arrayOf()
    private var cityList: Array<String> = arrayOf()
    private var villageList: Array<String> = arrayOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.getParcelable<User>("user")?.let{
            tempUser = it
        }
        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mViewModelDaerah = ViewModelProvider(this).get(DaerahViewModel::class.java)

        mViewModelDaerah.getAllProvince().observe(requireActivity(), Observer {
            it?.let {listDataDaerah ->
                provinceList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.province_name ?: ""
                }.toTypedArray()
            }
            tempUser?.let { setUIProvince(it) }
        })

        mViewModelDaerah.getAllCity().observe(requireActivity(), Observer {
            it?.let {listDataDaerah ->
                cityList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.kabupaten_name ?: ""
                }.toTypedArray()
            }
            tempUser?.let { setUICity(it) }
        })

        mViewModelDaerah.getAllStates().observe(requireActivity(), Observer {
            it?.let {listDataDaerah ->
                villageList = listDataDaerah.map {dataDaerah ->
                    return@map dataDaerah.kecamatan_name ?: ""
                }.toTypedArray()
            }
            tempUser?.let {setUIStates(it) }
        })

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
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int { // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay
            .getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tempUser?.let { updateUI(it) }

        view.findViewById<ImageView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
        view.findViewById<TextView>(R.id.btn_reset)?.setOnClickListener {
            submitForm()
        }
        view.findViewById<TextView>(R.id.btn_action_submit)?.setOnClickListener {
            view.findViewById<TextView>(R.id.btn_reset)?.performClick()
        }

        spinner_profile_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getCity(provinceList[position])
            }
        }

        spinner_profile_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModelDaerah.getStates(cityList[position])
            }
        }

        mViewModelDaerah.getProvince()
    }

    private fun setUIProvince(user: User){
        spinner_profile_province.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            provinceList
        )

        spinner_profile_province.setSelection(provinceList.indexOf(user.country))
    }

    private fun setUICity(user: User){
        spinner_profile_city.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            cityList
        )

        spinner_profile_city.setSelection(cityList.indexOf(user.city))
    }

    private fun setUIStates(user: User){
        spinner_profile_village.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            villageList
        )

        spinner_profile_village.setSelection(villageList.indexOf(user.state))
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
            else -> {
//                view?.findViewById<TextView>(R.id.et_profile_first_name)?.setText(user.first_name)
                et_profile_first_name?.setText(user.first_name)
                et_profile_last_name?.setText(user.last_name)
                rbtn_male?.isChecked = user.gender == "male"
                rbtn_female?.isChecked = user.gender == "female"
            }
        }
    }

    private fun submitForm() {
        var body = mutableMapOf<String, String>()
        if (tempUser?.id?.toString().isNullOrEmpty()) {
            MyToast.make(context).showErrorL(getString(R.string.txt_error_try_again_later))
            return
        }
        context?.let { progressBar.show(it, "Silakan tunggu...") }
        when (profileType) {
            ProfileType.ADDRESS -> {
                body.putIfDataNotNull(
                    "address",
                    et_profile_address?.text?.toString(),
                    oldValue = tempUser?.address
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
            progressBar.dialog.dismiss()
            Toast.makeText(context, "${it["message"]}", Toast.LENGTH_SHORT).show()
            if (it["networkRespone"]?.equals(NetworkResponse.SUCCESS)!!) {
                (activity as? ProfileActivity)?.refreshData(true)
                this.dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? ProfileActivity)?.refreshData(mViewModel.getIsSuccessUpdatee().value)
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