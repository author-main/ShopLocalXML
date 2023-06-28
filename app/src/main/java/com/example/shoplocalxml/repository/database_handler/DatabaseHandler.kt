package com.example.shoplocalxml.repository.database_handler

import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.Product

interface DatabaseHandler {
    suspend fun getProducts(token: String, page: Int, order: String): List<Product>?
    suspend fun getSearchProducts(token: String, uuid: String, searchQuery:String, page: Int, order: String): List<Product>?
    suspend fun getBrends(): List<Brend>?
    suspend fun getCategories(): List<Category>?
    suspend fun updateProductFavorite(token: String, idProduct: Int, favorite:Boolean)
}