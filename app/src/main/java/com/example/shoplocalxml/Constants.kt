package com.example.shoplocalxml

enum class PasswordSymbol {
    NUMBER,
    FINGER_PRINT,
    BACKSPACE
}
enum class TypeRequest {
    USER_LOGIN,
    USER_REGISTER,
    USER_RESTORE
}

const val KEY_PASSWORD = "password"
const val EMPTY_STRING = ""
const val FILE_PREFERENCES      = "settings"
const val SERVER_URL = "http://192.168.1.10"
//const val SERVER_URL = "http://faceshot.ru"