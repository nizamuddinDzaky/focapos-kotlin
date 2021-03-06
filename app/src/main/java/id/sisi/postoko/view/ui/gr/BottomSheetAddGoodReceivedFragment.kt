package id.sisi.postoko.view.ui.gr

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.sisi.postoko.R
import id.sisi.postoko.adapter.ListItemAddGoodReceiveAdapter
import id.sisi.postoko.model.DataSpinner
import id.sisi.postoko.model.GoodReceived
import id.sisi.postoko.model.Warehouse
import id.sisi.postoko.utils.KEY_GOOD_RECEIVED
import id.sisi.postoko.utils.MySpinnerAdapter
import id.sisi.postoko.utils.extensions.*
import id.sisi.postoko.view.custom.CustomProgressBar
import id.sisi.postoko.view.ui.warehouse.WarehouseViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_add_good_received.*


class BottomSheetAddGoodReceivedFragment : BottomSheetDialogFragment() {

    lateinit var viewModel: AddGoodReceivedViewModel
    private lateinit var adapter: ListItemAddGoodReceiveAdapter
    private lateinit var vmWareHouse: WarehouseViewModel
    private var listWarehouse: List<Warehouse> = ArrayList()
    private var idWarehouse: String = "0"
    var listener: () -> Unit = {}
    private val progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vmWareHouse = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        return inflater.inflate(R.layout.fragment_bottom_sheet_add_good_received, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_close.setOnClickListener {
            this.dismiss()
        }

        val goodReceived = arguments?.getParcelable<GoodReceived>(KEY_GOOD_RECEIVED)

        initSpinnerWarehouse(goodReceived)

        //adapter = ListItemAddGoodReceiveAdapter()
        /*rv_item_gr?.layoutManager = LinearLayoutManager(this.context)
        rv_item_gr?.setHasFixedSize(false)
        rv_item_gr?.adapter = adapter*/

        goodReceived?.let {
            viewModel = ViewModelProvider(this, AddGoodReceivedFactory(it.id?.toInt() ?: 0)).get(
                AddGoodReceivedViewModel::class.java
            )

            viewModel.getMessage().observe(viewLifecycleOwner, Observer {str ->
                str ?.let {message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })
            viewModel.getIsExecute().observe(viewLifecycleOwner, Observer {
                if (it && !progressBar.isShowing()) {

                    context?.let { context -> progressBar.show(context, getString(R.string.txt_please_wait)) }
                } else {
                    progressBar.dialog.dismiss()
                }
            })

            viewModel.getDetailGoodReceived().observe(viewLifecycleOwner, Observer { gr ->
                gr?.let {
                    logE("${gr.goodReceivedItems}")
                    //gr.goodReceivedItems?.let { item -> adapter.updateData(item) }
                    tv_detail_good_received_name?.text = gr.nama_produk
                    tryMe {
                        val price =
                            (gr.grand_total?.toDouble() ?: 0.0).div(gr.qty_do?.toDouble() ?: 1.0)
                        tv_detail_good_received_price.text = price.toCurrencyID()
                        et_detail_good_received_new_price?.setText(price.format(0))
                    }
                    tv_detail_good_received_quantity?.text = gr.qty_do?.toDouble()?.toNumberID()

                    tv_detail_good_received_spj_no?.text = gr.no_spj
                    tv_detail_good_received_total?.text = gr.grand_total?.toDouble()?.toCurrencyID()
                }
            })
            viewModel.requestDetailGoodReceived()
        }

        btn_confirmation_good_recieved?.setOnClickListener {
            actionAddGoodReceived()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.setupFullHeight(context as Activity)
        }
        return dialog
    }

    private fun initSpinnerWarehouse(goodReceived: GoodReceived?) {
        val adapterWarehouse = context?.let {
            MySpinnerAdapter(
                it,
                R.layout.list_spinner
            )
        }

        vmWareHouse.getListWarehouses().observe(viewLifecycleOwner, Observer {

            it?.let {
                adapterWarehouse?.udpateView(it.map {w->
                    logE("warehouse : ${w.name} => ${w.id}")
                    return@map DataSpinner(w.name, w.id)
                }.toMutableList())
                listWarehouse = it
            }

        })
        sp_warehouse.adapter = adapterWarehouse
        sp_warehouse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                idWarehouse = listWarehouse[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

    }

    private fun actionAddGoodReceived() {
        val body: MutableMap<String, Any> = mutableMapOf(
           "price" to (et_detail_good_received_new_price?.text?.toString() ?: ""),
            "warehouse_id" to idWarehouse
        )

        /*val body = mutableMapOf<String, String>()
        et_detail_good_received_new_price?.text?.let {
            body["price"] = it.toString()
        }*/
        viewModel.postAddGoodReceived(body) {
            listener()
            this.dismiss()
        }
    }

    companion object {
        fun showBottomSheet(
            fragmentManager: FragmentManager,
            goodReceived: GoodReceived?,
            listener: () -> Unit
        ) {
            val bottomSheetFragment = BottomSheetAddGoodReceivedFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_GOOD_RECEIVED, goodReceived)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.listener = listener
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
}