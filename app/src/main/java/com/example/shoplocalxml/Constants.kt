package com.example.shoplocalxml

import android.graphics.Bitmap
import android.graphics.Color
import android.icu.text.DecimalFormatSymbols
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.AppShopLocal.Companion.repository

enum class TypeRequest {
    USER_LOGIN,
    USER_REGISTER,
    USER_RESTORE
}
const val KEY_PASSWORD      = "password"
const val EMPTY_STRING      = ""
/*val EMPTY_BITMAP            = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888).apply {
    this.eraseColor(Color.TRANSPARENT)
}*/
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
const val ANY_VALUE = -1
const val DATA_PORTION = 8

const val ID_CATEGORY = 1L
const val ID_BRAND    = 2L
const val FILTER_KEY  = "filter"


    //val decimalSeparator = DecimalFormatSymbols(applicationContext.resources.configuration.locales[0]).decimalSeparator
//const val SERVER_URL = "http://faceshot.ru"