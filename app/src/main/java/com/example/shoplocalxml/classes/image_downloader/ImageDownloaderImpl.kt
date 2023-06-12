package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap
import android.os.Looper
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.EXT_TEMPFILE
import com.example.shoplocalxml.fileNameFromPath
import com.example.shoplocalxml.getCacheDirectory
import com.example.shoplocalxml.loadBitmap
import com.example.shoplocalxml.log
import com.example.shoplocalxml.md5
import com.example.shoplocalxml.renameFile
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.logging.Handler

class ImageDownloaderImpl
    (   private val url: String = EMPTY_STRING,
        private val reduce: Boolean,
        private var timestamp: Long,
        private val onComplete: (Bitmap?, Long) -> Unit
    ): ImageDownloader {

    override fun download() {
        //val result =
            downloadImage()//url, reduce, timestamp)
      //  onComplete(result.first, result.second)*/
    }

   /* override fun run() {
        //download()
        /*val result = downloadImage()//url, reduce, timestamp)
        onComplete(result.first, result.second)*/
        downloadImage()
    }*/

//    private fun downloadImage(url: String, reduce: Boolean, timestamp: Long): Pair<Bitmap?, Long> {
    private fun downloadImage(): Pair<Bitmap?, Long> {
        var timeStamp = timestamp
        val bufferSize = 32768
        //log("download $url...")
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
        for (i in 0..1000000000){}
        onComplete(bitmap, timeStamp)
        return bitmap to timeStamp
    }



}