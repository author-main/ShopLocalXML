package com.example.shoplocalxml.classes

import com.example.shoplocalxml.EMPTY_STRING
import com.google.gson.annotations.SerializedName

data class UserMessage(
    @SerializedName("id")       var id          :Int = 0,
    @SerializedName("message")  var message     :String = EMPTY_STRING,
    @SerializedName("type")     var type        :Int = 0,
    @SerializedName("read")     var read        :Int = 0,
    @SerializedName("date")     var date        : String = EMPTY_STRING,
    var deleted: Boolean = false
) {
    fun copydata(value: UserMessage) {
        id = value.id
        message = value.message
        type = value.type
        read = value.read
        date = value.date
        deleted = value.deleted
    }
}
