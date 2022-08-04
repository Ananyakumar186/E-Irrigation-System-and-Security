package com.example.irrigation_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.irrigation_app.extensions.extensions.toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_crop_soilt.*
import kotlinx.android.synthetic.main.activity_irrigation.*

class IrrigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_irrigation)

        /***
         Taken a database reference with url to the real time database.
          ***/
        val database = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://irrigation-a93de-default-rtdb.firebaseio.com")

        /*** Below is the value event listener used to listen to the flag running which is
         toggled on and off as a result the irrigate and force-stop button are disabled and enabled.***/
        val get = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val running = Integer.parseInt(snapshot.child("flags").child("running").getValue().toString())
                if(running==1){
                    irrigate.isEnabled=false
                    stop.isEnabled=true
                }else{
                    irrigate.isEnabled=true
                    stop.isEnabled=false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                toast("Fail to get status")
            }

        }
        database.addValueEventListener(get)


        /*** A value-eventlistener is used with variable getwater which listens to the datachange in
         firebase database and then sensor-value flag value is fetched and put into condition,
         if it is 1 then the data of crop and rest of the paramters is fetched and displayed in app,
         else a toast message is displayed.***/

        val getwater = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val water = findViewById<EditText>(R.id.editTextTextPersonName)
                val sensorv = Integer.parseInt(snapshot.child("flags").child("sensor_value").value.toString())
                //sen = sensorv as Int
                val running = Integer.parseInt(snapshot.child("flags").child("running").value.toString())
                if (sensorv == 1) {
                    val crop = findViewById<TextView>(R.id.cropv)
                    val temp = findViewById<TextView>(R.id.tempv)
                    val humid = findViewById<TextView>(R.id.humidv)
                    val sm = findViewById<TextView>(R.id.smv)
                    val st = findViewById<TextView>(R.id.stv)
                    val cc = findViewById<TextView>(R.id.cc)
                    val ca = findViewById<TextView>(R.id.ca)
                    val pred = findViewById<TextView>(R.id.pv)
                    //val pred = findViewById<TextView>(R.id.pv)
                    val humidity = snapshot.child("records").child("current").child("humidity")
                        .getValue<Int>()!!
                    val soil_moisture =
                        snapshot.child("records").child("current").child("soil moisture")
                            .getValue<Int>()!!
                    val soil_texture =
                        snapshot.child("records").child("current").child("soil texture")
                            .getValue<String>().toString()
                    val temparature =
                        snapshot.child("records").child("current").child("temparature")
                            .getValue<Int>()!!
                    val water_prediction =
                        snapshot.child("records").child("current").child("water prediction")
                            .getValue<Int>()!!
                    val c = snapshot.child("records").child("current").child("crop").getValue()
                        .toString()
                    val cropc = snapshot.child("records").child("current").child("cropcount").getValue<Int>()!!
                    val cropa = snapshot.child("records").child("current").child("cropage").getValue<Int>()!!

                    crop.text = c
                    temp.text = temparature.toString()
                    humid.text = humidity.toString()
                    sm.text = soil_moisture.toString()
                    st.text = soil_texture
                    val mul = water_prediction * cropc
                    water.setText(mul.toString())
                    cc.text = cropc.toString()
                    ca.text = cropa.toString()
                    pred.text = water_prediction.toString()


                }
            }
            override fun onCancelled(error: DatabaseError) {
                toast("Fail to get water required")
            }
        }
        /*** when the button is pressed with id stirri then another button called irrigate is enabled
         along with edit-text for user input and also the 'getwater' object variable is called with
         'add-value eventlistener' function to fetch and display the data in app and along with it
        flags are manipulated in the firebase database for control.***/

        stirri.setOnClickListener{
            irrigate.isEnabled = true
            editTextTextPersonName.isFocusableInTouchMode = true
            Toast.makeText(this,"Irrigate button is activated ",Toast.LENGTH_LONG).show()
            database.child("flags").child("trigger").setValue(1)
            database.addValueEventListener(getwater)
           // database.child("flags").child("sensor_value").setValue(0)

            irrigate.setOnClickListener {
                    val litre = editTextTextPersonName.text.toString().toFloat()
                    database.child("general").child("water_required").setValue(litre)
                    database.child("flags").child("valve").setValue(1)
                    //database.child("flags").child("trigger").setValue(0)
                    //database.child("flags").child("force_stop").setValue(0)
                    Toast.makeText(this, "Irrigating the plant", Toast.LENGTH_SHORT).show()

            }


        }
        /*** the force-stop button is used for stopping the irrigation process by setting the flag
         to 1 when running flag is 1.***/
        stop.setOnClickListener{
            database.child("flags").child("force_stop").setValue(1)

        }
    }

}
