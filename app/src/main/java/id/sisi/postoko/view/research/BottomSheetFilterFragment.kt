package id.sisi.postoko.view.research

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.utils.*
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.ui.gr.GoodReceiveStatus
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter.view.*


class BottomSheetFilterFragment : BottomSheetDialogFragment() {
    var listener: (HashMap<String, String>) -> Unit = {}
    private var mFilter = hashMapOf<String, String>()
    private val dataSortBy = mutableListOf<DataSpinner>()
    private val dataSortType = mutableListOf<DataSpinner>()
    private var mStartPickerDate: MyPickerDate? = null
    private var mEndPickerDate: MyPickerDate? = null
    private lateinit var toast: Toast

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toast = MyToast.make(context)
        mFilter =
            (arguments?.getSerializable(KEY_FILTER) as? HashMap<String, String>) ?: hashMapOf()
        return inflater.inflate(R.layout.fragment_bottom_sheet_filter, container, false).apply {
            layout_delivery_status?.checkVisibility(mFilter.containsKey(KEY_IS_SEARCH))

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

            dataSortBy.add(DataSpinner("Tanggal", "date"))
            dataSortBy.add(DataSpinner("Jumlah", "amount"))
            spinner_sort_by.adapter =
                MySpinnerAdapter(context, android.R.layout.simple_list_item_1, dataSortBy)

            dataSortType.add(DataSpinner("Besar > Kecil", "desc"))
            dataSortType.add(DataSpinner("Kecil > Besar", "asc"))
            spinner_sort_type.adapter =
                MySpinnerAdapter(context, android.R.layout.simple_list_item_1, dataSortType)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mStartPickerDate = MyPickerDate(tv_start_date_range, btn_start_date_range)
        mEndPickerDate = MyPickerDate(tv_end_date_range, btn_end_date_range)

        if (mFilter.containsKey(KEY_START_DATE)) {
            mStartPickerDate?.initDate(mFilter[KEY_START_DATE] as String)
        }
        if (mFilter.containsKey(KEY_END_DATE)) {
            mEndPickerDate?.initDate(mFilter[KEY_END_DATE] as String)
        }

        rbtn_cancel?.isChecked =
            mFilter.containsKey(KEY_GR_STATUS) && mFilter[KEY_GR_STATUS] == "GoodReceiveStatus.ALL.name"
        rbtn_confirmed?.isChecked =
            mFilter.containsKey(KEY_GR_STATUS) && mFilter[KEY_GR_STATUS] == GoodReceiveStatus.RECEIVED.name
        rbtn_reserved?.isChecked =
            mFilter.containsKey(KEY_GR_STATUS) && mFilter[KEY_GR_STATUS] == GoodReceiveStatus.DELIVERING.name
        rbtn_pending?.isChecked =
            mFilter.containsKey(KEY_GR_STATUS) && mFilter[KEY_GR_STATUS] == GoodReceiveStatus.ALL.name
        rbtn_closed?.isChecked =
            mFilter.containsKey(KEY_GR_STATUS) && mFilter[KEY_GR_STATUS] == "GoodReceiveStatus.RECEIVED.name"

        if (mFilter.containsKey(KEY_SORT_BY)) {
            spinner_sort_by?.setIfExist(mFilter[KEY_SORT_BY] as String)
        }
        if (mFilter.containsKey(KEY_SORT_TYPE)) {
            spinner_sort_type?.setIfExist(mFilter[KEY_SORT_TYPE] as String)
        }
    }

    private val listenerRadioButton = View.OnClickListener {
        checkRadio(it.id)
        mFilter[KEY_GR_STATUS] = when (it.id) {
            R.id.rbtn_reserved -> GoodReceiveStatus.DELIVERING.name
            R.id.rbtn_confirmed -> GoodReceiveStatus.RECEIVED.name
            else -> GoodReceiveStatus.ALL.name
        }
    }

    private fun sendFilter() {
        if (mStartPickerDate?.getDate().isNullOrEmpty() xor mEndPickerDate?.getDate().isNullOrEmpty()) {
            toast.showErrorL("Format range tanggal salah")
            return
        }
        val sortBy = spinner_sort_by?.getSelectedValue()
        val sortType = spinner_sort_type?.getSelectedValue()
        sortBy?.let {
            mFilter[KEY_SORT_BY] = sortBy
        }
        sortType?.let {
            mFilter[KEY_SORT_TYPE] = sortType
        }
        mStartPickerDate?.getDate()?.let {
            mFilter[KEY_START_DATE] = it
        }
        mEndPickerDate?.getDate()?.let {
            mFilter[KEY_END_DATE] = it
        }
        listener(mFilter)
    }

    private fun resetForm() {
        mFilter.remove(KEY_GR_STATUS)
        mFilter.remove(KEY_START_DATE)
        mFilter.remove(KEY_END_DATE)
        mStartPickerDate?.reset()
        mEndPickerDate?.reset()
        spinner_sort_by?.setDefault()
        spinner_sort_type?.setDefault()
        checkRadio(0)
    }

    private fun checkRadio(id: Int) {
        rbtn_cancel?.isChecked = id == R.id.rbtn_cancel
        rbtn_confirmed?.isChecked = id == R.id.rbtn_confirmed
        rbtn_reserved?.isChecked = id == R.id.rbtn_reserved
        rbtn_pending?.isChecked = id == R.id.rbtn_pending
        rbtn_closed?.isChecked = id == R.id.rbtn_closed
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