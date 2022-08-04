package com.example.irrigation_app

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.example.irrigation_app.extensions.extensions.toast
import com.example.irrigation_app.utils.firebaseutils.firebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*** Below the token is taked to fetch the saved email of the user from cache and also
         the buttons for directing to irrigation,laser,record view pages.  ***/
        val token = getSharedPreferences("signInEmail", Context.MODE_PRIVATE)
        username.text = token.getString("loginusername","didn't login")

        irri_op.setOnClickListener{
            startActivity(Intent(this, IrrigationActivity::class.java))
        }
        laser_op.setOnClickListener{
            startActivity(Intent(this, LaserActivity::class.java))
        }
        record_view.setOnClickListener{
            startActivity(Intent(this,RecordViewActivity::class.java))
        }
        Crop_SoilB.setOnClickListener{
            startActivity(Intent(this,Crop_SoiltActivity::class.java))
        }
    }
/*** Below is the function used for creating the menu and then inflating the options available ***/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true

    /*** Below is the function for selecting the option from the menu and it's corresponding
     event process in which case the log out and removing the saved email from shareddpreference. ***/
    }
    override fun onOptionsItemSelected(menu: MenuItem):
        Boolean{
            if (menu.itemId == R.id.menu_signout){
                val token = getSharedPreferences("signInEmail", Context.MODE_PRIVATE)
                firebaseAuth.signOut()
                val editor = token.edit()
                editor.putString("loginusername"," ")
                editor.commit()
                startActivity(Intent(this, LoginActivity::class.java))
                toast("signed out")
                finish()
            }

        return super.onOptionsItemSelected(menu)
        }
    }
