package com.example.irrigation_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.irrigation_app.extensions.extensions.toast
import com.example.irrigation_app.utils.firebaseutils.firebaseAuth
import com.example.irrigation_app.utils.firebaseutils.firebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    /*** The variables are declared along with the database path. ***/
    private lateinit var auth: FirebaseAuth;
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var createAccountArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        createAccountArray = arrayOf(etEmailAddress, etPassword, etcPassword)
        register_btn.setOnClickListener {
            signIn()
        }
          auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            startActivity(Intent(this, MainActivity::class.java))
            toast("welcome back")
        }
    }
    /*** Below is a function for checking if the fields are empty***/
    private fun notEmpty(): Boolean = etEmailAddress.text.toString().trim().isNotEmpty() &&
            etPassword.text.toString().trim().isNotEmpty() &&
            etcPassword.text.toString().trim().isNotEmpty()

    /*** Below is a function for checking if the passwords are identical***/
    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            etPassword.text.toString().trim() == etcPassword.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        } else {
            toast("passwords are not matching !")
        }
        return identical
    }

    /*** Below is the function for registering the user where user's email and password is taken
    and saved in the authentication database of firebase  for accuracy and directed to login page
     and along with that the email is saved in the cache. ***/
    private fun signIn() {
        if (identicalPassword()) {
            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
            userEmail = etEmailAddress.text.toString().trim()
            userPassword = etPassword.text.toString().trim()

            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        toast("created account successfully !")
                        //sendEmailVerification()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        toast("failed to Authenticate !")
                    }
                }
        }
    }
    /***private fun sendEmailVerification(){
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful ) {
                    toast("email sent to $userEmail")
                }
            }
        }
    }***/
}