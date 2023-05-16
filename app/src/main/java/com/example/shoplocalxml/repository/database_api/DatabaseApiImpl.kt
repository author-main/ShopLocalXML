package com.example.shoplocalxml.repository.database_api

import com.example.shoplocalxml.SERVER_URL
import com.example.shoplocalxml.classes.User
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DatabaseApiImpl {
    private val QUERY_REGUSER     = "reg_user"
    private val QUERY_LOGINUSER   = "login_user"
    private val QUERY_RESTOREUSER = "restore_user"
    private val retrofitInstance: DatabaseApi
    init{
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofitInstance = retrofit.create(DatabaseApi::class.java)
    }

    private suspend fun queryUser(query: String, user: User, action: (user: User)->Unit) {
        val response: Response<User> = retrofitInstance.queryUser(user, query)
    }
}