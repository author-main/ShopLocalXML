package com.example.shoplocalxml.classes
import android.content.Context
import android.content.SharedPreferences
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.FILE_PREFERENCES
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id")           var id: Int?,
    @SerializedName("email")        var email: String?,
    @SerializedName("firstname")    val firstname: String?,
    @SerializedName("lastname")     val lastname: String?,
    @SerializedName("phone")        val phone: String?,
    @SerializedName("password")     var password: String?,
    @SerializedName("token")        var token: String?

) {


    fun saveUserData(){
        id          = null
        password    = null
        token       = null
        val gson = Gson()
        val json = gson.toJson(this)
        sharedPrefs.edit().putString("user", json).apply()
    }

    fun validUser() =
        !token.isNullOrEmpty()

    companion object {
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
    }
}