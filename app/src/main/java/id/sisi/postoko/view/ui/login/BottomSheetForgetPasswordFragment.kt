package id.sisi.postoko.view.ui.login

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


class BottomSheetForgetPasswordFragment : BottomSheetDialogFragment() {
    private lateinit var mViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        return inflater.inflate(R.layout.fragment_bottom_sheet_forget_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager
        ) {
            val bottomSheetFragment = BottomSheetForgetPasswordFragment()
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}