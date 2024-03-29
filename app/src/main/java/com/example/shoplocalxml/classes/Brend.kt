package com.example.shoplocalxml.classes

import com.google.gson.annotations.SerializedName

data class Brend(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("link_img")
    val link_img: String,
    @SerializedName("count")
    val count: Int
)

