package com.example.shoplocalxml.repository.database_api

import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.classes.UserMessage
import retrofit2.Response

class DatabaseApiImpl (private val retrofitInstance: DatabaseApi) {
    private val QUERY_REGUSER     = "reg_user"
    private val QUERY_LOGINUSER   = "login_user"
    private val QUERY_RESTOREUSER = "restore_user"


    /*private val retrofitInstance: DatabaseApi
    init{
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofitInstance = retrofit.create(DatabaseApi::class.java)
    }*/

    private suspend fun queryUser(query: String, user: User): Response<User> {
        return retrofitInstance.queryUser(user, query)
    }

    suspend fun loginUser(mail: String, password: String): Response<User> {
        /*val user = User.getInstance()
        user.email      = mail
        user.password   = password*/
        return queryUser(QUERY_LOGINUSER, User(email = mail, password = password))
    }

    suspend fun registerUser(user: User): Response<User> {
        return queryUser(QUERY_REGUSER, user)
    }

    suspend fun restoreUser(user: User): Response<User> {
        return queryUser(QUERY_RESTOREUSER, user)
    }


    suspend fun getSearchProducts(token: String, uuid: String, searchQuery: String, page: Int, order: String): Response<List<Product>>? {
        return try {
            retrofitInstance.getSearchProducts(token, uuid, searchQuery, page, order)
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

    suspend fun addProductCart(token: String, idProduct: Int): Response<Int> =
        retrofitInstance.addProductCart(token, idProduct)

    suspend fun updateMessages(token: String, join_read: String, join_deleted: String){
        /*val read_messages: String    = join_read ?: "x"
        val deleted_messages: String = join_deleted ?: "x"*/
        /*log(token)
        log (read_messages)
        log(deleted_messages)*/
        retrofitInstance.updateMessages(token, join_read, join_deleted)
    }


    suspend fun getReviewsProduct(id: Int, limit: Int): Response<List<Review>> =
        retrofitInstance.getReviewsProduct(id, limit)

    suspend fun getMessages(token: String, requestCount: Int): Response<List<UserMessage>> =
        retrofitInstance.getMessages(token, requestCount)
}