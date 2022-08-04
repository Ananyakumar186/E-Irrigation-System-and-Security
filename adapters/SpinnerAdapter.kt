package com.example.irrigation_app.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.irrigation_app.R
import com.example.irrigation_app.supports.record

class SpinnerAdapter(val con: Activity, var trig:MutableList<String>, var cr:MutableList<record>,var st:MutableList<record>) :
    ArrayAdapter<String>(con, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,trig){

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater=con.layoutInflater
        val row=inflater.inflate(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,null,true)
        cr[position].key
        st[position].key
        cr[position].crop
        st[position].soil_texture

        return super.getDropDownView(position, convertView, parent)
    }

}