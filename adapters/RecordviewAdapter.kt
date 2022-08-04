package com.example.irrigation_app.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.irrigation_app.R
import com.example.irrigation_app.supports.record

class recordviewAdapter(val con: Activity, var trig:MutableList<String>, var r:MutableList<record>) :
    ArrayAdapter<String>(con, R.layout.recordrow,trig){

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val inflater=con.layoutInflater
            val row=inflater.inflate(R.layout.recordrow,null,true)
            val date=row.findViewById<TextView>(R.id.date)
            val crop=row.findViewById<TextView>(R.id.cropv)
            val temp=row.findViewById<TextView>(R.id.tempv)
            val humid=row.findViewById<TextView>(R.id.humidv)
            val sm=row.findViewById<TextView>(R.id.smv)
            val st=row.findViewById<TextView>(R.id.stv)
            val pred=row.findViewById<TextView>(R.id.pv)
            val cc = row.findViewById<TextView>(R.id.cc1)
            val ca = row.findViewById<TextView>(R.id.ca1)
            date.text=r[position].date
            crop.text=r[position].crop
            temp.text=r[position].temparature.toString()
            humid.text=r[position].humidity.toString()
            sm.text=r[position].soil_moisture.toString()
            st.text=r[position].soil_texture
            pred.text=r[position].water_prediction.toString()
            cc.text= r[position].cc.toString()
            ca.text= r[position].ca.toString()
            return  row
        }

    }