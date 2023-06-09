package com.example.shoplocalxml


import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.Window
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.toColor
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import java.math.BigInteger
import java.math.RoundingMode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import kotlin.math.roundToInt


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

fun setDialogStyle(dialog: AlertDialog, @StringRes title: Int? = null, noTitle: Boolean = false) {
    val bgColor = applicationContext.getColor(R.color.PrimaryDark)
    val drawable = AppCompatResources.getDrawable(applicationContext, R.drawable.background_dialog)
    drawable!!.setTint(bgColor)
    dialog.window?.let {
        if (noTitle)
            it.requestFeature(Window.FEATURE_NO_TITLE);
        /*val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(it.attributes)
        it.attributes = layoutParams*/
        it.setBackgroundDrawable(drawable)
    }
    title?.let{
        val textView = TextView(dialog.context)
        textView.setText(it)
        textView.setPadding(16.toPx, 16.toPx, 0, 16.toPx)
        dialog.setCustomTitle(textView)
    }
}


fun md5(value: String): String {
    try {
        val md = MessageDigest.getInstance("MD5")
        val messageDigest = md.digest(value.toByteArray())
        var hashtext = BigInteger(1, messageDigest).toString(16)
        while (hashtext.length < HASH_LENGTH)
            hashtext = "0$hashtext"
        return hashtext
    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
    }
}

fun getFormattedFloat(value: Float): String{
    val result = value.roundToInt()
    val dec = DecimalFormat("#,###.00")
    dec.roundingMode = RoundingMode.HALF_EVEN
    return dec.format(result) + getStringResource(R.string.currency)
}

/*fun downloadImage(url: String, reduce: Boolean, timestamp: Long): Pair<Bitmap?, Long> {
    var timeStamp = timestamp
    val bufferSize = 32768
    *//*val fileName = fileNameFromPath(url)
    val fileHash = md5(fileName)*//*
    val fileHash = md5(url)
    val filenameCache = getCacheDirectory() + fileHash
    val filenameTemp  = "$filenameCache$.EXT_TEMPFILE"
    var bitmap: Bitmap? = null
    var success = false

    try {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.connect()
        if (conn.responseCode == HttpURLConnection.HTTP_OK) {
            conn.requestMethod = "HEAD"
            val fileTime = conn.lastModified
            if (timestamp != fileTime) {
                timeStamp = fileTime
                conn.requestMethod = "GET"
                val inputStream = conn.inputStream
                val outputStream = FileOutputStream(filenameTemp)
                val buffer = ByteArray(bufferSize)
                var count: Int
                while (inputStream.read(buffer).also { count = it } > 0) {
                    outputStream.write(buffer, 0, count)
                }
                inputStream.close()
                outputStream.flush()
                outputStream.close()
                bitmap = loadBitmap(filenameTemp, reduce)
                renameFile(filenameTemp, filenameCache)
                conn.disconnect()
                success = true
            }
        }
    } catch (e: Exception) {
        log(e.message)
    }
    if (!success)
        bitmap = loadBitmap(filenameCache, reduce)
    return bitmap to timeStamp
}*/

fun getDisplaySize(): Size {
    val width  = Resources.getSystem().displayMetrics.widthPixels
    val height = Resources.getSystem().displayMetrics.heightPixels
    return Size(width, height)
}


/*fun setWidthDialog(dialog: AlertDialog, widthDP: Int){
    val width: Int = widthDP.toPx
    val height = ViewGroup.LayoutParams.WRAP_CONTENT
    dialog.window!!.setLayout(width, height)
}*/
