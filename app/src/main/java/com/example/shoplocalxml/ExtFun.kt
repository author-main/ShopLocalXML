package com.example.shoplocalxml

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.core.graphics.toColor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext


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

fun isConnectedNet(): Boolean{
    var connected = false
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.activeNetwork?.let {manager ->
        connected = connectivityManager.getNetworkCapabilities(manager)?.let{ capabilities ->
                       capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } ?: false
    }
    return connected
}

fun vibrate(duration: Long) {
    val vibe =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                applicationContext
                    .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            applicationContext
                .getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    val effect: VibrationEffect = VibrationEffect.createOneShot(
        duration,
        VibrationEffect.DEFAULT_AMPLITUDE
    )
    vibe.vibrate(effect)
}

