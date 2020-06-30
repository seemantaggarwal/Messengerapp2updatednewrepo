package com.example.messengerapp.ModelClasses

class Users {
    private  var uid:String = " "
    private  var username:String = " "
    private  var profile:String = " "
    private  var cover:String = " "
    private  var search:String = " "
    private  var email:String = " "
    private  var facebook:String = " "
    private  var instagram:String = " "
    private  var pass:String = " "
    private  var website:String = " "
    private var status :String = " "

    constructor()


    constructor(
        uid: String,
        username: String,
        profile: String,
        cover: String,
        search: String,
        email: String,
        facebook: String,
        instagram: String,
        pass: String,
        website: String
    ) {
        this.uid = uid
        this.username = username
        this.profile = profile
        this.cover = cover
        this.search = search
        this.email = email
        this.facebook = facebook
        this.instagram = instagram
        this.pass = pass
        this.website = website
    }
    fun getuid() : String?{
        return uid
    }
    fun setuid(uid: String){
        this.uid = uid
    }
    fun getusername() : String?{
        return username
    }
    fun setusername(username: String){
        this.username = username
    }
    fun getprofile() : String?{
        return profile
    }
    fun setprofile(profile: String){
        this.profile = profile
    }
    fun getfacebook() : String?{
        return facebook
    }
    fun setfacebook(facebook: String){
        this.facebook = facebook
    }
    fun getcover() : String?{
        return cover
    }
    fun setcover(cover: String){
        this.cover = cover
    }
    fun getsearch() : String?{
        return search
    }
    fun setsearch(search: String){
        this.search = search
    }
    fun getemail() : String?{
        return email
    }
    fun setemail(email: String){
        this.email = email
    }
    fun getinstagram() : String?{
        return instagram
    }
    fun setinstagram(instagram: String){
        this.instagram = instagram
    }
    fun getpass() : String?{
        return pass
    }
    fun setpass(pass: String){
        this.pass = pass
    }
    fun getwebsite() : String?{
        return website
    }
    fun setwebsite(website: String){
        this.website = website
    }
    fun getstatus() : String?{
        return status
    }
    fun setstatus(status : String){
        this.status = status 
    }

}