package id.sisi.postoko.view.ui.payment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.utils.LoadImageFromUrl
import id.sisi.postoko.utils.extensions.logE
import kotlinx.android.synthetic.main.dialog_image.*


class ImagePaymentDialogFragment: DialogFragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        return inflater.inflate(R.layout.dialog_image, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadImage = LoadImageFromUrl(iv_image)

        val attachment = arguments?.getString("payment")
        loadImage.execute("https://qp.forca.id/welcome/$attachment")
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}