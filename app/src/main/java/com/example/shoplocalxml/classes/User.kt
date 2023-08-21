package com.example.shoplocalxml.classes
import android.content.Context
import android.content.SharedPreferences
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.FILE_PREFERENCES
import com.example.shoplocalxml.log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id")           var id: Int?                = null,
    @SerializedName("email")        var email: String?          = null,
    @SerializedName("firstname")    var firstname: String?      = null,
    @SerializedName("lastname")     var lastname: String?       = null,
    @SerializedName("phone")        var phone: String?          = null,
    @SerializedName("password")     var password: String?       = null,
    @SerializedName("token")        var token: String?          = null

) {

  /*  private val sharedPrefs: SharedPreferences =
        applicationContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)*/


    private fun getSharedPreferences() =
        applicationContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)

    fun saveUserData(){
    /*    val sharedPrefs: SharedPreferences =
            applicationContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)*/
        id          = null
        password    = null
        token       = null
        val gson = Gson()
        val json = gson.toJson(this)
        //sharedPrefs
        getSharedPreferences().edit().putString("user", json).apply()
    }

    /*fun validUser() =
        !token.isNullOrEmpty()*/


    /*fun getUserData(): User {
        var user = User()
        try {
            val gson = Gson()
            val json = sharedPrefs.getString("user", null)
            if (!json.isNullOrEmpty())
                user = gson.fromJson(json, User::class.java)
        } catch (_: Exception) {}
        return user
    }*/

    fun getUserData(){
       /* val sharedPrefs: SharedPreferences =
            applicationContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)*/
        var user = User()
        try {
            val gson = Gson()
            val json = //sharedPrefs
                getSharedPreferences().getString("user", null)
            if (!json.isNullOrEmpty())
                user = gson.fromJson(json, User::class.java)
        } catch (_: Exception) {}
        id          = user.id
        email       = user.email
        firstname   = user.firstname
        lastname    = user.lastname
        phone       = user.phone
        password    = user.password
        token       = user.token
    }



   /* companion object {
        private val sharedPrefs: SharedPreferences =
            applicationContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)
        fun getUserData(): User? {
            return try {
                val gson = Gson()
                val json: String? = sharedPrefs.getString("user", null)
                gson.fromJson(json, User::class.java)
            } catch (e: Exception) {
                null
            }
        }
        fun getInstance() =
            User(null,
                null,
                null,
                null,
                null,
                null,
                null)

    }*/
}