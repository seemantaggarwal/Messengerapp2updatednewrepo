package com.example.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar : Toolbar = findViewById(R.id.toolbar_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "LOGIN"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@LoginActivity , WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        auth = FirebaseAuth.getInstance()
        login_button.setOnClickListener{
            loginUser()
        }
    }

    private fun loginUser() {
        val pass : String  = password_login.text.toString()
        val email :String = email_login.text.toString()
         if(pass.equals(""))
        {
            Toast.makeText(this@LoginActivity, "Please write a Valid Password", Toast.LENGTH_SHORT).show()
        }
        else  if(email.equals(""))
        {
            Toast.makeText(this@LoginActivity, "Please write a Valid Email", Toast.LENGTH_SHORT).show()

        }
        else
         {
             auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{task -> if(task.isSuccessful)
             {
                 val intent = Intent(this@LoginActivity  , MainActivity::class.java)
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                 startActivity(intent)
                 finish()

             }
             else
             {
                 Toast.makeText(this@LoginActivity, "Error Message:" + task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()

             }}

         }
    }
}
