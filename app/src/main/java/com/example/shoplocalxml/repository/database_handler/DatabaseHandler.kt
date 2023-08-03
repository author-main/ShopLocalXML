package com.example.shoplocalxml.repository.database_handler

import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.classes.UserMessage

interface DatabaseHandler {
    suspend fun updateMessages(token: String, join_read: String?, join_deleted: String?)
    suspend fun getMessages(token: String, requestCount: Int): List<UserMessage>?
    suspend fun getProducts(token: String, page: Int, order: String): List<Product>?
    suspend fun getSearchProducts(token: String, uuid: String, searchQuery:String, page: Int, order: String): List<Product>?
    suspend fun getReviewsProduct(id: Int, limit: Int): List<Review>?
    suspend fun getBrends(): List<Brend>?
    suspend fun getCategories(): List<Category>?
    suspend fun updateProductFavorite(token: String, idProduct: Int, favorite:Boolean)
}