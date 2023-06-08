package com.example.shoplocalxml.repository.database_handler

import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Product

interface DatabaseHandler {
    suspend fun getProducts(token: String, page: Int, order: String): List<Product>?
    suspend fun getBrends(): List<Brend>?
}