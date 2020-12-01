package com.example.messengerapp.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.messengerapp.MessageChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class  MyFirebaseMessaging : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val sent = p0.data["sent"]
        val user = p0.data["user"]
        val sharedpref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val curruser = sharedpref.getString("currentuser", "none")
        val firebaseuser = FirebaseAuth.getInstance().currentUser
        if(firebaseuser != null && sent == firebaseuser!!.uid)
        {
            if(curruser != null)
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    sendOreoNotification(p0)
                }
                else
                {
                    sendNotification(p0)
                }
            }

        }
    }

    private fun sendNotification(p0: RemoteMessage) {
        val user = p0.data["user"]
        val icon = p0.data["icon"]
        val title = p0.data["title"]
        val body = p0.data["body"]
        val notification = p0.notification
        val j = user!!.replace("[//D]".toRegex(), "").toInt()
        val intent = Intent(this,MessageChatActivity :: class.java)
        val bundle = Bundle()
        bundle.putString("userid", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(this).setSmallIcon(icon!!.toInt())
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(defaultsound)
            .setAutoCancel(true)

        val notif = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var i = 0
        if(j>0)
        {
            i = j
        }
        notif!!.notify(i,builder.build())


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendOreoNotification(p0: RemoteMessage) {
        val user = p0.data["user"]
        val icon = p0.data["icon"]
        val title = p0.data["title"]
        val body = p0.data["body"]
        val notification = p0.notification
        val j = user!!.replace("[//D]".toRegex(), "").toInt()
        val intent = Intent(this,MessageChatActivity :: class.java)
        val bundle = Bundle()
        bundle.putString("userid", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val oreonotif = OreoNotification(this)
        val builder  : Notification.Builder = oreonotif.getOreoNotification(title, body, pendingIntent, defaultsound, icon)
        var i = 0
        if(j>0)
        {
            i = j
        }
        oreonotif.getmanager!!.notify(i,builder.build())

    }
}