package com.example.irrigation_app

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.irrigation_app.extensions.extensions.toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_laser.*

class LaserActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laser)
        /*** A textview is initialized and then firebase database url is initialized to variable
         database1 and a switch variable is declared and initialized from the view. ***/

        val statusthread = findViewById<TextView>(R.id.message)
        val cutthread = findViewById(R.id.cutmessage) as TextView
        val database1 = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://irrigation-a93de-default-rtdb.firebaseio.com")
        val dc = findViewById<Switch>(R.id.switch1)

        /*** the switch library has a object called setOnCheckedChangeListener which listens
         to the switch manipulated by the user and according to that the textview and laserStatus
         flag created in the firebase database is manipulated. ***/

        dc.setOnCheckedChangeListener { _, b ->
            if (b) {
                database1.child("flags").child("laserStatus").setValue(1)
            } else {
                database1.child("flags").child("laserStatus").setValue(0)
            }
        }

        val getstatus=object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
        val s = Integer.parseInt(snapshot.child("flags").child("laserStatus").getValue().toString())
            val cut = Integer.parseInt(snapshot.child("flags").child("lasercut").getValue().toString())
            if(s==1){
                statusthread.text = "Laser is on "
        }else{
                statusthread.text = "Laser is off "
        }

            if(cut==1){
                cutthread.text = "detected"

            }else{
                cutthread.text = " off"

            }
            for(i in snapshot.child("laserLog").child("log").children){

            }
        }
        override fun onCancelled(error: DatabaseError) {
        toast("Fail to get data")
        }
        }
        database1.addValueEventListener(getstatus)
    }
    }
