package com.example.shoplocalxml.repository.database_api

import com.example.shoplocalxml.SERVER_URL
import com.example.shoplocalxml.classes.Product
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

    private suspend fun queryUser(query: String, user: User): Response<User> {
        return retrofitInstance.queryUser(user, query)
    }

    suspend fun loginUser(mail: String, password: String): Response<User> {
        val user = User.getInstance()
        user.email      = mail
        user.password   = password
        return queryUser(QUERY_LOGINUSER, user)
    }

    suspend fun registerUser(user: User): Response<User> {
        return queryUser(QUERY_REGUSER, user)
    }

    suspend fun restoreUser(user: User): Response<User> {
        return queryUser(QUERY_RESTOREUSER, user)
    }

    suspend fun getProducts(token: String, page: Int, order: String): Response<List<Product>> =
        retrofitInstance.getProducts(token, page, order)
}