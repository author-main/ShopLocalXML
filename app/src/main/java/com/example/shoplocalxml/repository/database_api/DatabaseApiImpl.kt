package com.example.shoplocalxml.repository.database_api

import com.example.shoplocalxml.SERVER_URL
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.encodeBase64
import com.example.shoplocalxml.log
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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


    suspend fun getSearchProducts(token: String, uuid: String, searchQuery: String, page: Int, order: String): Response<List<Product>>? {
        return try {
            val query64 = encodeBase64(searchQuery)
            retrofitInstance.getSearchProducts(token, uuid, query64, page, order)
        } catch (_: Exception){
            null
        }
    }


    suspend fun getProducts(token: String, page: Int, order: String): Response<List<Product>>? {
        return try {
            retrofitInstance.getProducts(token, page, order)
        } catch (_: Exception){
            null
        }
    }

    suspend fun getBrends(): Response<List<Brend>> =
        retrofitInstance.getBrends()

    suspend fun getCategories(): Response<List<Category>> =
        retrofitInstance.getCategories()

    suspend fun updateProductFavorite(token: String, idProduct: Int, favorite:Boolean){
        retrofitInstance.updateFavorite(token, idProduct, if (favorite) 1 else 0)
    }
}