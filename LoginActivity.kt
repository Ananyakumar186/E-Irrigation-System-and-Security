package com.example.irrigation_app

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.irrigation_app.classes.MyService
import com.example.irrigation_app.extensions.extensions.toast
import com.example.irrigation_app.utils.firebaseutils.firebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    /*** The variables are declared along with the database path. ***/
    lateinit var signinEmail: String
    private lateinit var signinPassword: String
    lateinit var signinArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        startService(Intent(this,MyService::class.java))
        /*** Below is the sharedPreference feature which is the virtual cache used for storing
        user email for direct access to dashboard if already logged in after installation.
         A login button is created for login event and button2 is for directing to register page.***/
        val token = getSharedPreferences("signInEmail", Context.MODE_PRIVATE)
        if (token.getString("loginusername", " ") != " ") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
            login.setOnClickListener {
                signinUser()
            }
            button2.setOnClickListener {
                startActivity(Intent(this,RegisterActivity::class.java))
                finish()
            }
        }

    /*** Below is a function for checking if the fields are empty***/
        private fun notEmpty(): Boolean = signinEmail.isNotEmpty() && signinPassword.isNotEmpty()


    /*** Below is the function for actual login process where user's email and password is taken
     and compared with the authentication firebase database for accuracy and directed to dashboard
    page and along with that the email is saved in the cache. ***/
        private fun signinUser() {
            val token = getSharedPreferences("signInEmail", Context.MODE_PRIVATE)
            signinEmail = etEmailAddress2.text.toString().trim()
            signinPassword = etPassword2.text.toString().trim()

            if (notEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(signinEmail, signinPassword)
                    .addOnCompleteListener { signIn ->
                        if (signIn.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            val editor = token.edit()
                            editor.putString("loginusername",signinEmail)
                            editor.commit()
                            toast("signed in succesfully")
                            finish()
                        } else {
                            toast("sign in failed")
                        }

                    }
            } else {
                signinArray.forEach { input ->
                    if (input.text.toString().trim().isEmpty()) {
                        input.error = "${input.hint} is required"
                    }

                }
            }
        }

    }
