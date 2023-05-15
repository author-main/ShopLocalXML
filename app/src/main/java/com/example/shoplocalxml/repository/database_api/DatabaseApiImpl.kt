package com.example.shoplocalxml.repository.database_api

import com.example.shoplocalxml.SERVER_URL
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DatabaseApiImpl: DatabaseApi {
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
}