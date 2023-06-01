package com.example.shoplocalxml.repository.database_handler

import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.log
import com.example.shoplocalxml.repository.database_api.DatabaseApi
import com.example.shoplocalxml.repository.database_api.DatabaseApiImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseHandlerImpl(private val databaseApi: DatabaseApiImpl): DatabaseHandler {
    override suspend fun getProducts(token: String, page: Int, order: String): List<Product>? =
        withContext(Dispatchers.IO) {
            val result = databaseApi.getProducts(token, page, order).body()
            result
        }
}