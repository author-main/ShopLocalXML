package com.example.shoplocalxml

import android.graphics.Bitmap
import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.AppShopLocal.Companion.repository

enum class TypeRequest {
    USER_LOGIN,
    USER_REGISTER,
    USER_RESTORE
}
const val KEY_PASSWORD      = "password"
const val EMPTY_STRING      = ""
val EMPTY_BITMAP            = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888).apply {
    this.eraseColor(Color.TRANSPARENT)
}
const val FILE_PREFERENCES  = "settings"
const val EXT_TEMPFILE = "tmp"
const val SERVER_URL        = "http://192.168.0.10"
//const val SERVER_URL = "http://faceshot.ru"
val Fragment.sharedViewModel: SharedViewModel
    get() = (this.activity as MainActivity).sharedViewModel

    //get() = ViewModelProvider(this.requireActivity(), FactoryViewModel(this.requireActivity(), repository))[SharedViewModel::class.java]*/