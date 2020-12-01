package com.example.messengerapp.ModelClasses

class Chat {
    private var sender : String? = ""
    private var message : String? = ""
    private var receiver : String? = ""
    private var isseen  = false
    private var url : String? = ""
    private var messageid : String? = ""

    constructor()
    constructor(
        sender: String?,
        message: String?,
        receiver: String?,
        isseen: Boolean,
        url: String?,
        messageid: String?
    ) {
        this.sender = sender
        this.message = message
        this.receiver = receiver
        this.isseen = isseen
        this.url = url
        this.messageid = messageid
    }

    fun getsender():String?{
        return sender
    }
    fun setsender(sender : String?)
    {
        this.sender = sender!!
    }
    fun getmessage():String?{
        return message
    }
    fun setmessage(message: String?)
    {
        this.message = message!!
    }
    fun getreceiver():String?{
        return receiver
    }
    fun setreceiver(receiver: String?)
    {
        this.receiver = receiver!!
    }
    fun isisseen():Boolean?{
        return isseen
    }
    fun setisseen(isseen: Boolean)
    {
        this.isseen = isseen!!
    }
    fun geturl ():String?{
        return url
    }
    fun seturl (url: String?)
    {
        this.url  = url !!
    }
    fun getmessageid():String?{
        return messageid
    }
    fun setmessageid(messageid: String? )
    {
        this.messageid = messageid!!
    }


}