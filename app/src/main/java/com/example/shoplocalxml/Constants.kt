package com.example.shoplocalxml

import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext

enum class TypeRequest {
    USER_LOGIN,
    USER_REGISTER,
    USER_RESTORE
}
const val KEY_PASSWORD      = "password"
const val EMPTY_STRING      = ""
const val FILE_PREFERENCES  = "settings"
const val EXT_TEMPFILE = "tmp"
//const val SERVER_URL = "http://faceshot.ru"
const val SERVER_URL        = "http://192.168.1.10"
const val DIR_IMAGES        = "images_ls"
val DEFAULT_BITMAP: Bitmap? = run{
    val drawable = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_default)
    drawable?.toBitmap(width = 24.toPx, height = 24.toPx, Bitmap.Config.ARGB_8888)
}
val paddingProductCard = 12.toPx
val widthProductCard = (getDisplaySize().width - (paddingProductCard * 3)) / 2
const val DATA_PORTION = 8
const val ID_CATEGORY = 1L
const val ID_BRAND    = 2L
const val FILTER_KEY  = "filter"
const val BOUND_BESTSELLER = 10
const val BOUND_ACTION     =  5
const val WORD_REVIEW         = 0
const val WORD_RATE           = 1
const val FRIDAY_PERCENT      = 10