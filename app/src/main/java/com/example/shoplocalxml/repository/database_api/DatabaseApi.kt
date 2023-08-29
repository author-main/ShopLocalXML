package com.example.shoplocalxml.repository.database_api

import com.example.shoplocalxml.classes.*
import retrofit2.Response
import retrofit2.http.*

interface DatabaseApi {
    @Headers("Content-Type: application/json")
    @POST("/api/{script}")
    suspend fun queryUser(@Body user: User, @Path("script") phpScript: String): Response<User>

    @GET("/api/get_found_products")
    suspend fun getSearchProducts(@Query("token") token: String,
                    @Query("uuid") uuid: String,
                    @Query("query") searchQuery: String,
                    @Query("part") part: Int,
                    @Query("order") order: String): Response<List<Product>>

    @GET("/api/get_products")
    suspend fun getProducts(@Query("token") token: String,
                            @Query("part") part: Int,
                            @Query("order") order: String): Response<List<Product>>

    @GET("/api/get_reviews_product")
    suspend fun getReviewsProduct(@Query("id") id: Int, @Query("limit") limit: Int): Response<List<Review>>

    @GET("/api/get_brands")
    suspend fun getBrends(): Response<List<Brend>>

    @GET("/api/get_categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("/api/get_messages")
    suspend fun getMessages(@Query("token") token: String, @Query("count") requestCount: Int): Response<List<UserMessage>>

    @FormUrlEncoded
    @POST("/api/update_favorite")
    suspend fun updateFavorite(@Field("token") token: String, @Field("id_product") id_product: Int, @Field("favorite") value: Byte): Response<Int>

    @FormUrlEncoded
    @POST("/api/update_messages")
    suspend fun updateMessages(@Field("token") token: String, @Field("read") join_read: String, @Field("deleted") join_deleted: String)
}