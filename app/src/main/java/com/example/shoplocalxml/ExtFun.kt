package com.example.shoplocalxml

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.core.graphics.toColor


val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/*val Int.toSp: Int
    get() = (this / Resources.getSystem().displayMetrics.scaledDensity).toInt()*/

fun<T> log(value: T?) {
    if (value != null) {
        if (value is String)
            Log.v("shoplocal", value)
        else
            Log.v("shoplocal", value.toString())
    }
}


fun Int.alpha(value: Float): Int {
        val color = this.toColor()
        val red   = color.red()
        val green = color.green()
        val blue  = color.blue()
        return Color.argb(value, red, green, blue)
}

fun getStringArrayResource(@ArrayRes id: Int): Array<String> =
    AppShopLocal.applicationContext.resources.getStringArray(id)

fun getStringResource(@StringRes id: Int): String =
    try {
        AppShopLocal.applicationContext.getString(id)
    }
    catch (e: Exception){
        EMPTY_STRING
    }

