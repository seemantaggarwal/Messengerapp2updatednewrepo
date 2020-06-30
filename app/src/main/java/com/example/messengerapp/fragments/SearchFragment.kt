package com.example.messengerapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.ModelClasses.Users
import com.example.messengerapp.R
import com.example.messengerapp.adapterclasses.useradapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  var userAdapter: useradapter? = null
    private var mUsers : List<Users>? = null
    private var recyclerView : RecyclerView?= null
    private  var searchet : EditText? = null


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
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.searchlist)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        mUsers = ArrayList()
        retreiveallusers()
        searchet = view.findViewById(R.id.usersearch)
        searchet!!.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchforuser(s.toString().toLowerCase())
            }

        })
        return view
    }

    private fun retreiveallusers() {
        var firebaseuser  = FirebaseAuth.getInstance().currentUser!!.uid
        var refusers = FirebaseDatabase.getInstance().reference.child("USERS")
        refusers.addValueEventListener(object  : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()

                    for(snapshots in snapshot.children)
                    {
                        val user: Users? = snapshots.getValue(Users::class.java)
                        if(user!!.getuid() != firebaseuser)
                        {
                            (mUsers as ArrayList<Users>).add(user)
                        }

                    }

                userAdapter = useradapter(context!!, mUsers!!, false)
                recyclerView!!.adapter = userAdapter
            }

        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private  fun searchforuser(str : String)
    {
        var firebaseuser  = FirebaseAuth.getInstance().currentUser!!.uid
        var queryusers = FirebaseDatabase.getInstance().reference.child("USERS").orderByChild("search").startAt(str).endAt(str + "\uf8ff")
        queryusers.addValueEventListener(object  : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for(snapshots in snapshot.children)
                {
                    val user: Users? = snapshots.getValue(Users::class.java)
                    if(user!!.getuid() != firebaseuser)
                    {
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }
                userAdapter = useradapter(context!!, mUsers!!, false)
                recyclerView!!.adapter = userAdapter

            }

        })


    }

}