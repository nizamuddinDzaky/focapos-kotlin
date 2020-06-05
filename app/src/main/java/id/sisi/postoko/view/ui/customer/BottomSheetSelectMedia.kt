package id.sisi.postoko.view.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import kotlinx.android.synthetic.main.fragment_bottom_sheet_select_media.*

class BottomSheetSelectMedia: BottomSheetDialogFragment() {
    var lCamera: () -> Unit = {}
    var lGallery: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_bottom_sheet_select_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_gallery.setOnClickListener {
            lGallery()
            this.dismiss()
        }

        btn_camera.setOnClickListener {
            lCamera()
            this.dismiss()
        }
    }
}