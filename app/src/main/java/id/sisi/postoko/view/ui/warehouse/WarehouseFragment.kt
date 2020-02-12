package id.sisi.postoko.view.ui.warehouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import id.sisi.postoko.R

class WarehouseFragment : Fragment() {

    companion object {
        fun newInstance() = WarehouseFragment()
    }

    private lateinit var viewModel: WarehouseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.warehouse_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
