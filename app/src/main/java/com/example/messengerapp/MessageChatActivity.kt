package com.example.messengerapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.ModelClasses.Chat
import com.example.messengerapp.ModelClasses.Users
import com.example.messengerapp.adapterclasses.ChatsAdapter
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*
import kotlinx.android.synthetic.main.user_search_item_layout.*

@Suppress("DEPRECATION")
class MessageChatActivity : AppCompatActivity() {
    var useridvisit : String? = null
    var firebaseuser : FirebaseUser? = null
    var chatsAdapter : ChatsAdapter? = null
    var mchatList: List<Chat>? = null
    lateinit var rcviewchats : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)
        intent = intent
        useridvisit = intent.getStringExtra("visit_id")
        firebaseuser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().reference.child("USERS").child(useridvisit!!)

        rcviewchats = findViewById(R.id.rvchats)
        rcviewchats.setHasFixedSize(true)

        var linearlayoutmanager = LinearLayoutManager(applicationContext)

        linearlayoutmanager.stackFromEnd = true
        rcviewchats.layoutManager = linearlayoutmanager

        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user : Users? = snapshot.getValue(Users::class.java)
                usernamemc.text = user!!.getusername()
                Picasso.get().load(user.getprofile()).into(profileimage_mc)

                retrievemessages(firebaseuser!!.uid, useridvisit!!, user.getprofile())
            }

        })

        sendmessage.setOnClickListener {
            val msg  = textmessage.text.toString()
            if(msg == "")
            {
                Toast.makeText(this@MessageChatActivity, "PLEASE TYPE SOMETHING", Toast.LENGTH_SHORT).show()
            }
            else
            {
                sendmessagetouser(firebaseuser!!.uid, useridvisit, msg)
            }
            textmessage.setText("")
        }
        attachimage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Pick Image"), 438)
        }
    }



    private fun sendmessagetouser(senderid: String, recieverid: String?, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messagekey = reference.push().key
        var msgmap = HashMap<String, Any?>()
        msgmap["sender"] = senderid
        msgmap["message"] = message
        msgmap["receiver"] = recieverid
        msgmap["isseen"] = false
        msgmap["url"] = ""
        msgmap["messageid"] = messagekey
        reference.child("Chats").child(messagekey!!).setValue(msgmap).addOnCompleteListener {
            task ->
            if(task.isSuccessful)
            {
                val chatslist = FirebaseDatabase.getInstance().reference.child("ChatLists").child(firebaseuser!!.uid).child(useridvisit!!)
                chatslist.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(!snapshot.exists())
                            chatslist.child("id").setValue(useridvisit)

                        val chatslistReceiver = FirebaseDatabase.getInstance().reference.child("ChatLists").child(useridvisit!!).child(firebaseuser!!.uid)

                        chatslistReceiver.child("id").setValue(firebaseuser!!.uid)

                    }

                })




                val reference = FirebaseDatabase.getInstance().reference.child("USERS").child(firebaseuser!!.uid)

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 438 && resultCode == Activity.RESULT_OK && data!!.data != null)
        {
            val progressbar = ProgressDialog(this)

            progressbar.setMessage("Please Wait, Image is Uploading...")
            progressbar.show()

            val fileurl = data.data

            val storageref = FirebaseStorage.getInstance().reference.child("Chat Images")

            val ref = FirebaseDatabase.getInstance().reference

            val messageid = ref.push().key

            val filepath = storageref!!.child("$messageid.jpg")

            var uploadtask: StorageTask<*>

            uploadtask = filepath.putFile(fileurl!!)

            uploadtask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                if(!task.isSuccessful)
                {
                    task.exception?.let { throw it }
                }
                return@Continuation filepath.downloadUrl
            }).addOnCompleteListener {
                task ->
                if(task.isSuccessful)
                {
                    val downloadurl = task.result
                    val muri = downloadurl.toString()
                    var msgmap = HashMap<String, Any?>()
                    msgmap["sender"] = firebaseuser!!.uid
                    msgmap["message"] = "sent you an image"
                    msgmap["receiver"] = useridvisit
                    msgmap["isseen"] = false
                    msgmap["url"] = muri
                    msgmap["messageid"] = messageid
                    ref.child("Chats").child(messageid!!).setValue(msgmap)
                    progressbar.dismiss()

                }
            }

        }
    }
    private fun retrievemessages(senderid: String, recieverid: String, imageurl: String?) {
        mchatList = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                (mchatList as ArrayList<Chat>).clear()
                for (snapshots in snapshot.children)
                {
                    val chat = snapshots.getValue(Chat::class.java)
                    if((chat!!.getreceiver() == senderid && chat.getsender() == recieverid) || (chat.getreceiver() == recieverid && chat.getsender() == senderid))
                    {
                        (mchatList as ArrayList<Chat>).add(chat)

                    }
                    chatsAdapter = ChatsAdapter(this@MessageChatActivity, (mchatList as ArrayList<Chat>), imageurl!! )
                    rcviewchats.adapter = chatsAdapter

                }
            }

        })

    }
}