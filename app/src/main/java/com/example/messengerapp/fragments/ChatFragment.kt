package com.example.messengerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.ModelClasses.Chatlist
import com.example.messengerapp.ModelClasses.Users
import com.example.messengerapp.R
import com.example.messengerapp.adapterclasses.useradapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  var userAdapter: useradapter? = null
    private var mUsers : List<Users>? = null
    private var userChatList : List<Chatlist>? = null
    lateinit var rcviewchatlist : RecyclerView
    private var firebaseuser : FirebaseUser? = null



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
        val view =  inflater.inflate(R.layout.fragment_chat, container, false)
        rcviewchatlist  = view.findViewById(R.id.rcviewchat)
        rcviewchatlist.setHasFixedSize(true)
        rcviewchatlist.layoutManager = LinearLayoutManager(context)
        firebaseuser = FirebaseAuth.getInstance().currentUser
        userChatList = ArrayList()
        val ref = FirebaseDatabase.getInstance().reference.child("ChatLists").child(firebaseuser!!.uid)
        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (userChatList as ArrayList).clear()
                for(snapshot in p0.children)
                {
                    val chatlist = snapshot.getValue(Chatlist::class.java)
                    (userChatList as ArrayList).add(chatlist!!)
                }
                retrievechatlist()
            }

        })
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun retrievechatlist()
    {
        mUsers = ArrayList()
        val ref =  FirebaseDatabase.getInstance().reference.child("USERS")
        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList).clear()
                for (datasnapshot in p0.children)
                {
                    val user = datasnapshot.getValue(Users::class.java)
                    for(chatlist in userChatList!!)
                    {
                        if(user!!.getuid() == chatlist.getid() )
                        (mUsers as ArrayList).add(user!!)
                    }
                }
                userAdapter = useradapter(context!!, mUsers as ArrayList<Users>, true)

                rcviewchatlist.adapter = userAdapter

            }

        })

    }

}