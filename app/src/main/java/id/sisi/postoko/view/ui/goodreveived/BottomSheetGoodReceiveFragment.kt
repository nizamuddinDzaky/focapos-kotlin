package id.sisi.postoko.view.ui.goodreveived

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import id.sisi.postoko.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet_good_received.*


class BottomSheetGoodReceiveFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_good_received, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_confirmation_good_recieved?.setOnClickListener {
            this.dismiss()
        }
    }
}