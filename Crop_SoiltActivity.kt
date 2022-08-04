package com.example.irrigation_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.irrigation_app.adapters.recordviewAdapter
import com.example.irrigation_app.extensions.extensions.toast
import com.example.irrigation_app.supports.record
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_crop_soilt.*
import kotlinx.android.synthetic.main.activity_irrigation.*


class Crop_SoiltActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://irrigation-a93de-default-rtdb.firebaseio.com")
    var ci = " "
    var sti = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_soilt)
        /***Below is the declaration of Spinner widget for dropdown list ***/
        val clist = findViewById<Spinner>(R.id.croplist)
        val stlist = findViewById<Spinner>(R.id.soiltlist)
        val cropage = findViewById<TextView>(R.id.cropage)
        val cropcount = findViewById<TextView>(R.id.cropcount)
        
        /*** Below is the value event listener to fetch the croplist and soil texture list***/
        val getdata = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val Crlist = mutableListOf<String>()
                val Stlist = mutableListOf<String>()
                val ca = snapshot.child("general").child("cropage").value.toString()
                val cc = snapshot.child("general").child("cropcount").value.toString()
                cropage.text = ca
                cropcount.text = cc
                for (i in snapshot.child("general").child("croplist").children) {
                    val c = i.value.toString()
                    Crlist.add(c)
                }
                for (i in snapshot.child("general").child("soilTextureList").children) {
                    val soil_texture = i.getValue<String>().toString()
                    Stlist.add(soil_texture)
                }
                val addressAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@Crop_SoiltActivity,
                    R.layout.spinner,
                    Crlist
                )
                val addressAdapter1: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@Crop_SoiltActivity,
                    R.layout.spinner1,
                    Stlist
                )
                addressAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                clist.adapter = addressAdapter
                stlist.adapter = addressAdapter1

            }
            override fun onCancelled(error: DatabaseError) {
                toast("$error")
            }
        }
        database.addValueEventListener(getdata)


/*** Below is the insert button to insert crop age and crop count to firebase database***/
        insert3.setOnClickListener {
            val cropcount = cropcount.text.toString().toInt()
            database.child("general").child("cropcount").setValue(cropcount)
            val cropage = cropage.text.toString().toInt()
            database.child("general").child("cropage").setValue(cropage)
            //Toast.makeText(this,"cropcount:"+cropcount,Toast.LENGTH_LONG).show()
        }

        /***Below is the onItemSelected Listener with methods to select an item from the dropdown
         and then inserting to firebase database using the insert button 2. ***/
        clist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item = p0?.getItemAtPosition(p2)
                ci = item.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                throw UnsupportedOperationException("not yet implemented")
            }
        }
        stlist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p1: AdapterView<*>?, p2: View?, p3: Int, p4: Long) {
                val it1 = p1?.getItemAtPosition(p3)
                sti = it1.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                throw UnsupportedOperationException("not yet implemented")
            }
        }
        insert2.setOnClickListener {
            database.child("general").child("crop").setValue(ci)
            database.child("general").child("soil texture").setValue(sti)
        }

    }




    }



