package com.example.irrigation_app.classes

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.irrigation_app.LaserActivity
import com.example.irrigation_app.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationchannel()
        val database1 = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://irrigation-a93de-default-rtdb.firebaseio.com")
        val getstat: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val s = snapshot.child("flags").child("lasercut").value.toString().toInt()
                val log = snapshot.child("laserLog").child("current").value.toString()
                if (s == 1) {
                    buildnotification(log)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw UnsupportedOperationException("not yet implemented")
            }
        }
        database1.addValueEventListener(getstat)

        //Toast.makeText(this,"This is a Service running in background",Toast.LENGTH_SHORT).show()
        return START_NOT_STICKY

    }

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartSI = Intent(this, this.javaClass)
        restartSI.setPackage(null)
        startService(restartSI)
        super.onTaskRemoved(rootIntent)
    }

    private fun buildnotification(log: String) {
        val intent1 = Intent(this, LaserActivity::class.java)
        val pendingintent = PendingIntent.getActivity(this, 0, intent1, 0)
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(this, "Irri")
            .setContentTitle("Laser threat detected")
            .setContentText(" Time: $log")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingintent)

        NotificationManagerCompat.from(this).apply {
            notify(5400, notification.build())
        }
    }

    private fun createNotificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Irri",
                "Irrigation_app",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
            manager.cancel(5400)
        }

    }
}