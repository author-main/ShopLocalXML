package com.example.shoplocalxml.repository.database_api

import com.example.shoplocalxml.classes.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface DatabaseApi {
    @Headers("Content-Type: application/json")
    @POST("/api/{script}")
    suspend fun queryUser(@Body user: User, @Path("script") phpScript: String): Response<User>

    @GET("/api/get_products")
    suspend fun getProducts(@Query("token") token: String,
                    @Query("part") part: Int,
                    @Query("order") order: String): Response<List<Product>>

    @GET("/api/get_found_products")
    suspend fun getFoundProducts(@Query("query") query: String,
                         @Query("order") order: String,
                         @Query("portion") portion: Int,
                         @Query("uuid") uuid: String,
                         @Query("token") token: String): Response<List<Product>>


    @GET("/api/get_reviews_product")
    suspend fun getReviewProduct(@Query("id") id: Int): Response<List<Review>>

    @GET("/api/get_brands")
    suspend fun getBrands(): Response<List<Brand>>

    @GET("/api/get_categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("/api/get_messages")
    suspend fun getMessages(@Query("token") token: String, @Query("count") requestCount: Int): Response<List<UserMessage>>

    @FormUrlEncoded
    @POST("/api/update_favorite")
    suspend fun updateFavorite(@Field("token") token: String, @Field("id_product") id_product: Int, @Field("favorite") value: Byte): Response<Int>

    @FormUrlEncoded
    @POST("/api/update_message")
    suspend fun updateUserMessage(@Field("token") token: String, @Field("what") what: Int, @Field("id_message") id_message: String): Response<Int>

}