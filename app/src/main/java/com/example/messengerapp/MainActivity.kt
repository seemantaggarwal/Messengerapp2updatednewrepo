package com.example.messengerapp

import ViewPagerAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.messengerapp.ModelClasses.Chat
import com.example.messengerapp.ModelClasses.Chatlist
import com.example.messengerapp.ModelClasses.Users
import com.example.messengerapp.fragments.ChatFragment
import com.example.messengerapp.fragments.SearchFragment
import com.example.messengerapp.fragments.SettingsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


class  MainActivity : AppCompatActivity() {


var refusers : DatabaseReference? = null
    var firebaseuser : FirebaseUser? =null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        firebaseuser = FirebaseAuth.getInstance().currentUser
        refusers = FirebaseDatabase.getInstance().reference.child("USERS").child(firebaseuser!!.uid)


        val toolbar : Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        val fragments = arrayListOf(
            ChatFragment.newInstance(),
            SearchFragment.newInstance(),
            SettingsFragment.newInstance()

        )
        lateinit var names  : ArrayList<String>
//        val adapter = ViewPagerAdapter( this , fragments )
//        view_pager.adapter= adapter
//        TabLayoutMediator(tab_layout, view_pager){tab, position->
//            tab.text = " ${names[position]}"
//        }.attach()

        val ref = FirebaseDatabase.getInstance().reference.child("Chats")
        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                 val adapter = ViewPagerAdapter( this@MainActivity  , fragments )
                var countunread = 0
                for(snapshot in p0.children)
                {
                    val chat = snapshot.getValue(Chat::class.java)
                    if(chat!!.getreceiver() == firebaseuser!!.uid && (!chat.isisseen()!!))
                    {
                        countunread +=1
                    }

                }
                if(countunread == 0)
                {
                            names = arrayListOf<String>(
                                "CHATS",
                                "SEARCH",
                                "SETTINGS"

                            )
                }
                else
                {
                    names = arrayListOf<String>(
                    "($countunread) CHATS",
                    "SEARCH",
                    "SETTINGS"

                )

                }
                view_pager.adapter= adapter
                TabLayoutMediator(tab_layout, view_pager){tab, position->
                    tab.text = " ${names[position]}"
                }.attach()

            }

        }
        )

        refusers!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user: Users? = snapshot.getValue(Users::class.java)
                    user_name.text = user!!.getusername()
                    Picasso.get().load(user.getprofile()).placeholder(R.drawable.profile).into(profileimage)

                }
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
         when (item.itemId) {

            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity , WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                return true
            }
        }
        return false
    }
//    internal class ViewPagerAdapter(fragmentManager : FragmentManager) : FragmentStateAdapter(fragmentManager)
//    {
//        private  val fragments : ArrayList<Fragment>
//        private val titles : ArrayList<String>
//        init {
//            fragments = ArrayList<Fragment>()
//            titles  = ArrayList<String>()
//        }
//
//        override fun getItemCount(): Int {
//            TODO("Not yet implemented")
//            return fragments.size
//        }
//
//        override fun createFragment(position: Int): Fragment {
//            TODO("Not yet implemented")
//            return fragments[position]
//        }
//        fun addfragment(fragment : Fragment, title: String)
//        {
//            fragments.add(fragment)
//            titles.add(title)
//        }
//         fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//            val tabLayout = view.findViewById(R.id.tab_layout)
//             val viewPager: ViewPager2 = findViewById(R.id.view_pager)
//
//             TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                tab.text = titles[position]
//                viewPager.setCurrentItem(tab.position, true)
//            }.attach()
//        }
//
//    }

}

