package com.example.messengerapp.notifications

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceId : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        val firebaseuser = FirebaseAuth.getInstance().currentUser
        val refresh = FirebaseInstanceId.getInstance().token
        if(firebaseuser != null)
        {
            updatetoken(refresh)
        }

    }

    private fun updatetoken(refresh: String?) {

        val firebaseuser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
        val token = Token(refresh!!)
        ref.child(firebaseuser!!.uid).setValue(token)
    }

}