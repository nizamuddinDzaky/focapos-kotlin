package id.sisi.postoko.utils

import android.content.Context
import android.widget.ArrayAdapter
import id.sisi.postoko.model.DataSpinner

class MySpinnerAdapter(
    context: Context,
    resource: Int,
    var objects: MutableList<DataSpinner> = mutableListOf()
) :
    ArrayAdapter<String>(context, resource, objects.map {
        it.name
    }){
    fun udpateView(newObject: MutableList<DataSpinner>){
        objects = newObject
        clear()
        addAll(objects.map{
            it.name
        })
    }
}