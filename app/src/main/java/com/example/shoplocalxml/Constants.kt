package com.example.shoplocalxml

import androidx.fragment.app.Fragment

enum class TypeRequest {
    USER_LOGIN,
    USER_REGISTER,
    USER_RESTORE
}
const val KEY_PASSWORD      = "password"
const val EMPTY_STRING      = ""
const val FILE_PREFERENCES  = "settings"
const val SERVER_URL        = "http://192.168.0.10"
//const val SERVER_URL = "http://faceshot.ru"
val Fragment.sharedViewModel: SharedViewModel
    get() = (this.activity as MainActivity).sharedViewModel