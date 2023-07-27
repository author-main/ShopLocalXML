package com.example.shoplocalxml

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.math.roundToInt


val CART_IMAGE_SIZE = 150.toPx
const val HASH_LENGTH = 32
const val DISK_CACHESIZE  = 50 * 1024 * 1024  // 50Мб

val CACHE_DIR = getCacheDirectory()

fun renameFile(source: String, dest: String){
    renameFile(File(source), File(dest))
}

fun renameFile(source: File, dest: File){
    try {
        if (source.exists()) {
            deleteFile(dest)
            source.renameTo(dest)
        }
    } catch (_: IOException){}
}

fun deleteFile(filename: String) {
    deleteFile(File(filename))
}

fun deleteFiles(folderName: String, mask: String = "*"){
    val dir = File(folderName)
    val files = dir.listFiles(FileFilter {
        if (mask == "*")
            true
        else
            it.extension == mask
    })
    files?.forEach {
        deleteFile(it)
    }
}

fun getFileSize(filename: String): Long {
    val file = File(filename)
    return if (file.exists())
                file.length()
           else
                0
}

fun deleteFile(file: File) {
    try {
        file.delete()
    } catch (_: IOException){}
}

fun createDirectory(value: String): Boolean {
    val dir = File(value)
    return if (!dir.exists()) {
        try {
            dir.mkdirs()
        } catch (_: IOException) {
            false
        }
    } else
        true
}

fun fileExists(fileName: String): Boolean {
    val file = File(fileName)
    return file.exists()
}

fun fileExists(file: File) = file.exists()

fun getCacheDirectory(): String =
    AppShopLocal.applicationContext.applicationInfo.dataDir + "/cache/"


fun getTempDirectory(): String =
    AppShopLocal.applicationContext.applicationInfo.dataDir + "/temp/"

fun loadBitmap(filename: String, reduce: Boolean = false): Bitmap?{
    if (!File(filename).exists())
        return null
    return try {
        val option = BitmapFactory.Options()
        if (reduce) {
            option.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filename, option)
            val reduceSize = option.outWidth.coerceAtLeast(option.outHeight).toDouble()
            val inSampleSize = (reduceSize / CART_IMAGE_SIZE).roundToInt()
            //log("inSampleSize = $inSampleSize, bitmapWidth = ${option.outWidth}")
            option.inSampleSize = inSampleSize * 2
            option.inJustDecodeBounds = false
        }
        option.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(filename, option)
        bitmap
    } catch(_:Exception){
        null
    }
}

fun fileNameFromPath(path: String): String {
    val file = File(path)
    return file.name
}


