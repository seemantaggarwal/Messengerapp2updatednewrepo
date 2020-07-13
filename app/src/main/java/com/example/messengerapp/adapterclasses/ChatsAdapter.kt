package com.example.messengerapp.adapterclasses

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.ModelClasses.Chat
import com.example.messengerapp.ModelClasses.Chatlist
import com.example.messengerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.message_item_letf.view.*

class ChatsAdapter(
    mContext : Context,
    mChatlist: List<Chat>,
    imageUrl : String
): RecyclerView.Adapter<ChatsAdapter.ViewHolder>()
{
    private val mContext: Context
    private val mChatlist : List<Chat>
    private val imageUrl: String
    var firebaseuser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    init {
        this.mContext = mContext
        this.mChatlist = mChatlist
        this.imageUrl = imageUrl
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return  if (position == 1)
        {
            val view: View = LayoutInflater.from(mContext).inflate(com.example.messengerapp.R.layout.message_item_right, parent, false)
            ViewHolder(view)
        }
        else{
            val view: View = LayoutInflater.from(mContext).inflate(R.layout.message_item_letf, parent, false)
            ViewHolder(view)
        }

    }

    override fun getItemCount() = mChatlist.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat : Chat = mChatlist[position]
        Picasso.get().load(imageUrl).into(holder.profileimage)

        if(chat.getmessage() == "sent you an image" && !chat.geturl().equals("")) {
            if (chat.getsender() == firebaseuser!!.uid) {
                holder.showtextmessage!!.visibility = View.GONE
                holder.rightimageview!!.visibility = View.VISIBLE
                Picasso.get().load(chat.geturl()).into(holder.rightimageview)
            }
            else if (chat.getsender() != firebaseuser!!.uid) {
                holder.showtextmessage!!.visibility = View.GONE
                holder.leftimageview!!.visibility = View.VISIBLE
                Picasso.get().load(chat.geturl()).into(holder.leftimageview)
            }
        }
        else
        {
            holder.showtextmessage!!.visibility = View.VISIBLE
            holder.showtextmessage!!.text = chat.getmessage()
        }
        if(position == mChatlist.size - 1)
        {
            if(chat.isisseen()!!)
            {
                holder.textseen!!.setText("SEEN")
                if(chat.getmessage() == "sent you an image" && !chat.geturl().equals("")) {

                    val lp : RelativeLayout.LayoutParams? = holder.textseen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0,245,10,0)
                    holder.textseen!!.layoutParams = lp
                }
            }
            else
            {
                holder.textseen!!.text = "SENT"
                if(chat.getmessage() == "sent you an image" && !chat.geturl().equals("")) {

                    val lp : RelativeLayout.LayoutParams? = holder.textseen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0,245,10,0)
                    holder.textseen!!.layoutParams = lp
                }
            }



        }
        else
        {
            holder.textseen!!.visibility = View.GONE
        }
    }

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)
    {
        var profileimage : CircleImageView? = null
        var showtextmessage : TextView? = null
        var leftimageview : ImageView? = null
        var textseen : TextView? = null
        var rightimageview : ImageView? = null

        init {
            profileimage = itemView.findViewById(R.id.profileimage)
            showtextmessage = itemView.findViewById(R.id.textmessage)
            leftimageview = itemView.findViewById(R.id.leftimageview)
            textseen = itemView.findViewById(R.id.textseen)
            rightimageview = itemView.findViewById(R.id.rightimageview)


        }
    }

    override fun getItemViewType(position: Int): Int {
         return if(mChatlist[position].getsender().equals(firebaseuser.uid))
        {
            1
        }
        else
        {
            0
        }
    }


}