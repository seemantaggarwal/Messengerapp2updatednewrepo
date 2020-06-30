 package com.example.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

 class RegisterActivity : AppCompatActivity() {

     private lateinit var auth: FirebaseAuth
     private lateinit var refusers : DatabaseReference
      var firebaseuserid :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val toolbar : Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "REGISTER"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity , WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        auth = FirebaseAuth.getInstance()
        register_button.setOnClickListener{
            registeruser()
        }
    }

     private fun registeruser() {
         val username : String = user_name.text.toString()
         val pass : String  = password_text.text.toString()
         val email :String = email_register.text.toString()
         if(username.equals(""))
         {
             Toast.makeText(this@RegisterActivity, "Please write a Valid Username", Toast.LENGTH_SHORT).show()

         }
         else if(pass.equals(""))
         {
            Toast.makeText(this@RegisterActivity, "Please write a Valid Password", Toast.LENGTH_SHORT).show()
         }
         else  if(email.equals(""))
         {
             Toast.makeText(this@RegisterActivity, "Please write a Valid Email", Toast.LENGTH_SHORT).show()

         }
         else
         {
             auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{task -> if(task.isSuccessful)
             {
                   firebaseuserid = auth.currentUser!!. uid
                 refusers = FirebaseDatabase.getInstance().reference.child("USERS").child(firebaseuserid)
                 val userhash = HashMap<String, Any>()
                 userhash["uid"] =  firebaseuserid
                 userhash["username"] = username
                 userhash["profile"] = "https://firebasestorage.googleapis.com/v0/b/messengerapp-b51fe.appspot.com/o/profile.png?alt=media&token=fe7b3b10-49bf-4cf7-b006-c3b62b1627df"
                 userhash["cover"] = "https://firebasestorage.googleapis.com/v0/b/messengerapp-b51fe.appspot.com/o/cover.png?alt=media&token=49689315-127b-47b6-8554-820b8e8c8b87"
                 userhash["search"] = username.toLowerCase()
                 userhash["facebook"] = "https://www.facebook.com/"
                 userhash["instagram"] = "https://www.instagram.com/"
                 userhash["website "] = "https://www.google.com/"
                 userhash["email"] = email
                 userhash["pass"] = pass
                 userhash["status"] = "offline"
                 refusers.updateChildren(userhash).addOnCompleteListener{
                     task ->
                     if(task.isSuccessful)
                     {
                         val intent = Intent(this@RegisterActivity , MainActivity::class.java)
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                         startActivity(intent)
                         finish()
                     }
                 }


             }
             else
             {
                 Toast.makeText(this@RegisterActivity, "Error Message:" + task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()

             }
             }
         }
     }
 }