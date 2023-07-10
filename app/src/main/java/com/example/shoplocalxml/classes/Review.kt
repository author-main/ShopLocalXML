package com.example.shoplocalxml.classes

import com.google.gson.annotations.SerializedName

data class Review (
    @SerializedName("comment")      var comment     : String,
    @SerializedName("username")     var username    : String,
    @SerializedName("countstar")    var countstar   : Int,
    @SerializedName("date")         var date        : String
)