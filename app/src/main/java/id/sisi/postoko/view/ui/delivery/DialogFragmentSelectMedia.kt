package id.sisi.postoko.view.ui.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.sisi.postoko.R
import kotlinx.android.synthetic.main.dialog_fragment_select_media.*

class DialogFragmentSelectMedia: DialogFragment() {

    var lCamera: () -> Unit = {}
    var lGallery: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        return inflater.inflate(R.layout.dialog_fragment_select_media, container, false)
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