package id.sisi.postoko.utils

import android.content.Context
import android.widget.ArrayAdapter
import id.sisi.postoko.model.DataSpinner

class MySpinnerAdapter(
    context: Context,
    resource: Int,
    var objects: MutableList<DataSpinner>
) :
    ArrayAdapter<String>(context, resource, objects.map {
        it.name
    })