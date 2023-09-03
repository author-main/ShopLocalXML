package com.example.shoplocalxml.repository.database_handler

import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.repository.database_api.DatabaseApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseHandlerImpl @Inject constructor(private val databaseApi: DatabaseApiImpl): DatabaseHandler {

    override suspend fun getMessages(token: String, requestCount: Int): List<UserMessage>? =
        withContext(Dispatchers.IO) {
            databaseApi.getMessages(token, requestCount).body()
        }

    override suspend fun updateMessages(token: String, join_read: String, join_deleted: String) {
        withContext(Dispatchers.IO) {
            databaseApi.updateMessages(token, join_read, join_deleted)
        }
    }

    override suspend fun getSearchProducts(
        token: String,
        uuid: String,
        searchQuery: String,
        page: Int,
        order: String
    ): List<Product>? =
        withContext(Dispatchers.IO) {
            databaseApi.getSearchProducts(token, uuid, searchQuery, page, order)?.body()
        }

    override suspend fun getProducts(token: String, page: Int, order: String): List<Product>? =
        withContext(Dispatchers.IO) {
            databaseApi.getProducts(token, page, order)?.body()
        }

    override suspend fun getBrends(): List<Brend>? =
        withContext(Dispatchers.IO) {
             databaseApi.getBrends().body()
        }

    override suspend fun updateProductFavorite(token: String, idProduct: Int, favorite:Boolean){
        withContext(Dispatchers.IO) {
            databaseApi.updateProductFavorite(token, idProduct, favorite)
        }
    }

    override suspend fun getCategories(): List<Category>? =
        withContext(Dispatchers.IO) {
            databaseApi.getCategories().body()
        }

    override suspend fun getReviewsProduct(id: Int, limit: Int): List<Review>? =
        withContext(Dispatchers.IO) {
            databaseApi.getReviewsProduct(id, limit).body()
        }

    override suspend fun addProductCart(token: String, idProduct: Int): Int =
        withContext(Dispatchers.IO) {
            databaseApi.addProductCart(token, idProduct).body() ?: -1
        }
}