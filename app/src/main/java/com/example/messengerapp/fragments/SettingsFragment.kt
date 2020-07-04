 package com.example.messengerapp.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.messengerapp.ModelClasses.Users
import com.example.messengerapp.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

 // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var usersref : DatabaseReference? = null
    var firebaseuser : FirebaseUser? = null
    private val reqcode = 438
    private var imageuri : Uri? = null
    private var storageref : StorageReference? = null
    private var coverchecker : String? = null
    private var socialchecker : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_settings, container, false)
        firebaseuser = FirebaseAuth.getInstance().currentUser
        usersref = FirebaseDatabase.getInstance().reference.child("USERS").child(firebaseuser!!.uid)
        view.profileimage.setOnClickListener{
            coverchecker = "profile"
            pickimage()
        }
        view.coverimage.setOnClickListener {
            coverchecker = "cover"
            pickimage()
        }
        view.setfacebook.setOnClickListener {
            socialchecker = "facebook"
            setsociallinks()

        }
        view.setinstagram.setOnClickListener {
            socialchecker = "instagram"
            setsociallinks()

        }
        view.setwebsite.setOnClickListener {
            socialchecker = "website"
            setsociallinks()

        }
        storageref = FirebaseStorage.getInstance().reference.child("UserImages")
        usersref!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user : Users? = snapshot.getValue(Users::class.java)
                    if(context!=null) {
                        view.username.text = user!!.getusername().toString()
                        username.setText(user!!.getusername().toString())
                        //Toast.makeText(context, "${user.getprofile().toString()}...", Toast.LENGTH_SHORT).show()
                        Picasso.get().load(user.getprofile().toString()).placeholder(R.drawable.profile)
                            .into(view.profileimage)
//                        Picasso.get().load(user.getcover().toString()).placeholder(R.drawable.cover).into(view.coverimage)
                        Picasso.get().load(user.getcover().toString()).placeholder(R.drawable.cover)
                            .into(view.coverimage)
                    }
                    else
                    {
                        Picasso.get().load(R.drawable.profile).placeholder(R.drawable.profile)
                            .into(view.profileimage)
                        Picasso.get().load(R.drawable.cover).placeholder(R.drawable.cover).into(view.coverimage)

                    }


                }
            }

        })

        return view
    }

    private fun setsociallinks() {
        val  builder : AlertDialog.Builder = AlertDialog.Builder(requireContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        if(socialchecker == "website")
        {
            builder.setTitle("write URL")
        }

        else
        {
            builder.setTitle("write USERNAME")

        }
        val editText = EditText(context)
        if(socialchecker == "website")
        {
            editText.hint = "write URL"
        }

        else
        {
            editText.hint = "write USERNAME"

        }
        builder.setView(editText)
        builder.setPositiveButton("CREATE", DialogInterface.OnClickListener{
            dialog, which ->
            val str = editText.text.toString()
            if(str == "")
            {
                Toast.makeText(context, "Please write Something", Toast.LENGTH_SHORT).show()
            }
            else
            {
                savesociallink(str)
            }

        })
        builder.setNegativeButton("Cancel" , DialogInterface.OnClickListener{
                dialog, which ->
            dialog.cancel()

        })
        builder.show()
    }

    private fun savesociallink(str: String) {
        val mapsocial = HashMap<String, Any>()
//        mapsocial["cover"] = muri
//        usersref!!.updateChildren(mapcover)
        when (socialchecker)
        {
            "facebook"->{
                mapsocial["facebook"] = "https://m.facebook.com/$str"
            }
            "instagram"->
            {
                mapsocial["instagram"] = "https://m.instagram.com/$str"
            }
            "website"->
            {
                mapsocial["website"] = "https://$str"
            }
        }
              usersref!!.updateChildren(mapsocial).addOnCompleteListener {
                  task ->
                  if(task.isSuccessful)
                  {
                      Toast.makeText(context, "Updated Succesfully", Toast.LENGTH_SHORT).show()
                  }
              }


    }

    private fun pickimage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,438)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == reqcode && resultCode  == Activity.RESULT_OK && data!!.data!=null)
        {
            imageuri = data.data
            Toast.makeText(context, "uploading...", Toast.LENGTH_SHORT).show()
            uploadimage()
        }
    }

    private fun uploadimage() {
        val progressbar = ProgressDialog(context)
        progressbar.setMessage("Image is Uploading")
        progressbar.show()
        if(imageuri != null)
        {
            val fileref  = storageref!!.child(System.currentTimeMillis().toString() + ".jpg")
            var uploadtask: StorageTask<*>
            uploadtask = fileref.putFile(imageuri!!)
            uploadtask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{task ->
                if(!task.isSuccessful)
                {
                    task.exception?.let { throw it }
                }
                return@Continuation fileref.downloadUrl
            }).addOnCompleteListener { task ->
                if(task.isSuccessful)
                {
                    val downloadurl = task.result
                    val muri = downloadurl.toString()
                    if(coverchecker == "cover")
                    {
                        val mapcover = HashMap<String, Any>()
                        mapcover["cover"] = muri
                        usersref!!.updateChildren(mapcover)
                        coverchecker = ""
                    }
                    else
                    {
                        val mapprofile = HashMap<String, Any>()
                        mapprofile["profile"] = muri
                        usersref!!.updateChildren(mapprofile)
                        coverchecker = ""
                    }
                    progressbar.dismiss()
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}