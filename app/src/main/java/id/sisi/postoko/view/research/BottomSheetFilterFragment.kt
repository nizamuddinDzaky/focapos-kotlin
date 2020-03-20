package id.sisi.postoko.view.research

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.KEY_FILTER
import id.sisi.postoko.utils.KEY_GR_STATUS
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter.view.*


class BottomSheetFilterFragment : BottomSheetDialogFragment() {
    var listener: (HashMap<String, String>) -> Unit = {}
    var mFilter = hashMapOf<String, String>()

    private val listenerRadioButton = View.OnClickListener {
        checkRadio(it.id)
        mFilter[KEY_GR_STATUS] = when (it.id) {
            R.id.rbtn_reserved -> GoodReceiveStatus.RECEIVED.name
            else -> GoodReceiveStatus.ALL.name
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(KEY_FILTER)
        mFilter =
            (arguments?.getSerializable(KEY_FILTER) as? HashMap<String, String>) ?: hashMapOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_filter, container, false).apply {
            btn_close?.setOnClickListener {
                dismiss()
            }
            btn_reset?.setOnClickListener {
                resetForm()
            }
            btn_action_submit?.setOnClickListener {
                sendFilter()
            }
            rbtn_cancel?.setOnClickListener(listenerRadioButton)
            rbtn_confirmed?.setOnClickListener(listenerRadioButton)
            rbtn_reserved?.setOnClickListener(listenerRadioButton)
            rbtn_pending?.setOnClickListener(listenerRadioButton)
            rbtn_closed?.setOnClickListener(listenerRadioButton)
        }
    }

    private fun sendFilter() {
        listener(mFilter)
    }

    private fun resetForm() {
        checkRadio(0)
    }

    private fun checkRadio(id: Int) {
        rbtn_cancel?.isChecked = id == R.id.rbtn_cancel
        rbtn_confirmed?.isChecked = id == R.id.rbtn_confirmed
        rbtn_reserved?.isChecked = id == R.id.rbtn_reserved
        rbtn_pending?.isChecked = id == R.id.rbtn_pending
        rbtn_closed?.isChecked = id == R.id.rbtn_closed
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            filter: HashMap<String, String>,
            listener: (HashMap<String, String>) -> Unit
        ) {
            val bottomSheetFragment = BottomSheetFilterFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_FILTER, filter)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.listener = listener
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}