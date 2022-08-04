package com.example.irrigation_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.irrigation_app.extensions.extensions.toast
import com.example.irrigation_app.supports.record
import com.example.irrigation_app.adapters.recordviewAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class RecordViewActivity : AppCompatActivity() {
    /*** A Firebase database path is taken in a variable ***/
    val database3 = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://irrigation-a93de-default-rtdb.firebaseio.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_view)

      /***Below is the variable object for fetching the records of irrigation from the firebase
       database and then setting it in ' custom listview' which contains textview for displaying
      in the app. ***/
        val getdata = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val listv = findViewById<ListView>(R.id.list)
                var Reclist = mutableListOf<record>()
                var trig= mutableListOf<String>()

                for (i in snapshot.child("records").child("recordDB").children) {
                    var r= record()
                    r.date=i.key+""
                    r.humidity= i.child("humidity").getValue<Integer>()!!
                    r.soil_moisture = i.child("soil moisture").getValue<Integer>()!!
                    r.soil_texture= i.child("soil texture").getValue<String>().toString()
                    r.temparature= i.child("temparature").getValue<Integer>()!!
                    r.water_prediction = i.child("water prediction").getValue<Integer>()!!
                    r.crop=i.child("crop").getValue().toString()
                    r.cc = i.child("cropcount").getValue<Integer>()!!
                    r.ca = i.child("cropage").getValue<Integer>()!!
                    Reclist.add(r)
                    trig.add(r.date)
                }
                var adapter = recordviewAdapter(this@RecordViewActivity, trig,Reclist)
                listv.setAdapter(adapter)
            }
            override fun onCancelled(error: DatabaseError) {
                toast("Fail to get data")
            }
        }
        database3.addValueEventListener(getdata)
    }
}







