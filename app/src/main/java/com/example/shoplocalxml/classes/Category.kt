package com.example.shoplocalxml.classes

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("link_img")
    val link_img: String,
    @SerializedName("count")
    val count: Int
)

