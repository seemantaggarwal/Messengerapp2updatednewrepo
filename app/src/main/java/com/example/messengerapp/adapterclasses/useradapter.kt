package com.example.messengerapp.adapterclasses

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.MainActivity
import com.example.messengerapp.MessageChatActivity
import com.example.messengerapp.ModelClasses.Users
import com.example.messengerapp.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_search_item_layout.view.*

class useradapter(mContext : Context, mUsersList: List<Users>, isChatCheck: Boolean): RecyclerView.Adapter<useradapter.ViewHolder?>() {
    private val mContext : Context
    private val mUsersList: List<Users>
    private val isChatCheck: Boolean
    init {
        this.mContext = mContext
        this.mUsersList = mUsersList
        this.isChatCheck = isChatCheck
    }
    class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)
    {
        var username :TextView
        var profileimage: CircleImageView
        var online : CircleImageView
        var offline : CircleImageView
        var lastmessage : TextView
        init {
            username  = itemView.findViewById(R.id.username)
            profileimage = itemView.findViewById(R.id.profileimage)
            online = itemView.findViewById(R.id.onlineimage)
            offline = itemView.findViewById(R.id.offlineimage)
            lastmessage = itemView.findViewById(R.id.messagelast)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout,parent, false)
        return useradapter.ViewHolder(view)

    }

    override fun getItemCount() = mUsersList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: Users = mUsersList[position]
        holder.username.text = user!!.getusername()
        Picasso.get().load(user.getprofile()).placeholder(R.drawable.profile).into(holder.profileimage)
        holder.itemView.setOnClickListener{
            val options = arrayOf<CharSequence>(
                "Send Message",
                "View Profile"
            )
            val builder : AlertDialog.Builder =  AlertDialog.Builder(mContext)
            builder.setTitle("Choose one Option")
            builder.setItems(options, DialogInterface.OnClickListener{
                dialog, position ->
                if(position == 0)
                {
                    val intent = Intent(mContext , MessageChatActivity::class.java)
                    intent.putExtra("visit_id", user.getuid())
                    mContext.startActivity(intent)

                }
                else if(position == 1)
                {

                }
            })
            builder.show()
        }
    }
}