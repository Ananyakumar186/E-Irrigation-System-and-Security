package com.example.irrigation_app.extensions

import android.app.Activity
import android.widget.Toast

object extensions {
    fun Activity.toast(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}