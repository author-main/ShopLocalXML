package com.example.shoplocalxml.classes

import android.graphics.Bitmap
import com.example.shoplocalxml.R
import com.example.shoplocalxml.getStringResource
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name:           String,
    @SerializedName("category")
    var category:       Int? = null,
    @SerializedName("description")
    var description:    String,
    @SerializedName("instock")
    var instock:        Int = 1,
    @SerializedName("discount")
    var discount:       Int,
    @SerializedName("price")
    var price:          Float,
    @SerializedName("star")
    var star:           Float = 1.0f,
    @SerializedName("favorite")
    var favorite:       Byte = 0,
    @SerializedName("brand")
    var brand:          Int? = null,
    @SerializedName("sold")
    var sold:          Int? = null,
    @SerializedName("linkimages")
    var linkimages:     List<String>? = null,
    ) {
        constructor(): this(
                id              = -1,
                name            = "",
                category        = 0,
                description     = "",
                instock         = 0,
                discount        = 0,
                price           = 0.0f,
                star            = 1.0f,
                favorite        = 0,
                brand           = 0,
                sold            = 0,
                linkimages      =  emptyList<String>()
        )
 }